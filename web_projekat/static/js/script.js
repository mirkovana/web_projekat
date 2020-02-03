var korisnikUloga = "";
function getFormData($form) { //ucitava sa svake forme u bilo kom .html fajlu
	var unindexedArray = $form.serializeArray();
	var indexedArray = {};
	
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
				$("#log_war").text("Neispravan email ili lozinka.");
				$("#log_war").show();				
			}
			else {				
				korisnikUloga = d.uloga;
				console.log(korisnikUloga);
				if(korisnikUloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else if(korisnikUloga.localeCompare("admin")==0)
					window.location.replace("/adminPocetna.html");
				else
					window.location.replace("/korisnikPocetna.html");
			}
		} 
	});
}

function getUlogovan(fja){
	var ul;
	$.ajax({
		url: "rest/getUlogovan",
		type: "GET",
		contentType: "application/json",
		complete : function (data) {
			d = JSON.parse(data.responseText); 
			console.log("ULOGA" + d.uloga);
			ul = d.uloga;
			if(fja.localeCompare("ucitaj")==0)
				ucitajDiskove(ul);
			else
				filterDiskovi(ul);
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
	var data = getFormData($("#dodajOrganizaciju")); //ili dic class="prikaz" NIJE DOBRO OVO SA tabela_ostali_podaci
	var filePath=$('#inputFile').val();
	data['logo'] = filePath;
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
				if(d.uloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else
					window.location.replace("/adminPocetna.html");
			}
		} 
	});
}

function proveriUlogovanog(){
	$.ajax({
		url: "rest/ucitajOrganizacije",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			console.log(d);
			document.getElementById("inputIme").placeholder = d.ime;
			document.getElementById("inputOpis").placeholde = d.opis;
		}
	});
}

function proveriUlogovanogDisk(disk){
	$.ajax({
		url: "rest/ucitajDiskove",
		type: "GET",
		contentType: "application/json",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			for(let o of d) {
				if(o.ime.localeCompare(disk)===0){
					console.log("popunjavanje polja iz skrpta za izmenu diska "+o+" kraj");
					document.getElementById("ime").placeholder = o.ime;
					$("tip").val(o.tipDiska);
					//document.getElementById("tip").value = podaci.tip;
					document.getElementById("kapacitet").placeholder = o.kapacitet;
					document.getElementById("nazivVM").placeholder = o.vm;
					break;
				}
			}
			
		}
	});
}

function proveriUlogovanogKategorija(){
	$.ajax({
		url: "rest/ucitajKategorije",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			console.log("popunjavanje polja iz skrpta za izmenu diska "+d+" kraj");
            var podaci = d[0];
			document.getElementById("ime").value = podaci.ime;
			document.getElementById("brojJezgara").value = podaci.brojJezgara;
			document.getElementById("RAM").value = podaci.RAM;
			document.getElementById("GPU").value = podaci.GPU;
		}
	});
}

function proveriUlogovanogDiskDodavanje(){
	$.ajax({
		url: "rest/ucitajDiskove",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			console.log("popunjavanje polja iz skrpta za izmenu diska "+d+" kraj");
            var podaci = d[0];
			
			document.getElementById("nazivVM").value = podaci.vm;
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

function obrisiDisk() {
	var data2 = {};
	data2["ime"] = document.getElementById("ime").placeholder;
	var s = JSON.stringify(data2);
	$.ajax({
		url: "rest/obrisiDisk",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			var mess = d.good;
			if(mess){
				window.location.replace("/supAdminPocetna.html");
			}
			else{
				alert("Ne uspelo brisanje");
			}
		}
	});
}

function izmenaDisk(){
	var data2 = getFormData($("#izmenaDisk"));
	data2["ime"] = document.getElementById("ime").placeholder;
	var s = JSON.stringify(data2);
	$.ajax({
		url: "rest/izmenaDiska",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			var mess = d.good;
			if(mess){
				window.location.replace("/supAdminPocetna.html");
			}
			else{
				alert("Ne uspela izmena");
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

function izmenaOrganizacije(){
	window.location.replace("/dodajOrganizaciju.html");
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
			$("#tr_forma_diskovi").hide();
			$("#tr_forma").hide();
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#tabela_header").append(tableHeader("organizacije"));
			for(let o of organizacije) {
				table.append(makeTableRowIzbor("organizacije",o));
			}
			console.log(korisnikUloga);
			$(".prikaz").append("<button id = \"dodaj\" onclick=\"\" ><a href = \"dodajOrganizaciju.html\" class=\"btn btn-primary\">Dodaj</a></button>");
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
			$("#tr_forma_diskovi").hide();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#tabela_header").append(tableHeader("korisnici"));
			for(let k of korisnici) {
				table.append(makeTableRowIzbor("korisnici",k));
			}
			$(".prikaz").append("<button id = \"dodaj\" onclick=\"\"><a href = \"dodajKorisnika.html\" class=\"btn btn-primary\">Dodaj</a></button>");
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
			$("#tr_forma_diskovi").hide();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("button#dodaj").remove();
			$("#pretraga").hide();
			$("#filter").hide();
			//document.getElementById('filter').remove();
			$("#tabela_header").append(tableHeader("kategorije"));
			for(let k of kategorije) {
				table.append(makeTableRowIzbor("kategorije",k));
			}
			$(".prikaz").append("<button id = \"dodaj\" onclick=\"\" ><a href = \"dodajKategoriju.html\" class=\"btn btn-primary\">Dodaj</a></button>");
			$("#homepage").show();
		}
	});
}
function ucitajDiskove(uloga) {
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
		    $("#tr_forma_diskovi").show();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("#brisi").remove();
			$("#dodaj").remove();
			$("#tabela_header").append(tableHeader("diskovi"));
			for(let d of diskovi) {
				table.append(makeTableRowIzbor("diskovi",d));
			}
            if(uloga.localeCompare("ADMIN")>-1)
            	$(".prikaz").append("<button id = \"dodaj\" onclick=\"\"><a href = \"dodajDisk.html\" class=\"btn btn-primary\">Dodaj</a></button>"); 
			
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
			$("#tr_forma_diskovi").hide();
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
					<td class='align-middle'><span class="link" onclick="izmenaKorisnika('${obj.email}')">${obj.email}</span></td>
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
					<td id = "klik" class='align-middle'><span class="link" onclick="izmenaDiska('${obj.ime}')">${obj.ime}</span></td>
					<td id = "klik" class='align-middle'>${obj.kapacitet}</td>
					<td id = "klik" class='align-middle'>${obj.vm}</td>					
			   </tr>
			   </tbody>`;
		
	}
	
	else if(izbor =="kategorije"){
		row =
			`<tbody>
			    <tr>
					<td class='align-middle'><span class="link" onclick="izmenaKategorije()">${obj.ime}</td>
					<td class='align-middle'>${obj.brojJezgara}</td>
					<td class='align-middle'>${obj.RAM}</td>
					<td class='align-middle'>${obj.GPU}</td>

			   </tr>
			   </tbody>`;
		
	}
	return row;
}

function izmenaDiska(obj){
	location.replace("izmenaDisk.html?x="+obj);
}

function izmenaKorisnika(obj){
	location.replace("izmenaProfila.html?x="+obj);
}

function izmenaViruelneMasine(obj){
	location.replace("izmenaVirtuelneMasine.html?x="+obj);
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function izmenaKategorije(){
	location.replace("izmenaKategorije.html");
}

function loadVM(virtuelneMasine) {
	var table = $("#tabela_ostali_podaci");
	$("#tabela_ostali_podaci tbody tr").remove();
	$("#brisi").remove();
	$("button#dodaj").remove();
	$("#pretraga").show();
	$("#tr_forma_diskovi").hide();
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
	$(".prikaz").append("<button id = \"dodaj\" onclick=\"\"><a href = \"dodajVirtuelnuMasinu.html\" class=\"btn btn-primary\">Dodaj</a></button>"); 
}


function makeTableRow(vm) {
	var naziv = vm.ime;
	var kat = vm.organizacija;
	var brj = vm.brojJezgara;
	var ram = vm.RAM;
	var gpu = vm.GPU;
	var row =
		`<tbody><tr onclick="izmenaViruelneMasine('${naziv}')">
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

function filterDiskovi(uloga){
	var data = getFormData($("#filter_diskovi"));
	var s = JSON.stringify(data); 
	$.ajax({
		url: "rest/filterDiskovi",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			diskovi = JSON.parse(data.responseText);
			console.log(diskovi);
			//$("#tabela_ostali_podaci").show();
		    $("#tr_forma").hide();
		    $("#tr_forma_diskovi").show();
			var table = $("#tabela_ostali_podaci");
			$("#tabela_ostali_podaci tbody tr").remove();
			$("button#dodaj").remove();
			for(let d of diskovi) {
				table.append(makeTableRowIzbor("diskovi",d));
			}
			
			console.log("KORISINIKKK  " + uloga + "   cm");
			
			if(uloga.localeCompare("ADMIN")>-1){
		        $(".prikaz").append("<br><button id = \"dodaj\" onclick=\"\"><a href = \"dodajDisk.html\" class=\"btn btn-primary\">Dodaj</a></button>");
		    console.log("USAO U IFFFFFFFFFFFFFFFFFF");  
		    }  
		}
	});
}

function loadDiskovi(diskovii) {
	var table = $("#tabela_ostali_podaci_diskovi");
	$("#tabela_ostali_podaci tbody tr").remove();//nzm jel treba
	$("#tabela_ostali_podaci_diskovi tbody tr").remove();
	$("#brisi").remove();
	$("button#dodaj").remove();
	$("#pretraga").show();
	var row = `<tr  id = 'brisi'>
					<th>Ime</th>
					<th>Kapacitet</th>
					<th>Naziv VM</th>
				</tr>`
	table.append(row);
	for(let disk of diskovii) {
		table.append(makeTableRowIzbor("diskovi", disk));
	}
}

/*function makeTableRowDiskovi(disk) {
	var naziv = disk.ime;
	var kapacitet = disk.kapacitet;
	var vm = disk.vm;
	var row =
		`<tbody><tr>
			<td>${naziv}</td>
			<td>${kapacitet}</td>
			<td>${vm}</td>
		</tr></tbody>`;
	
	return row;
}*/

function addDisk() {
	var data = getFormData($("#dodajDisk")); //ili dic class="prikaz" NIJE DOBRO OVO SA tabela_ostali_podaci
	data["stariDisk"] = document.getElementById("ime").placeholder;
	var racun = JSON.stringify(data);
	$.ajax({
		url: "rest/addDisk",
		type: "POST",
		data: racun,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);			
			if(d.added.localeCompare("true")==0) {
				console.log("ulazzzz");
				if(d.uloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else
					window.location.replace("/adminPocetna.html");
			}
			else
				alert("Nije uspelo");
		} 
	});
}

function addKategorija() {
	var data = getFormData($("#dodajKategoriju"));
	var racun = JSON.stringify(data);
	$.ajax({
		url: "rest/addKategorija",
		type: "POST",
		data: racun,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);			
			if(d.added) {
				console.log("ulazzzz");
				if(d.uloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
			}
		} 
	});
}

function vmDodaj(){
	window.location.replace("/dodajVirtuelnuMasinu.html");
}