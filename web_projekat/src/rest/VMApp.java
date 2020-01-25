package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import beans.Korisnik;
import beans.VirtualnaMasina;
import json.GenerisanjeJSONa;
import spark.Session;

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
			 if(korisnikSession == null) {
				 if(mape.getKorisnici().containsKey(k.getEmail())) {
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
			 return gson.toJson(returnMess);
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
	}
}
