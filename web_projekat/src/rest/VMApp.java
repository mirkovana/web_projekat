package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.staticFiles;
import static spark.Spark.halt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import beans.Korisnik;
import beans.Organizacija;
import beans.Uloga;
import beans.VirtualnaMasina;
import beans.Disk;
import beans.Kategorija;
import json.GenerisanjeJSONa;
import spark.Session;
import rest.Aplikacija;

public class VMApp {
	
	private static Gson gson = new Gson();	
	
	private static GenerisanjeJSONa xml ;
	private static Aplikacija mape;
	

	public static void main(String[] args) throws Exception {
		
		//prvo mora da se napravi objekat klase GenerisanjeXMLa da bi se izgenerisali fajlovi na osnovu kreiranih objekata
		//u konstruktoru klase Aplikacija poziva se private f-ja koja ce ucitati izgenerisane fajlove
		xml = new GenerisanjeJSONa();
		mape = new Aplikacija();
		port(8080);
			
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		post("/rest/login", (req, res) -> {
			 res.type("application/json");
			 Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			 Session ss = req.session(true);
			 Korisnik korisnikSession = ss.attribute("user");
			 HashMap<String, String> returnMess = new HashMap<String, String>();
			 returnMess.put("message", "false");
			 returnMess.put("prazan", "");
			 String prazan = "";
		     if(korisnikSession == null) {
				 System.out.println("mail"+k.getEmail());
				 System.out.println("pass"+k.getLozinka());
				 if(k.getEmail().isEmpty()) {
					 prazan+="email";
					 System.out.println("prrrrrrrazno");
					 //neka ret mess ne znam da l true ili false
				 }
				 if(k.getLozinka().isEmpty()) {
					 prazan += "lozinka"; 
			     }
				 if(mape.getKorisnici().containsKey(k.getEmail())) {
			    	 System.out.println("else if");
					 Korisnik izMape = mape.getKorisnici().get(k.getEmail());
					 if(izMape.equals(k)) {
						korisnikSession = mape.getKorisnici().get(k.getEmail());
						ss.attribute("user", korisnikSession);
						returnMess.replace("message", "true");
						returnMess.put("uloga", izMape.getUloga().toString().toLowerCase());
						System.out.println(returnMess.toString());
					}
				 }
			 }
		     System.out.println(returnMess.get("prazan"));
			 returnMess.replace("prazan", prazan);
			 return gson.toJson(returnMess);
		});
		
		get("/rest/ucitaj", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik korisnikSession = ss.attribute("user");
			return gson.toJson(mape.izvuciVM(korisnikSession));
		});
		
		get("/rest/ucitajOrganizacije", (req, res) -> {
			res.type("application/json");	
			Session ss = req.session(true);
			Korisnik korisnikSession = ss.attribute("user");
			String s;
			System.out.println(korisnikSession.getUloga());
			if(korisnikSession.getUloga().equals(Uloga.SUPERADMIN))
				s = gson.toJson(mape.izvuciOrganizacije());
			else
				s = gson.toJson(mape.izvOrg(korisnikSession));
			return s;
		});
		
		get("/rest/ucitajKorisnike", (req, res) -> {
			res.type("application/json");	
			Session ss = req.session(true);
			Korisnik korisnikSession = ss.attribute("user");
			return gson.toJson(mape.izvuciKorisnike(korisnikSession));
		});
		
		get("/rest/ucitajKategorije", (req, res) -> {
			res.type("application/json");	
			return gson.toJson(mape.izvuciKategorije());
		});
		
		get("/rest/ucitajDiskove", (req, res) -> {
			res.type("application/json");	
			Session ss = req.session(true);
			Korisnik korisnikSession = ss.attribute("user");
			/*String s;
			if(korisnikSession.getUloga().equals(Uloga.SUPERADMIN))
				s = gson.toJson(mape.izvuciOrganizacije());
			else
				s = gson.toJson(mape.izvOrg(korisnikSession));
			return s;*/
			System.out.println(gson.toJson(mape.izvuciDiskove(korisnikSession)));
			return gson.toJson(mape.izvuciDiskove(korisnikSession));
		});
		
		get("/rest/ucitajDiskove22", (req, res) -> {
			res.type("application/json");	
			
			return gson.toJson(mape.getDiskovi().values());
		});
			
		post("/rest/filter", (req, res) -> {
			System.out.println("prosao1");
			System.out.println(req.body());
			 res.type("application/json");
			String[] params = req.body().split(":|\\,");
			 ArrayList<VirtualnaMasina> virtuelneMasine = new ArrayList<VirtualnaMasina>();
			 String ime = params[1].replaceAll("\"", "");
			 String brojJezgara = params[3].replaceAll("\"", "");
			 String ram = params[5].replaceAll("\"", "");
			 String gpu = params[7].replaceAll("\"", "");
			 String kat = params[9].replaceAll("\"|}", "");
			 if(!ime.equals(""))
			 {
				 System.out.println("ime");
				 for(VirtualnaMasina r : mape.getVmovi().values()) {
					if(r.getIme().equalsIgnoreCase(ime))
						virtuelneMasine.add(r);
				}
			 }
			 if(!brojJezgara.equals(""))
			 {
				 System.out.println("brojJezgara");
				 int i = Integer.parseInt(brojJezgara);
				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						if(r.getBrojJezgara()==i)
							virtuelneMasine.add(r);
					 }
				 else {
					 for(VirtualnaMasina r : virtuelneMasine) 
							if(r.getBrojJezgara()!=i)
								virtuelneMasine.remove(r);
				 }
			 }
			 if(!ram.equals(""))
			 {
				 System.out.println("ram");

				 int i = Integer.parseInt(ram);
				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						if(r.getRAM()==i)
							virtuelneMasine.add(r);
					 }
				 else {
					 for(VirtualnaMasina r : virtuelneMasine) 
							if(r.getRAM()!=i)
								virtuelneMasine.remove(r);
				 }
			 }
			 if(!gpu.equals(""))
			 {
				 System.out.println("gpu");

				 int i = Integer.parseInt(gpu);
				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						if(r.getGPU()==i)
							virtuelneMasine.add(r);
				 }
				 else {
					 for(VirtualnaMasina r : virtuelneMasine) 
							if(r.getGPU()!=i)
								virtuelneMasine.remove(r);
				 }
					
			 }
			 if(!kat.equals(""))
			 {
				 System.out.println("kategorija "+ kat);

				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						 if(r.getKategorija().equalsIgnoreCase(kat))
							virtuelneMasine.add(r);
					 }				
				 else {
					 for(VirtualnaMasina r : virtuelneMasine) 
							if(!r.getKategorija().equalsIgnoreCase(kat))
								virtuelneMasine.remove(r);
				 }
			 }
			 return gson.toJson(virtuelneMasine);
		});
		
		get("/rest/isLoggedIn", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			String loggedIn = "true";
			if (k == null)
				loggedIn = "false";
				
			return "{\"loggedIn\":" + loggedIn + "}";
		});
		
		get("/rest/logout", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			
			if (user != null) {
				ss.invalidate();
			}
			return "{\"loggedOut\": true}";
		});
		
		after("/rest/logout", (req, res) -> {
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			
			if (user == null) {
				res.redirect("/index.html", 301);
			}
		});
		
		before("/rest/addOrganizacija", (req, res) -> {
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			
			if (user == null) {
				halt(403, "<h2>403 <br>Unauthorized operation</h2>");
			}
		});
		
		post("/rest/addOrganizacija", (req, res) -> {
			res.type("application/json");
			Organizacija o = gson.fromJson(req.body(), Organizacija.class);
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			 returnMess.put("added", "false");
			 returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			System.out.println("Uloga korisnika : " +k.getUloga());
			if(k.getUloga().equals(Uloga.SUPERADMIN))
			{
				if(o.getIme()==null || o.getOpis()==null || o.getLogo()==null)
					{
						System.out.println(req.body());
						System.out.println(o.getIme());
						return gson.toJson(returnMess);		
					}
				if(!mape.getOrganizacije().containsKey(o.getIme())) {
					//k.addRacun(o.getIme());
					System.out.println("Nije nasao organizaciju");
					mape.getOrganizacije().put(o.getIme(), o);
					returnMess.replace("added", "true");				
				}
				return gson.toJson(returnMess);	
			}
			else
			{
				String orgIme="";
				Organizacija novaOrg=null;
				for(Organizacija org : mape.getOrganizacije().values())
				{
					if(org.getKorisnici().contains(k.getEmail()))
					{
						orgIme = org.getIme();
						if(o.getIme()!= null)org.setIme(o.getIme());
						if(o.getOpis()!= null)org.setOpis(o.getOpis());
						if(o.getLogo()!= null)org.setLogo(o.getLogo());
						novaOrg = org;
					}
					break;
				}
				mape.getOrganizacije().remove(orgIme);
				mape.getOrganizacije().put(novaOrg.getIme(), novaOrg);
				returnMess.replace("added", "true");
				return gson.toJson(returnMess);
			}
		});
		
		get("/rest/getUlogovan", (req, res) -> {
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			return gson.toJson(k);
		});
		
		post("/rest/izmeni", (req, res) -> {
			res.type("application/json");
			HashMap<String, String> izmene = gson.fromJson(req.body(), HashMap.class);
			Session ss = req.session(true);
			System.out.println(req.body());
			Korisnik k = ss.attribute("user");
			Korisnik stari = mape.getKorisnici().get(k.getEmail());
			Korisnik novi = new Korisnik(stari);
			String email = izmene.get("email"), ime = izmene.get("ime"), pre = izmene.get("prezime");
			String loz = izmene.get("pass"), loz2 = izmene.get("pass2");
			int ind=1; // 0 korisnik postoji; -1 lozinka nije uneta; -2 lozinke se ne poklapaju
			if(!email.equals(""))
			{
				if(mape.getKorisnici().containsKey(email))
					ind = 0;
				else
					novi.setEmail(email);
			}
			if(!ime.equals(""))
				novi.setIme(ime);
			if(!pre.equals(""))
				novi.setPrezime(pre);
			if(!loz.equals("") && !loz2.equals(""))
				if(loz.equals(loz2))
					novi.setLozinka(loz);
				else
					ind = -2;
			else if(!loz.equals("") && loz2.equals("")) ind = -1;
			if(ind == 1) {
				mape.updateKorisnik(stari,novi);
				ss.attribute("user", novi);
			}
			izmene.clear();
			izmene.put("uloga", k.getUloga().toString().toLowerCase());
			izmene.put("izmena", String.valueOf(ind));
			return gson.toJson(izmene);
		});
		
		post("/rest/filterDiskovi", (req, res) -> {
			System.out.println("prosao1");
			System.out.println(req.body());
			 res.type("application/json");
			 Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			ArrayList<Disk> ucitaniDiskovi;
			if(k.getUloga().equals(Uloga.KORISNIK))
				ucitaniDiskovi = mape.izvuciDiskove(k);
			else
				ucitaniDiskovi = new ArrayList<Disk>(mape.getDiskovi().values());
			String[] params = req.body().split(":|\\,");
			ArrayList<Disk> diskovii = new ArrayList<Disk>();
			String ime = params[1].replaceAll("\"", "");
			String kapacitet = params[3].replaceAll("\"", "");
			String vm = params[5].replaceAll("\"|}", "");
			 //vm = vm.replace("}", "");
		
			 if(!ime.equals(""))
			 {
				 System.out.println("ime");
				 for(Disk d : ucitaniDiskovi) {
					if(d.getIme().equalsIgnoreCase(ime))
						diskovii.add(d);
				}
			 }
			 if(!kapacitet.equals(""))
			 {
				 System.out.println("kapacitet");
				 double i = Double.parseDouble(kapacitet);
				 if(diskovii.isEmpty()){
					 for(Disk d : ucitaniDiskovi) 
						if(d.getKapacitet()==i)
							diskovii.add(d);
					 }
				 else {
					 for(Disk d : diskovii) 
						 if(d.getKapacitet() != i) {
							 diskovii.remove(d);
						 }
				 }
			 }
			 if(!vm.equals(""))
			 {
				 if(diskovii.isEmpty()){
					 System.out.println("diskovi u listi na ulazu " + diskovii.toString());
					 for(Disk d : ucitaniDiskovi) {
						 System.out.println("posle fora vm " + d.getVm());
							if(d.getVm().equalsIgnoreCase(vm)) { //mora to -1 jer vita vm1} ??
								diskovii.add(d);
							}
					  }
				 }else {
					 for(Disk d : diskovii) 
						 if(!d.getVm().equalsIgnoreCase(vm)) {
							 diskovii.remove(d);
						 }
				 }
			 }
			 System.out.println("diskovi: ");
			 for(Disk d : diskovii) {
				 System.out.println(d.toString());
			 }
			 return gson.toJson(diskovii);
		});
		
		before("/rest/addDisk", (req, res) -> {
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			
			if (user == null) {
				halt(403, "<h2>403 <br>Unauthorized operation</h2>");
			}
		});
		
		post("/rest/addDisk", (req, res) -> {
			res.type("application/json");
			Disk d = gson.fromJson(req.body(), Disk.class);
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			 returnMess.put("added", "false");
			 returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			System.out.println("Uloga korisnika : " +k.getUloga());
			if(k.getUloga().equals(Uloga.SUPERADMIN) || k.getUloga().equals(Uloga.ADMIN))
			{
				System.out.println("req.body pre ifa "+req.body());
				
				
				if(d.getIme()==null || d.getKapacitet()==0.0d || d.getVm()==null)
					{
						System.out.println("req.body iz add disk vma "+req.body());
						System.out.println("disk ime iz add disk vma "+d.getIme());
						System.out.println("disk kapacitet iz add disk vma "+d.getKapacitet());
						System.out.println("disk vm iz add disk vma "+d.getVm());
						return gson.toJson(returnMess);		
					}
				if(!mape.getDiskovi().containsKey(d.getIme())) {
					//k.addRacun(o.getIme());
					System.out.println("Nije nasao disk");
					mape.getDiskovi().put(d.getIme(), d);
					returnMess.replace("added", "true");				
				}
				return gson.toJson(returnMess);	
			}
			else
			{
				String disIme="";
				Disk noviDis=null;
				for(Disk dis : mape.getDiskovi().values())
				{
					disIme = dis.getIme();
					if(d.getIme()!= null)dis.setIme(d.getIme());
					if(d.getKapacitet()!= 0.0d)dis.setKapacitet(d.getKapacitet());
					if(d.getVm()!= null)dis.setVm(d.getVm());
					noviDis = dis;
					
					break;
				}
				mape.getDiskovi().remove(disIme);
				mape.getDiskovi().put(noviDis.getIme(), noviDis);
				returnMess.replace("added", "true");
				return gson.toJson(returnMess);
			}
		});
		
		before("/rest/obrisiDisk", (req, res) -> {
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			
			if (user == null) {
				halt(403, "<h2>403 <br>Unauthorized operation</h2>");
			}
		});
		
		post("/rest/obrisiDisk", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			String ime = req.queryParams("ime");
			String msg = "false";
			
			if(mape.getDiskovi().containsKey(ime)) {
				//k.deleteRacun(brRacuna);
				mape.getDiskovi().remove(ime);
				msg = "true";
			}
			
			return "{\"good\": " + msg + "}";
		});
		
		before("/rest/addKategorija", (req, res) -> {
			Session ss = req.session(true);
			Korisnik user = ss.attribute("user");
			
			if (user == null) {
				halt(403, "<h2>403 <br>Unauthorized operation</h2>");
			}
		});
		
		post("/rest/addKategorija", (req, res) -> {
			res.type("application/json");
			Kategorija d = gson.fromJson(req.body(), Kategorija.class);
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			 returnMess.put("added", "false");
			 returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			System.out.println("Uloga korisnika : " +k.getUloga());
			if(k.getUloga().equals(Uloga.SUPERADMIN))
			{
				System.out.println("req.body pre ifa "+req.body());
				
				
				if(d.getIme()==null || d.getBrojJezgara()==0 || d.getRAM()==0 || d.getGPU()==0)
					{
						System.out.println("req.body iz add disk vma "+req.body());
						return gson.toJson(returnMess);		
					}
				if(!mape.getKategorije().containsKey(d.getIme())) {
					//k.addRacun(o.getIme());
					System.out.println("Nije nasao disk");
					mape.getKategorije().put(d.getIme(), d);
					returnMess.replace("added", "true");				
				}
				return gson.toJson(returnMess);	
			}
			return gson.toJson(returnMess);	
		});
		
		get("/rest/getUlogovan", (req, res) -> {
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			return gson.toJson(k);
		});
	}
}
