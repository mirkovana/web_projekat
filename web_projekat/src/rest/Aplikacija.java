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
	
	public ArrayList<Korisnik> izvuciKorisnike(Korisnik kor) {
		ArrayList<Korisnik> korisnici = new ArrayList<Korisnik>();		
		if(kor.getUloga().equals(Uloga.SUPERADMIN))
			for(Korisnik k : this.korisnici.values()) {
				if(k.getOrganizacija()==null)
					k.setOrganizacija("");
				korisnici.add(k);
			}	
		else {
			for(Korisnik k : this.korisnici.values())
				if(k.getOrganizacija()!=null)
					if(k.getOrganizacija().equalsIgnoreCase(kor.getOrganizacija()))
						korisnici.add(k);
		}
		return korisnici;
	}
	
	public ArrayList<HashMap<String, String>> izvuciVM(Korisnik k) {
		ArrayList<HashMap<String, String>> virtuelne = new ArrayList<HashMap<String, String>>();
		if(k.getUloga().equals(Uloga.SUPERADMIN))
			for(VirtualnaMasina r : this.getVmovi().values()) {
				HashMap<String, String> vm  = kreirajHashMapu(r);
				for(Organizacija org : this.getOrganizacije().values())
				{
					if(org.getKorisnici()!=null)
						if(org.getResursi().contains(r.getIme()))
							vm.put("organizacija", org.getIme());
				}
				if( vm!=null )virtuelne.add(vm);
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
				HashMap<String, String> vm = kreirajHashMapu(r);
				if(vmLista.contains(r.getIme())){
					vm.put("organizacija", orgIme);
					virtuelne.add(vm);
				}
			}
		}
		return virtuelne;
	}
	
	
	private HashMap<String, String> kreirajHashMapu(VirtualnaMasina r) {
		HashMap<String, String> mapa = new HashMap<String, String>();
		mapa.put("ime", r.getIme());
		mapa.put("kategorija", r.getKategorija());
		mapa.put("brojJezgara", String.valueOf(r.getBrojJezgara()));
		mapa.put("RAM", String.valueOf(r.getRAM()));
		mapa.put("GPU", String.valueOf(r.getGPU()));
		mapa.put("diskovi", String.valueOf(r.getDiskovi()));
		mapa.put("aktivnost", r.getAktivnosti().toString());
		mapa.put("upaljena", r.isUkljucena()?"true":"false");
		return mapa;
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

	public void dodajKorisnika(Korisnik korisnikForma) {
		// TODO Auto-generated method stub
		this.korisnici.put(korisnikForma.getEmail(), korisnikForma);
	}

	public void dodajKorisnikaUorganizaciju(Korisnik korisnikForma) {
		// TODO Auto-generated method stub
		this.organizacije.get(korisnikForma.getOrganizacija()).dodajKorisnika(korisnikForma);
	}

	public void izbrisiKorisnika(String param) {
		// TODO Auto-generated method stub
		this.korisnici.remove(param);
	}

	public void dodajIzmenjenuVM(String pos, VirtualnaMasina kojaSeMenja) {
		// TODO Auto-generated method stub
		this.vmovi.remove(pos);
		this.vmovi.put(kojaSeMenja.getIme(), kojaSeMenja);
		for(Organizacija org : this.organizacije.values())
		{
			if(org.getResursi()!=null)
			{
				if(org.getResursi().contains(pos))
				{
					org.getResursi().remove(pos);
					org.getResursi().add(kojaSeMenja.getIme());
				}
			}
		}
		for(Disk d : this.diskovi.values())
		{
			if(d.getVm()!=null)
			{
				if(d.getVm().equals(pos))
				{
					d.setVm(kojaSeMenja.getIme());
				}
			}
		}
	}

	public boolean izbrisiVirtuelnu(String param) {
		if(this.vmovi.containsKey(param))
		{
			this.vmovi.remove(param);
			for(Organizacija o : this.organizacije.values())
				if(o.getResursi().contains(param))
					o.getResursi().remove(param);
			for(Disk o : this.diskovi.values())
				if(o.getVm().equalsIgnoreCase(param))
					o.setVm("");
			return true;
		}
		return false;
	}

	public boolean dodajVM(VirtualnaMasina vm, String organizacija) {
		// TODO Auto-generated method stub
		if(!this.vmovi.containsKey(vm.getIme()))
		{
			Kategorija k = this.kategorije.get(vm.getKategorija());
			vm.setGPU(k.getGPU());
			vm.setRAM(k.getRAM());
			vm.setBrojJezgara(k.getBrojJezgara());
			this.vmovi.put(vm.getIme(), vm);
			this.organizacije.get(organizacija).dodajVM(vm);
			for(String d : vm.getDiskovi())
			{
				this.diskovi.get(d).setVm(vm.getIme());
			}
			return true;
		}
		return false;
	}

	public void dodajIzmenjenDisk(String pos, Disk kojaSeMenja) {
		// TODO Auto-generated method stub
		this.diskovi.remove(pos);
		this.diskovi.put(kojaSeMenja.getIme(), kojaSeMenja);
		for(VirtualnaMasina vm:this.vmovi.values()) {
			if(vm.getDiskovi().contains(pos)) {
				vm.getDiskovi().remove(pos);
				vm.dodajDisk(kojaSeMenja);
			}
		}
	}

	public void dodajKategoriju(String pos, Kategorija kojaSeMenja) {
		// TODO Auto-generated method stub
		this.kategorije.remove(pos);
		this.kategorije.put(kojaSeMenja.getIme(), kojaSeMenja);
		for(VirtualnaMasina vm : this.vmovi.values()) {
			if(vm.getKategorija().equalsIgnoreCase(pos)) {
				vm.setKategorija(kojaSeMenja.getIme());
				vm.setRAM(kojaSeMenja.getRAM());
				vm.setGPU(kojaSeMenja.getGPU());
				vm.setBrojJezgara(kojaSeMenja.getGPU());
			}
		}
	}
}
