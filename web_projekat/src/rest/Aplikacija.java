package rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	public HashMap<String, String> izvOrg(Korisnik korisnik)
	{
		HashMap<String, String> retMap = new HashMap<String, String>();
		for(Organizacija org : this.getOrganizacije().values())
		{
			if(org.getKorisnici()!=null)
				if(org.getKorisnici().contains(korisnik.getEmail()))
				{
					retMap.put("ime", org.getIme());
					retMap.put("opis", org.getOpis());
					break;
				}
		}
		return retMap;
	}
	
	public ArrayList<HashMap<String,String>> izvuciKorisnike(Korisnik kor) {
		ArrayList<HashMap<String,String>> virtuelne = new ArrayList<HashMap<String,String>>();		
		if(kor.getUloga().equals(Uloga.SUPERADMIN))
			for(Korisnik r : this.getKorisnici().values()) {
				HashMap<String, String> k = new HashMap<String, String>();
				k.put("email", r.getEmail());
				k.put("ime", r.getIme());
				k.put("prezime", r.getPrezime());
				String orgKor = "";
				for(Organizacija org:this.organizacije.values())
				{
					if(org.getKorisnici()!=null)
						if(org.getKorisnici().contains(r.getEmail()))
							orgKor += org.getIme()+" ";
				}
				k.put("organizacija", orgKor);
				virtuelne.add(k);
			}	
		else {
			List<String> orgKor = new ArrayList<String>();
			String org = "";
			for(Organizacija o : this.organizacije.values())
			{
				if(o.getKorisnici()!=null)
					if(o.getKorisnici().contains(kor.getEmail()))
					{
						org = o.getIme();
						orgKor = o.getKorisnici();
						break;
					}
			}
			for(Korisnik r : this.getKorisnici().values()) 
				if(orgKor.contains(r.getEmail())){
					HashMap<String, String> k = new HashMap<String, String>();
					k.put("email", r.getEmail());
					k.put("ime", r.getIme());
					k.put("prezime", r.getPrezime());
					k.put("organizacija", org);
					virtuelne.add(k);
				}
		}
		return virtuelne;
	}
	
	public ArrayList<VirtualnaMasina> izvuciVM(Korisnik k) {
		ArrayList<VirtualnaMasina> virtuelne = new ArrayList<VirtualnaMasina>();
		if(k.getUloga().equals(Uloga.SUPERADMIN))
			for(VirtualnaMasina r : this.getVmovi().values()) {
				VirtualnaMasina vm = new VirtualnaMasina(r);
				for(Organizacija org : this.getOrganizacije().values())
				{
					if(org.getKorisnici()!=null)
						if(org.getResursi().contains(r.getIme()))
							vm.setKategorija(vm.getKategorija()+org.getIme()+" ");
				}
				virtuelne.add(vm);
			}
		else {
			List<String> vmLista = new ArrayList<String>() ;
			String orgIme = "";
			for(Organizacija org : this.getOrganizacije().values())
				if(org.getKorisnici()!=null)
					if(org.getKorisnici().contains(k.getEmail())) {
						vmLista = org.getResursi();
						orgIme = org.getIme();
						break;
					}
			for(VirtualnaMasina r : this.getVmovi().values()) {
				VirtualnaMasina vm = new VirtualnaMasina(r);
				if(vmLista.contains(vm.getIme())){
					vm.setKategorija(vm.getKategorija()+orgIme);
					virtuelne.add(vm);
				}
			}
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
	
	public ArrayList<Disk> izvuciDiskove(Korisnik k) {		
		ArrayList<Disk> virtuelne = new ArrayList<Disk>();		
		
		if(k.getUloga().equals(Uloga.SUPERADMIN))
			for(Disk r : this.getDiskovi().values()) {
				virtuelne.add(r);
			}
		else {
			List<String> vmLista = new ArrayList<String>() ;
			String orgIme = "";
			for(Organizacija org : this.getOrganizacije().values())
				if(org.getKorisnici()!=null)
					if(org.getKorisnici().contains(k.getEmail())) {
						vmLista = org.getResursi();
						orgIme = org.getIme();
						break;
					}
			for(VirtualnaMasina r : this.getVmovi().values()) {
				if(vmLista.contains(r.getIme())){
					for(String d : r.getDiskovi())
					{
						virtuelne.add(this.getDiskovi().get(d));
					}
				}
			}
		}
		
		return virtuelne;
	}

	public void updateKorisnik(Korisnik stari, Korisnik novi) {
		this.korisnici.remove(stari.getEmail());
		this.korisnici.put(novi.getEmail(), novi);
		if(novi.getUloga().equals(Uloga.SUPERADMIN))
			return;
		for(Organizacija org : this.organizacije.values())
		{
			if(org.getKorisnici()!=null)
			{
				if(org.getKorisnici().contains(stari.getEmail()))
				{
					org.getKorisnici().remove(stari.getEmail());
					org.getKorisnici().add(novi.getEmail());
				}
			}
		}
	}
}
