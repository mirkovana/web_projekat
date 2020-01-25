package rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.Disk;
import beans.Kategorija;
import beans.Korisnik;
import beans.Organizacija;
import beans.Uloga;
import beans.VirtualnaMasina;



public class Aplikacija {
	private HashMap<String, Organizacija> organizacije;
	private HashMap<String, Korisnik> korisnici;
	private HashMap<String, VirtualnaMasina> vmovi;
	private HashMap<String, Kategorija> kategorije;
	private HashMap<String, Disk> diskovi;

	public Aplikacija() {
		ucitajListe(Organizacija.class, "organizacije.txt");
		ucitajListe(Korisnik.class, "korisnici.txt");
		ucitajListe(VirtualnaMasina.class, "vmasine.txt");
		ucitajListe(Kategorija.class, "kategorije.txt");
		ucitajListe(Disk.class, "diskovi.txt");
	}

	public HashMap<String, Kategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(HashMap<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}

	public HashMap<String, Disk> getDiskovi() {
		return diskovi;
	}

	public void setDiskovi(HashMap<String, Disk> diskovi) {
		this.diskovi = diskovi;
	}
	
	public HashMap<String, Organizacija> getOrganizacije() {
		return organizacije;
	}

	public void setOrganizacije(HashMap<String, Organizacija> organizacije) {
		this.organizacije = organizacije;
	}

	public HashMap<String, Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
	public HashMap<String, VirtualnaMasina> getVmovi() {
		return vmovi;
	}

	public void setVmovi(HashMap<String, VirtualnaMasina> vmovi) {
		this.vmovi = vmovi;
	}

	private void ucitajListe(Class<?> cls, String path) {
		Gson gson = new Gson();
		try {
	      File myObj = new File(path);
	      Scanner myReader = new Scanner(myObj);
	      String data = "";
	      while (myReader.hasNextLine())
	        data += myReader.nextLine();
	      if (cls == Organizacija.class)
	    	  organizacije = gson.fromJson(data, new TypeToken<HashMap<String, Organizacija>>(){}.getType()); 
	      if (cls == Disk.class)
	    	  diskovi = gson.fromJson(data, new TypeToken<HashMap<String, Disk>>(){}.getType()); 
	      if (cls == Kategorija.class)
	    	  kategorije = gson.fromJson(data, new TypeToken<HashMap<String, Kategorija>>(){}.getType()); 
	      if (cls == Korisnik.class)
	    	  korisnici = gson.fromJson(data, new TypeToken<HashMap<String, Korisnik>>(){}.getType()); 
	      if (cls == VirtualnaMasina.class)
	    	  vmovi = gson.fromJson(data, new TypeToken<HashMap<String, VirtualnaMasina>>(){}.getType()); 
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	public ArrayList<Organizacija> izvuciOrganizacije() {
		ArrayList<Organizacija> virtuelne = new ArrayList<Organizacija>();
		for(Organizacija r : this.getOrganizacije().values()) {
			virtuelne.add(r);
		}
		return virtuelne;
	}
	
	public ArrayList<HashMap<String,String>> izvuciKorisnike() {
		ArrayList<HashMap<String,String>> virtuelne = new ArrayList<HashMap<String,String>>();		
		for(Korisnik r : this.getKorisnici().values()) {
			HashMap<String, String> k = new HashMap<String, String>();
			k.put("email", r.getEmail());
			k.put("ime", r.getIme());
			k.put("prezime", r.getPrezime());
			String orgKor = "";
			for(Organizacija org:this.organizacije.values())
			{
				if(org.getKorisnici().contains(r.getEmail()))
					orgKor += org.getIme()+" ";
			}
			k.put("organizacija", orgKor);
			virtuelne.add(k);
		}		
		return virtuelne;
	}
	
	public ArrayList<VirtualnaMasina> izvuciVM() {
		ArrayList<VirtualnaMasina> virtuelne = new ArrayList<VirtualnaMasina>();
		
		for(VirtualnaMasina r : this.getVmovi().values()) {
			VirtualnaMasina vm = new VirtualnaMasina(r);
			for(Organizacija org : this.getOrganizacije().values())
			{
				if(org.getResursi().contains(r.getIme()))
					vm.setKategorija(vm.getKategorija()+org.getIme()+" ");
			}
			virtuelne.add(vm);
		}		
		return virtuelne;
	}
	
	
	public ArrayList<Kategorija> izvuciKategorije() {
		ArrayList<Kategorija> virtuelne = new ArrayList<Kategorija>();
		
		for(Kategorija r : this.getKategorije().values()) {
			virtuelne.add(r);
		}
		
		return virtuelne;
	}
	
	public ArrayList<Disk> izvuciDiskove() {		
		ArrayList<Disk> virtuelne = new ArrayList<Disk>();
		
		for(Disk r : this.getDiskovi().values()) {
			virtuelne.add(r);
		}
		
		return virtuelne;
	}
}
