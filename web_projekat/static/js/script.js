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

function pretraga() {
	  // Declare variables
	  var input, filter, table, tr, td, i, txtValue;
	  input = document.getElementById("pretraga");
	  filter = input.value.toUpperCase();
	  table = document.getElementById("tabela_vm");
	  tr = table.getElementsByTagName("tr");

	  // Loop through all table rows, and hide those who don't match the search query
	  for (i = 0; i < tr.length; i++) {
	    td = tr[i].getElementsByTagName("td")[0];
	    if (td) {
	      txtValue = td.textContent || td.innerText;
	      if (txtValue.toUpperCase().indexOf(filter) > -1) {
	        tr[i].style.display = "";
	      } else {
	        tr[i].style.display = "none";
	      }
	    }
	  }
	}

function ucitajOrganizacije() {
	$.ajax({
		url: "rest/ucitajOrganizacije",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			organizacije = JSON.parse(data.responseText);
			console.log(organizacije);			
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#tabela_ostali_podaci thead tr").remove();
			$("button#dodaj").remove();
			table.append(tableHeader("organizacije"));
			for(let o of organizacije) {
				table.append(makeTableRowIzbor("organizacije",o));
			}
			$("body").append("<button id = \"dodaj\" onclick=\"\"><a href = \"dodajOrganizaciju.html\">Dodaj</a></button>");
			$("#homepage").show();
		}
	});
}
function ucitajKorisnike() {
	$.ajax({
		url: "rest/ucitajKorisnike",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			korisnici = JSON.parse(data.responseText);
			console.log(korisnici);
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#tabela_ostali_podaci thead tr").remove();
			$("button#dodaj").remove();
			table.append(tableHeader("korisnici"));
			for(let k of korisnici) {
				table.append(makeTableRowIzbor("korisnici",k));
			}
			$("body").append("<button id = \"dodaj\" onclick=\"\">Dodaj</button>");
			$("#homepage").show();
		}
	});
}
function ucitajKategorije() {
	$.ajax({
		url: "rest/ucitajKategorije",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			kategorije = JSON.parse(data.responseText);
			console.log(kategorije);			
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#tabela_ostali_podaci thead tr").remove();
			$("button#dodaj").remove();
			table.append(tableHeader("kategorije"));
			for(let k of kategorije) {
				table.append(makeTableRowIzbor("kategorije",k));
			}
			$("body").append("<button id = \"dodaj\" onclick=\"\">Dodaj</button>");
			$("#homepage").show();
		}
	});
}
function ucitajDiskove() {
	$.ajax({
		url: "rest/ucitajDiskove",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			diskovi = JSON.parse(data.responseText);
			console.log(diskovi);
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#tabela_ostali_podaci thead tr").remove();
			$("button#dodaj").remove();
			table.append(tableHeader("diskovi"));
			for(let d of diskovi) {
				table.append(makeTableRowIzbor("diskovi",d));
			}
			$("#homepage").show();
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

function tableHeader(izbor){
	var row = "";
	if(izbor == "organizacije"){
		row =
			`<thead class='thead-light'>
				<tr>
					<th>Ime</th>
					<th>Opis</th>
					<th>Logo</th>
				</tr>
			</thead>`;
	}else if(izbor == "korisnici"){
		row =
			`<thead class='thead-light'>
				<tr>
					<th>Email</th>
					<th>Ime</th>
					<th>Prezime</th>
					<th>Naziv organizacije</th>
				</tr>
			</thead>`;
	}
	else if(izbor == "diskovi"){
		row =
			`<thead class='thead-light'>
				<tr>
					<th>Naziv diska</th>
					<th>Kapacitet</th>
					<th>Naziv vm</th>
					
				</tr>
			</thead>`;
	}
	
	else if(izbor == "kategorije"){
		row =
			`<thead class='thead-light'>
				<tr>
					<th>Naziv kategorije</th>
					<th>Broj jezgara</th>
					<th>Ram</th>
					<th>Gpu</th>
					
				</tr>
			</thead>`;
	}
	return row;
}

function makeTableRowIzbor(izbor,obj) {
	var row = "";
	if(izbor == "organizacije"){
	   row =
		`<tbody>
		<tr>
			<td class='align-middle'><span class="link" onclick="">${obj.ime}</span></td>
			<td class='align-middle'>${obj.opis}</td>
			<td class='align-middle'>${obj.logo}</td>
		</tr>
		</tbody>`;
	}
	else if(izbor == "korisnici"){
		   row =
			`<tbody>
			    <tr>
					<td class='align-middle'><span class="link" onclick="">${obj.email}</span></td>
					<td class='align-middle'>${obj.ime}</td>
					<td class='align-middle'>${obj.prezime}</td>
					<td class='align-middle'>${obj.organizacija}</td>
			   </tr>
			   </tbody>`;
		}
	else if(izbor =="diskovi"){
		row =
			`<tbody>
			    <tr>
					<td class='align-middle'><span class="link" onclick="">${obj.ime}</span></td>
					<td class='align-middle'>${obj.kapacitet}</td>
					<td class='align-middle'>${obj.vm}</td>
					
			   </tr>
			   </tbody>`;
		
	}
	
	else if(izbor =="kategorije"){
		row =
			`<tbody>
			    <tr>
					<td class='align-middle'>${obj.ime}</td>
					<td class='align-middle'>${obj.brojJezgara}</td>
					<td class='align-middle'>${obj.RAM}</td>
					<td class='align-middle'>${obj.GPU}</td>

			   </tr>
			   </tbody>`;
		
	}
	return row;
}

function loadVM(virtuelneMasine) {
	var table = $("#tabela_vm");
	$("#tabela_vm tbody tr").remove();
	for(let vm of virtuelneMasine) {
		table.append(makeTableRow(vm));
	}
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