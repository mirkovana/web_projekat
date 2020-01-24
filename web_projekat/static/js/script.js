function getFormData($form) { //ucitava sa svake forme u bilo kom .html fajlu
	var unindexedArray = $form.serializeArray();
	var indexedArray = {}
	
	$.map(unindexedArray, function(n, i){
		indexedArray[n['name']] = n['value'];
    });

    return indexedArray;
}

function login() {
	var data = getFormData($("#login")); 
	var s = JSON.stringify(data); 
	$.ajax({
		url: "rest/login",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText); 
			console.log(d.message);
			if(!d.message) {
				$("#log_war").text("Pogrešan email ili lozinka!");
				$("#log_war").show(); 
			}
			else {
				window.location.replace("/login.html");
			}
		} 
	});
}

function filter(){
	var data = getFormData($("#filter")); 
	var s = JSON.stringify(data); 
	$.ajax({
		url: "rest/filter",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			virtuelneMasine = JSON.parse(data.responseText);
			console.log(virtuelneMasine);
			loadVM(virtuelneMasine);
			$("#homepage").show();
		} 
	});
}

function addRacun() {
	var data = getFormData($("#add_racun"));
	data.aktivan = "true";
	data.tipRacuna = data.tipRacuna.toUpperCase();
	console.log(data);
	var racun = JSON.stringify(data);
	$.ajax({
		url: "rest/addRacun",
		type: "POST",
		data: racun,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			
			if(d.added) {
				var table = $("#table_racuni");
				var selectRacuni = $("select[name=\"racun\"]");
				table.append(makeTableRow(JSON.parse(racun)));
				selectRacuni.append(makeSelectOption(JSON.parse(racun)));
				
			/*	$(':input','#add_racun')
				  .not(':button, :submit, :reset, :hidden')
				  .val('')
				  .prop('checked', false)
				  .prop('selected', false);
				*/
				$("select[name=\"tipRacuna\"]").val($("select[name=\"tipRacuna\"] option:first").val());
			}
		} 
	});
}

function uplati() {
	var data = getFormData($("#uplata"));
	var uplataRacun = JSON.stringify(data);
	$.ajax({
		url: "rest/uplataRacun",
		type: "POST",
		data: uplataRacun,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			console.log(d);
			var tableRow = $("td").filter(function() {
			    return $(this).text() == d.brojRacuna;
			}).closest("tr");
			
			tableRow.replaceWith(makeTableRow(d));
		} 
	});
}

function obrisi(brojRacuna) {
	$.ajax({
		url: "rest/obrisiRacun?brRacuna=" + brojRacuna,
		type: "POST",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			console.log(d);
			if(d.good) {
				$("td").filter(function() {
				    return $(this).text() == brojRacuna;
				}).closest("tr").remove();
				$("option[value='" + brojRacuna + "']").remove();
			}
		} 
	});
}

function postaviAktivnost(brojRacuna) {
	$.ajax({
		url: "rest/postaviAktivnost?brRacuna=" + brojRacuna,
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);

			$("td").filter(function() {
			    return $(this).text() == brojRacuna;
			}).closest("tr").replaceWith(makeTableRow(d));
			if(!d.aktivan) 
				$("option[value='" + brojRacuna + "']").remove();
			else 
				$("select[name=\"racun\"]").append(makeSelectOption(d));
		} 
	});
}

function isLoggedIn() {
	$.ajax({
		url: "rest/isLoggedIn",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			if(d.loggedIn) {
				window.location.replace("/");
			}
		}
	});
}

function isLoggedOut() {
	$.ajax({
		url: "rest/isLoggedIn",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			if(!d.loggedIn) {
				window.location.replace("/login.html");
			}
			else {
				$("#homepage").show();
				setUpUserPage();
			}
		}
	});
}

function ucitaj() {
	$.ajax({
		url: "rest/ucitaj",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			virtuelneMasine = JSON.parse(data.responseText);
			console.log(virtuelneMasine);
			loadVM(virtuelneMasine);
			$("#homepage").show();
		}
	});
}

function loadVM(virtuelneMasine) {
	var table = $("#tabela_vm");
	$("#tabela_vm tbody tr").remove();
	for(let vm of virtuelneMasine) {
		table.append(makeTableRow(vm));
	}
}

function capitalize(s) {
	s = s.toLowerCase();
	if (typeof s !== 'string') return ''
	return s.charAt(0).toUpperCase() + s.slice(1)
}

function makeTableRow(racun) {
	var naziv = racun.ime;
	var kat = racun.kategorija;
	var brj = racun.brojJezgara;
	var ram = racun.RAM;
	var gpu = racun.GPU;
	var row =
		`<tbody><tr>
			<td>${naziv}</td>
			<td>${brj}</td>
			<td>${ram}</td>
			<td>${gpu}</td>
			<td>${kat}</td>
		</tr></tbody>`;
	
	return row;
}