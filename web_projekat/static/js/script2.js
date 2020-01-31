function getFormData($form) { //ucitava sa svake forme u bilo kom .html fajlu
	var unindexedArray = $form.serializeArray();
	var indexedArray = {}
	$.map(unindexedArray, function(n, i){
		indexedArray[n['name']] = n['value'];
    });

    return indexedArray;
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function izmeni(){
	var data2 = getFormData($("#izmenaPodataka")); 
	console.log(data2);
	data2["stariKorisnik"] = document.getElementById("email").placeholder;
	var s = JSON.stringify(data2);
	$.ajax({
		url: "rest/izmeni",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			if(d.izmena.localeCompare("0")==0)
				//vec postoji
			{
				$("#log_war_mess").text("Korisnik sa tim mejlom vec postoji!");
				$("#log_war_mess").css('color', 'red');
			}
			if(d.izmena.localeCompare("-1")==0)
				//fali lozinka2
			{
				$("#log_war_pass").text("Potvrdi lozinku!");
				$("#log_war_pass").css('color', 'red');
				//document.location.reload(false);
			}
			if(d.izmena.localeCompare("1")==0)
			//sve okej
			{
				korisnikUloga = d.uloga;
				console.log(korisnikUloga);
				if(korisnikUloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else if(korisnikUloga.localeCompare("admin")==0)
					window.location.replace("/adminPocetna.html");
				else
					window.location.replace("/korisnikPOcetna.html");
					
			}	
			if(d.izmena.localeCompare("-2")==0)
			//lozinke se ne poklapaju
			{
				$("#log_war_mess").text("Lozinke se ne poklapaju!");
				$("#log_war_mess").css('color', 'red');
			}
		} 
	});
}

function getUlogovan(){ // u izmeni profila
	$.ajax({
		url: "rest/getUlogovan",
		type: "GET",
		contentType: "application/json",
		complete : function (data) {
			d = JSON.parse(data.responseText); 
			document.getElementById("ime").placeholder = d.ime;
			document.getElementById("prezime").placeholder = d.prezime;
			document.getElementById("email").placeholder = d.email;
			document.getElementById("lozinka").placeholder = d.lozinka;
		} 
	});
}

function getUlogovanDodavanje(){ 
// prilikom dodavanja korisnika ako je ulogovan korisnik admin ne moze da bira organizaciju za novog korisnika
	$.ajax({
		url: "rest/getUlogovan",
		type: "GET",
		contentType: "application/json",
		complete : function (data) {
			d = JSON.parse(data.responseText); 
			if(d.uloga.localeCompare("ADMIN")==0)
				$("#organizacija").hide();
			else
			{
				var org = getOrganizacije();
			}
		} 
	});
}

function getOrganizacije() // da bi se ubacile u select tag
{
	$.ajax({
		url: "rest/ucitajOrganizacije",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			organizacije = JSON.parse(data.responseText);
			console.log(organizacije);			
			for(let o of organizacije) {
				var x = document.getElementById("orgSelect");
				var opt = document.createElement("option");
				opt.text = o.ime;
				opt.value = o.ime; 
				x.add(opt); 
				console.log(o.ime);			
			}
		}
	});
}

function dodajKorisnika()
{
	var data2 = getFormData($("#dodajKorisnika")); 
	var s = JSON.stringify(data2);
	$.ajax({
		url: "rest/dodajKorisnika",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			korisnikUloga = d.uloga;
			if(d.added.localeCompare("true")==0){
				if(korisnikUloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else if(korisnikUloga.localeCompare("admin")==0)
					window.location.replace("/adminPocetna.html");
				else
					window.location.replace("/korisnikPOcetna.html");
			}
			else{
				if(d.added.localeCompare("imaKorisnik")==0)	{
					$("#log_war").text("Takav korisnik vec postoji!");
					$("#log_war").css('color', 'red');
					$("#log_war").show();
				}
				else{
					$("#log_war").text("Nisu sva polja popunjena!");
					$("#log_war").css('color', 'red');
					$("#log_war").show();
				}
			} 
				
		} 
	});
}

function KorisnikKojiSeMenja(kor){
	if(kor.localeCompare("none")===0){
		getUlogovan();
		$("#obrisi_btn").hide();
		return;
	}
	console.log("KORISNIK " + kor);
	$.ajax({
		url: "rest/ucitajKorisnike",
		type: "GET",
		contentType: "application/json",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			for(let o of d) {
				if(o.email.localeCompare(kor)===0){
					document.getElementById("ime").placeholder = o.ime;
					document.getElementById("prezime").placeholder = o.prezime;
					document.getElementById("email").placeholder = o.email;
					document.getElementById("lozinka").placeholder = o.lozinka;
					break;
				}
			}
			
		}
	});
}

function obrisi(){
	var data2 = {};
	data2["korisnikBrisanje"] = document.getElementById("email").placeholder;
	var s = JSON.stringify(data2);
	$.ajax({
		url: "rest/izbrisiKorisnika",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			korisnikUloga = d.uloga;
			if(d.obrisan.localeCompare("true")==0){
				if(korisnikUloga.localeCompare("superadmin")==0)
					window.location.replace("/supAdminPocetna.html");
				else 
					window.location.replace("/adminPocetna.html");
			}
			else{
				alert("Ne mozes obrisati sam sebe");
			}
				
		}
	});
}