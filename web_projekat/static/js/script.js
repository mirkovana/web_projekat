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
			//console.log(d.message);
			var n = d.prazan;
			if(n.localeCompare("emaillozinka")==0){
				$("#log_war_email").text("Unos email adrese je obavezan!").css('color', 'red');
				$("#log_war_email").show(); 
				$("#log_war_pass").text("Unos lozinke je obavezan!");
				$("#log_war_pass").css('color', 'red')
				$("#log_war_pass").show(); 				
			}else if(n.localeCompare("email")==0){
				$("#log_war_email").text("Unos email adrese je obavezan!").css('color', 'red');
				$("#log_war_email").show(); 
			}else if(n.localeCompare("lozinka")==0){
				$("#log_war_pass").text("Unos lozinke je obavezan!");
				$("#log_war_pass").css('color', 'red')
				$("#log_war_pass").show();  
			}else if(d.message.localeCompare("false")==0) {
				$("#log_war").text("Neispravan email ili lozinka!");
				$("#log_war").show();				
			}
			else {				
				korisnikUloga = d.uloga;
				if(korisnikUloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else if(korisnikUloga.localeCompare("admin")==0)
					window.location.replace("/adminPocetna.html");
				else
					window.location.replace("/korisnikPOcetna.html");
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

function addOrganizacija() {
	var data = getFormData($("#prikaz")); //ili dic class="prikaz" NIJE DOBRO OVO SA tabela_ostali_podaci
	var racun = JSON.stringify(data);
	$.ajax({
		url: "rest/addOrganizacija",
		type: "POST",
		data: racun,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			
			if(d.added) {
				var table = $("#tabela_ostali_podaci");
				//var selectRacuni = $("select[name=\"racun\"]");
				table.append(makeTableRow(JSON.parse(racun)));
				//selectRacuni.append(makeSelectOption(JSON.parse(racun)));
				//$("select[name=\"tipRacuna\"]").val($("select[name=\"tipRacuna\"] option:first").val());
				window.location.replace("/supAdminPocetna.html");
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
	  table = document.getElementById("tabela_ostali_podaci");
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
			$("#tabela_ostali_podaci").show();
			$("#tr_forma").hide();
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#tabela_header").append(tableHeader("organizacije"));
			for(let o of organizacije) {
				table.append(makeTableRowIzbor("organizacije",o));
			}
			$(".prikaz").append("<br><button id = \"dodaj\" onclick=\"\" ><a href = \"dodajOrganizaciju.html\" class=\"btn btn-primary\">Dodaj</a></button>");
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
			$("#tabela_ostali_podaci").show();
			$("#tr_forma").hide();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#tabela_header").append(tableHeader("korisnici"));
			for(let k of korisnici) {
				table.append(makeTableRowIzbor("korisnici",k));
			}
			$(".prikaz").append("<br><button id = \"dodaj\" onclick=\"\"><a href = \"dodajKorisnika.html\" class=\"btn btn-primary\">Dodaj</a></button>");
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
			$("#tabela_ostali_podaci").show();
			$("#tr_forma").hide();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#tabela_header").append(tableHeader("kategorije"));
			for(let k of kategorije) {
				table.append(makeTableRowIzbor("kategorije",k));
			}
			$(".prikaz").append("<button id = \"dodaj\" onclick=\"\">Dodaj</button>");
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
			$("#tabela_ostali_podaci").show();
			$("#tr_forma").hide();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#tabela_header").append(tableHeader("diskovi"));
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
			$("#tabela_ostali_podaci").show();
			$("#tr_forma").show();
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
			`
				<tr id = 'brisi'>
					<th>Ime</th>
					<th>Opis</th>
					<th>Logo</th>
				</tr>
			`;
	}else if(izbor == "korisnici"){
		row =
			`
				<tr id = 'brisi'>
					<th>Email</th>
					<th>Ime</th>
					<th>Prezime</th>
					<th>Naziv organizacije</th>
				</tr>
			`;
	}
	else if(izbor == "diskovi"){
		row =
			`
				<tr id = 'brisi'>
					<th>Naziv diska</th>
					<th>Kapacitet</th>
					<th>Naziv vm</th>
					
				</tr>
			`;
	}
	
	else if(izbor == "kategorije"){
		row =
			`
				<tr id = 'brisi'>
					<th>Naziv kategorije</th>
					<th>Broj jezgara</th>
					<th>Ram</th>
					<th>Gpu</th>
					
				</tr>
			`;
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
	var table = $("#tabela_ostali_podaci");
	$("#tabela_ostali_podaci tbody tr").remove();
	$("#brisi").remove();
	$("button#dodaj").remove();
	$("#pretraga").show();
	var row = `<tr  id = 'brisi'>
					<th>Ime</th>
					<th>Broj jezgara</th>
					<th>RAM</th>
					<th>GPU</th>
					<th>Organizacija</th>
				</tr>`
	table.append(row);
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
				window.location.replace("/index.html");
			}
			else {
				$("#homepage").show();
				ucitaj();
			}
		}
	});
}


