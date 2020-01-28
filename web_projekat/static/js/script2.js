function getFormData($form) { //ucitava sa svake forme u bilo kom .html fajlu
	var unindexedArray = $form.serializeArray();
	var indexedArray = {}
	$.map(unindexedArray, function(n, i){
		indexedArray[n['name']] = n['value'];
    });

    return indexedArray;
}

function izmeni(){
	var data2 = getFormData($("#izmenaPodataka")); 
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
				//document.location.reload(false);
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
				//$("#log_war_pass").show();
				//document.location.reload(false);
			}
		} 
	});
}

function getUlogovan(){
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

function getOrganizacije()
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
				//x.appendChild(option);
				console.log(o.ime);			
				//var or = o.ime;
				//$("#orgSelect").append("<option value=\"${o.ime}\">$or</option>");
			}
		}
	});
}