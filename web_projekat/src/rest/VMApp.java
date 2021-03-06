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
import beans.Disk.tipDiska;
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
			return gson.toJson(mape.izvuciDiskove(korisnikSession));
		});
				
		post("/rest/filter", (req, res) -> {
			System.out.println("prosao1");
			System.out.println(req.body());
			 res.type("application/json");
			String[] params = req.body().split(":|\\,");
			 ArrayList<VirtualnaMasina> virtuelneMasine = new ArrayList<VirtualnaMasina>();
			 //String ime = params[1].replaceAll("\"", "");
			 String brojJezgara = params[1].replaceAll("\"", "");
			 String ram = params[3].replaceAll("\"", "");
			 String gpu = params[5].replaceAll("\"|}", "");
			 //String kat = params[9].replaceAll("\"|}", "");
			 if(!brojJezgara.equals(""))
			 {
				 System.out.println("brojJezgara");
				 int manji = Integer.parseInt(brojJezgara.split("AND")[0]);
				 int veci = 0;
				 if(brojJezgara.split("AND").length==2)	
					 veci = Integer.parseInt(brojJezgara.split("AND")[1]);
				 else 
					 veci = manji;
				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						if(r.getBrojJezgara()>=manji && r.getBrojJezgara()<=veci)
							virtuelneMasine.add(r);
					 }
				 else {
					 for(VirtualnaMasina r : virtuelneMasine) 
							if(r.getBrojJezgara()>=manji && r.getBrojJezgara()<=veci)
								virtuelneMasine.remove(r);
				 }
			 }
			 if(!ram.equals(""))
			 {
				 System.out.println("ram");

				 int manji = Integer.parseInt(ram.split("AND")[0]);
				 int veci = 0;
				 if(ram.split("AND").length==2)	
					 veci = Integer.parseInt(ram.split("AND")[1]);
				 else 
					 veci = manji;
				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						if(r.getRAM()>=manji && r.getRAM()<=veci)
							virtuelneMasine.add(r);
					 }
				 else {
					 int n = virtuelneMasine.size();
					 for(int i = 0; i<n; i++)
					 if(virtuelneMasine.get(i).getRAM()<manji || virtuelneMasine.get(i).getRAM()>veci)
						{
							virtuelneMasine.remove(i);
							i--;
							n--;
						}
				 }
			 }
			 if(!gpu.equals(""))
			 {
				 System.out.println("gpu");
				 int manji = Integer.parseInt(gpu.split("AND")[0]);
				 int veci = 0;
				 if(gpu.split("AND").length==2)	
					 veci = Integer.parseInt(gpu.split("AND")[1]);
				 else 
					 veci = manji;
				 if(virtuelneMasine.isEmpty()){
					 for(VirtualnaMasina r : mape.getVmovi().values()) 
						if(r.getGPU()>=manji && r.getGPU()<=veci)
							virtuelneMasine.add(r);
				 }
				 else {
					 int n = virtuelneMasine.size();
					 for(int i = 0; i<n; i++)
					 if(virtuelneMasine.get(i).getGPU()<manji || virtuelneMasine.get(i).getGPU()>veci)
						{
							virtuelneMasine.remove(i);
							i--;
							n--;
						}
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
				upisiOrganizacije();
				return gson.toJson(returnMess);
			}
		});
		
		get("/rest/getUlogovan", (req, res) -> {
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			return gson.toJson(k);
		});
		
		post("/rest/izmeni", (req, res) -> { //izmena korisnika
			res.type("application/json");
			HashMap<String, String> izmene = gson.fromJson(req.body(), HashMap.class);
			Session ss = req.session(true);
			System.out.println(req.body());
			Korisnik k = ss.attribute("user");
			Korisnik stari = null;
			//provera da li korisnik menja sam sebe ili nekog drugog
			boolean samSebe = true;
			if(izmene.get("stariKorisnik").equals(k.getEmail())) 
				stari = mape.getKorisnici().get(k.getEmail());
			else
			{
				stari = mape.getKorisnici().get(izmene.get("stariKorisnik"));
				samSebe = false;
			}
			Korisnik novi = new Korisnik(stari);
			String email = izmene.get("email"), ime = izmene.get("ime"), pre = izmene.get("prezime");
			String loz = izmene.get("pass"), loz2 = izmene.get("pass2");
			int ind=1; // 0 korisnik postoji; -1 lozinka nije uneta; -2 lozinke se ne poklapaju
			if(email!=null) {
				if(!email.equals(""))
				{
					if(mape.getKorisnici().containsKey(email))
						ind = 0;
					else
						novi.setEmail(email);
				}
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
			if(ind == 1) mape.updateKorisnik(stari,novi);
			if(samSebe) ss.attribute("user", novi);
			izmene.clear();
			izmene.put("uloga", k.getUloga().toString().toLowerCase());
			izmene.put("izmena", String.valueOf(ind));
			upisiKorisnike();
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
			else if(k.getUloga().equals(Uloga.ADMIN))
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
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			String[] params = req.body().split(":|\\,");
			returnMess.put("added", "false");
			returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			for(int i = 0; i < params.length ; i++) 
				params[i] = params[i].replaceAll("\"|}", "");
			for(int i = 0; i < params.length ; i++) {
				if(params[i].equals("")){
					returnMess.replace("added", "prazno");
					return gson.toJson(returnMess);
				}
			}
			Disk d = new Disk();
			d.setIme(params[1]);
			d.setKapacitet(Double.parseDouble(params[3]));
			d.setVm(params[5]);
			mape.getDiskovi().put(d.getIme(), d);
			upisiDiskove();
			returnMess.replace("added", "true");
			return gson.toJson(returnMess);
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
			String ime = req.body().split(":")[1].replaceAll("\"|}", "");
			String msg = "false";
			
			if(mape.getDiskovi().containsKey(ime)) {
				//k.deleteRacun(brRacuna);
				mape.getDiskovi().remove(ime);
				msg = "true";
				upisiDiskove();
			}
			
			return "{\"good\": " + msg + "}";
		});
		
		post("/rest/obrisiKat", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			String ime = req.body().split(":")[1].replaceAll("\"|}", "");
			String msg = "false";
			
			if(mape.getKategorije().containsKey(ime)) {
				//k.deleteRacun(brRacuna);
				mape.getKategorije().remove(ime);
				msg = "true";
				upisiKategorije();
			}
			
			return "{\"good\": " + msg + "}";
		});
		
		post("/rest/izmenaDiska", (req, res) -> {
			res.type("application/json");
			HashMap<String, String> izmene = gson.fromJson(req.body(), HashMap.class);
			Session ss = req.session(true);
			System.out.println(req.body());
			Korisnik k = ss.attribute("user");
			String pos = izmene.get("stariDisk");
			Disk kojaSeMenja = mape.getDiskovi().get(pos);
			String ime = izmene.get("ime"), tip = izmene.get("tip"), kapacitet = izmene.get("kapacitet");
			String vm = izmene.get("vm");
			int ind=1; // 0 disk postoji; 1 sve okej
			if(!ime.equals(""))
			{
				if(mape.getDiskovi().containsKey(ime))
					ind = 0;
				else
					kojaSeMenja.setIme(ime);
			}
			if(!tip.equalsIgnoreCase(""))
				if(!tip.equalsIgnoreCase(kojaSeMenja.getTip().toString()))
				{
					kojaSeMenja.setTip(tip.equalsIgnoreCase("SSD")?tipDiska.SSD:tipDiska.HDD);
				}
			if(!kapacitet.equalsIgnoreCase(""))
			{
				if(Double.parseDouble(kapacitet)!=kojaSeMenja.getKapacitet())
					kojaSeMenja.setKapacitet(Double.parseDouble(kapacitet));
			}
			if(!vm.equalsIgnoreCase(""))
				if(!vm.equalsIgnoreCase(kojaSeMenja.getVm()))
					kojaSeMenja.setVm(vm);
			
			//if(ulogaStr != null) kojaSeMenja.setUkljucena(uloga);
			mape.dodajIzmenjenDisk(pos, kojaSeMenja);
			izmene.clear();
			izmene.put("uloga", k.getUloga().toString().toLowerCase());
			izmene.put("izmena", String.valueOf(ind));
			
			
			upisiDiskove();
			upisiVirtuelne();
			return gson.toJson(izmene);
		});
		
		post("/rest/izmenaKat", (req, res) -> {
			res.type("application/json");
			HashMap<String, String> izmene = gson.fromJson(req.body(), HashMap.class);
			Session ss = req.session(true);
			System.out.println(req.body());
			Korisnik k = ss.attribute("user");
			String pos = izmene.get("staraKat");
			Kategorija kojaSeMenja = mape.getKategorije().get(pos);
			String ime = izmene.get("ime"), brojJezgara = izmene.get("brojJezgara"), RAM = izmene.get("RAM");
			String GPU = izmene.get("GPU");
			int ind=1; // 0 disk postoji; 1 sve okej
			if(!ime.equals(""))
			{
				if(mape.getKategorije().containsKey(ime))
					ind = 0;
				else
					kojaSeMenja.setIme(ime);
			}
			if(!brojJezgara.equalsIgnoreCase(""))
			{
				if(Integer.parseInt(brojJezgara)!=kojaSeMenja.getBrojJezgara())
					kojaSeMenja.setBrojJezgara(Integer.parseInt(brojJezgara));
			}
			if(!RAM.equalsIgnoreCase(""))
			{
				if(Integer.parseInt(RAM)!=kojaSeMenja.getRAM())
					kojaSeMenja.setRAM(Integer.parseInt(RAM));
			}
			if(!GPU.equalsIgnoreCase(""))
			{
				if(Integer.parseInt(GPU)!=kojaSeMenja.getGPU())
					kojaSeMenja.setGPU(Integer.parseInt(GPU));
			}
			//if(ulogaStr != null) kojaSeMenja.setUkljucena(uloga);
			mape.dodajKategoriju(pos, kojaSeMenja);
			izmene.clear();
			izmene.put("uloga", k.getUloga().toString().toLowerCase());
			izmene.put("izmena", String.valueOf(ind));
			
			
			upisiDiskove();
			upisiVirtuelne();
			return gson.toJson(izmene);
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
					upisiKategorije();
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
		
		post("/rest/dodajKorisnika", (req, res) -> {
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			returnMess.put("added", "imaKorisnik");
			returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			String[] params = req.body().split(":|\\,");
			for(int i = 0; i < params.length ; i++) 
				params[i] = params[i].replaceAll("\"|}", "");
			for(int i = 0; i < params.length ; i++) {
				if(params[i].equals("")){
					returnMess.replace("added", "prazno");
					return gson.toJson(returnMess);
				}
			}
			Korisnik korisnikForma = new Korisnik();
			if(mape.getKorisnici().containsKey(params[5]))
				return gson.toJson(returnMess);
			korisnikForma.setIme(params[1]);
			korisnikForma.setPrezime(params[3]);
			korisnikForma.setEmail(params[5]);
			korisnikForma.setLozinka(params[7]);
			if(params.length==12)
			{
				if(params[11].equalsIgnoreCase("admin"))
					korisnikForma.setUloga(Uloga.ADMIN);
				else korisnikForma.setUloga(Uloga.KORISNIK);
				korisnikForma.setOrganizacija(params[9]);							
			}
			else
			{
				if(params[9].equalsIgnoreCase("admin"))
					korisnikForma.setUloga(Uloga.ADMIN);
				else korisnikForma.setUloga(Uloga.KORISNIK);
				korisnikForma.setOrganizacija(k.getOrganizacija());
			}
			mape.dodajKorisnikaUorganizaciju(korisnikForma);	
			mape.dodajKorisnika(korisnikForma);
			returnMess.replace("added", "true");
			upisiKorisnike();
			return gson.toJson(returnMess);
		});
		
		post("/rest/izbrisiKorisnika", (req, res) -> {
			String izbrisan = "false";
			String param = req.body().split(":|\\,")[1].replaceAll("\"|}", "");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			returnMess.put("obrisan", "false");
			returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			if(k.getEmail().equalsIgnoreCase(param))
				return gson.toJson(returnMess);
			mape.izbrisiKorisnika(param);
			returnMess.replace("obrisan", "true");
			System.out.println("PARAMETAR BRISANJE " + param);
			upisiKorisnike();
			return gson.toJson(returnMess);
		});
		
		post("/rest/izmeniVM", (req,res)->{
			res.type("application/json");
			HashMap<String, String> izmene = gson.fromJson(req.body(), HashMap.class);
			Session ss = req.session(true);
			System.out.println(req.body());
			Korisnik k = ss.attribute("user");
			String pos = izmene.get("staraVM");
			VirtualnaMasina kojaSeMenja = mape.getVmovi().get(pos);
			String ime = izmene.get("ime"), kategorija = izmene.get("kategrija");
			String ulogaStr = izmene.get("uloga");
			boolean uloga = false;
			if(ulogaStr != null)  uloga = Boolean.parseBoolean(ulogaStr);
			int ind=1; // 0 vm postoji; 1 sve okej
			if(!ime.equals(""))
			{
				if(mape.getVmovi().containsKey(ime))
					ind = 0;
				else
					kojaSeMenja.setIme(ime);
			}
			if(!kategorija.equalsIgnoreCase(kojaSeMenja.getKategorija()))
			{
				Kategorija kat = mape.getKategorije().get(kategorija);
				kojaSeMenja.setKategorija(kategorija);
				kojaSeMenja.setBrojJezgara(kat.getBrojJezgara());
				kojaSeMenja.setRAM(kat.getRAM());
				kojaSeMenja.setGPU(kat.getGPU());
			}
			if(ulogaStr != null) kojaSeMenja.setUkljucena(uloga);
			mape.dodajIzmenjenuVM(pos, kojaSeMenja);
			izmene.clear();
			izmene.put("uloga", k.getUloga().toString().toLowerCase());
			izmene.put("izmena", String.valueOf(ind));
			upisiVirtuelne();
			return gson.toJson(izmene);
		});	
	
		post("/rest/izbrisiVM", (req, res) -> {
			String izbrisan = "false";
			String param = req.body().split(":|\\,")[1].replaceAll("\"|}", "");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			returnMess.put("obrisan", "false");
			returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			boolean uspelo = mape.izbrisiVirtuelnu(param);
			returnMess.replace("obrisan", String.valueOf(uspelo));
			System.out.println("PARAMETAR BRISANJE " + param);
			upisiVirtuelne();
			return gson.toJson(returnMess);
		});
		
		post("/rest/dodajVM", (req, res) -> {
			Session ss = req.session(true);
			Korisnik k = ss.attribute("user");
			HashMap<String, String> returnMess = new HashMap<String, String>();
			returnMess.put("added", "imaVM");
			returnMess.put("uloga", k.getUloga().toString().toLowerCase());
			String[] params = req.body().split(":|\\,");
			System.out.println(req.body());
			for(int i = 0; i < params.length ; i++) 
			{
				params[i] = params[i].replaceAll("\"|}", "");
			}
			if(params.length<8)
			{
				returnMess.replace("added", "prazno");
				return gson.toJson(returnMess);
			}
			if(!params[0].equalsIgnoreCase("{name") || !params[2].equalsIgnoreCase("organizacija") || !params[4].equalsIgnoreCase("kategorija"))
			{
				returnMess.replace("added", "prazno");
				return gson.toJson(returnMess);
			}
			String ime = params[1], organizacija = params[3], kategorija = params[5];
			ArrayList<String> diskovi = new ArrayList<String>();
			for(int i = 7; i<params.length; i+=2)
				diskovi.add(params[i]);
			VirtualnaMasina vm = new VirtualnaMasina();
			vm.setIme(ime);
			vm.setDiskovi(diskovi);
			vm.setKategorija(kategorija);
			if(mape.dodajVM(vm,organizacija)) returnMess.replace("added", "true");
			upisiVirtuelne();
			return gson.toJson(returnMess);
		});
	}


	private static void upisiOrganizacije() {
		// TODO Auto-generated method stub
		xml.kreirajIUpisiUFajl("organizacije.txt", gson.toJson(mape.getOrganizacije()));
	}


	private static void upisiDiskove() {
		// TODO Auto-generated method stub
		xml.kreirajIUpisiUFajl("diskovi.txt", gson.toJson(mape.getDiskovi()));
	}


	private static void upisiKategorije() {
		// TODO Auto-generated method stub
		xml.kreirajIUpisiUFajl("kategorije.txt", gson.toJson(mape.getKategorije()));
	}


	private static void upisiKorisnike() {
		// TODO Auto-generated method stub
		xml.kreirajIUpisiUFajl("korisnici.txt", gson.toJson(mape.getKorisnici()));
	}


	private static void upisiVirtuelne() {
		// TODO Auto-generated method stub
		xml.kreirajIUpisiUFajl("vmasine.txt", gson.toJson(mape.getVmovi()));
	}
}
