package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.Disk;
import beans.Disk.tipDiska;
import beans.Kategorija;
import beans.Korisnik;
import beans.Organizacija;
import beans.Uloga;
import beans.VirtualnaMasina;
import spark.Session;

public class VMApp {
	
	private static Gson gson = new Gson();
	
	private static Aplikacija mape = new Aplikacija();
	
	static Kategorija cat1 = new Kategorija("Kategorija1", 4, 4, 2);
	static Kategorija cat2 = new Kategorija("Kategorija2", 8, 4, 2);
	static Kategorija cat3 = new Kategorija("Kategorija3", 8, 8, 4);
	private ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>() {{
		add(cat1);
		add(cat2);
		add(cat3);
	}};
	private static Disk disk1 = new Disk("Disk1", tipDiska.SSD, 500, "vm1");
	private static Disk disk2 = new Disk("Disk2", tipDiska.SSD, 250, "vm1");
	private static Disk disk3 = new Disk("Disk3", tipDiska.HDD, 1000, "vm2");
	
	private static ArrayList<String> diskovi1 = new ArrayList<String>() {{
		add("Disk1");
		add("Disk2");
	}};
	private static ArrayList<String> diskovi2 = new ArrayList<String>() {{
		add("Disk3");
	}};
	
	static VirtualnaMasina vm1 = new VirtualnaMasina("vm1", "Kategorija1", cat1.getBrojJezgara(), cat1.getRAM(), cat1.getGPU(),
			diskovi1);
	static VirtualnaMasina vm2 = new VirtualnaMasina("vm2", "Kategorija2", cat2.getBrojJezgara(), cat2.getRAM(), cat2.getGPU(),
			diskovi1);
	static VirtualnaMasina vm3 = new VirtualnaMasina("vm3", "Kategorija3", cat3.getBrojJezgara(), cat3.getRAM(), cat3.getGPU(),
			diskovi2);
	private static ArrayList<String> vme = new ArrayList<String>() {{
		add("vm1");
		add("vm2");
		add("vm3");
	}};
	
	
	private static Korisnik Korisnik1 = new Korisnik("peraperic@gmail.com","1234", "Pera", "Peric",Uloga.SUPERADMIN);
	private static Korisnik Korisnik2 = new Korisnik("anaanic@gmail.com","aaa","Ana", "Anic","Org2" , Uloga.ADMIN);
	private static Korisnik Korisnik3 = new Korisnik("markomarkovic@gmail.com","bbb","Marko", "Markovic","Org2" , Uloga.KORISNIK);
	
	private static ArrayList<String> korisnici1 = new ArrayList<String>() {{
		add("peraperic@gmail.com");
	}};
	
	private static ArrayList<String> korisnici2 = new ArrayList<String>() {{
		add("anaanic@gmail.com");
		add("markomarkovic@gmail.com");
	}};
		
	private static Organizacija org1 = new Organizacija("Org1", "Opis1", "", korisnici1, vme);
	private static Organizacija org2 = new Organizacija("Org2", "Opis2", "", korisnici2, vme);

	public static void main(String[] args) throws Exception {
		
		port(8080);
		
		mape.addOrganizacija(org1);
		mape.addOrganizacija(org2);
		mape.addKorisnik(Korisnik1);
		mape.addKorisnik(Korisnik2);
		mape.addKorisnik(Korisnik3);
		mape.addVM(vm1);
		mape.addVM(vm2);
		mape.addVM(vm3);
		mape.addKategorija(cat1);
		mape.addKategorija(cat2);
		mape.addKategorija(cat3);
		mape.addDisk(disk1);
		mape.addDisk(disk2);
		mape.addDisk(disk3);
		
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());		
		
		post("/rest/login", (req, res) -> {
			 res.type("application/json");
			 Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			 Session ss = req.session(true);
			 Korisnik korisnikSession = ss.attribute("user");
			 String message = "false";
			 if(korisnikSession == null) {
				 if(mape.getKorisnici().containsKey(k.getEmail())) {
					 if(mape.getKorisnici().get(k.getEmail()).equals(k)) {
						korisnikSession = mape.getKorisnici().get(k.getEmail());
						ss.attribute("user", korisnikSession);
						message = "true";
					}
				 }
			 }
			 
			 return "{\"message\": " + message + "}";
		});
		
		get("/rest/ucitaj", (req, res) -> {
			res.type("application/json");	
			return gson.toJson(mape.izvuciVM());
		});
		
		get("/rest/ucitajOrganizacije", (req, res) -> {
			System.out.println("Ucitao");
			res.type("application/json");	
			String s = gson.toJson(mape.izvuciOrganizacije());
			System.out.println(s);
			return s;
		});
		
		get("/rest/ucitajKorisnike", (req, res) -> {
			res.type("application/json");	
			return gson.toJson(mape.izvuciKorisnike());
		});
		
		get("/rest/ucitajKategorije", (req, res) -> {
			res.type("application/json");	
			return gson.toJson(mape.izvuciKategorije());
		});
		
		get("/rest/ucitajDiskove", (req, res) -> {
			res.type("application/json");	
			return gson.toJson(mape.izvuciDiskove());
		});
			
		post("/rest/filter", (req, res) -> {
			System.out.println("prosao1");
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
	}
}
