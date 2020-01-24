package rest;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public Aplikacija(HashMap<String, Organizacija> organizacije, HashMap<String, Korisnik> korisnici,
			HashMap<String, VirtualnaMasina> vmovi) {
		super();
		this.organizacije = organizacije;
		this.korisnici = korisnici;
		this.vmovi = vmovi;
		
	}

	public Aplikacija() {
		this.organizacije = new HashMap<String,Organizacija>();
		this.korisnici = new HashMap<String,Korisnik>();
		this.vmovi =  new HashMap<String,VirtualnaMasina>();
		this.diskovi = new HashMap<String,Disk>();
		this.kategorije = new HashMap<String,Kategorija>();
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

	public void addKorisnik(Korisnik k)
	{
		this.korisnici.put(k.getEmail(), k);
	}
	
	public void addOrganizacija(Organizacija o) {
		this.organizacije.put(o.getIme(), o);
	}
	
	public void addVM(VirtualnaMasina v) {
		this.vmovi.put(v.getIme(), v);
	}
	
	public void addKategorija(Kategorija k) {
		this.kategorije.put(k.getIme(), k);
	}
	
	public void addDisk(Disk d) {
		this.diskovi.put(d.getIme(), d);
	}
	
	public ArrayList<Organizacija> izvuciOrganizacije() {
		ArrayList<Organizacija> virtuelne = new ArrayList<Organizacija>();
		for(Organizacija r : this.getOrganizacije().values()) {
			virtuelne.add(r);
		}
		return virtuelne;
	}
	
	public ArrayList<Korisnik> izvuciKorisnike() {
		ArrayList<Korisnik> virtuelne = new ArrayList<Korisnik>();		
		for(Korisnik r : this.getKorisnici().values()) {
			virtuelne.add(r);
		}		
		return virtuelne;
	}
	
	public ArrayList<VirtualnaMasina> izvuciVM() {
		ArrayList<VirtualnaMasina> virtuelne = new ArrayList<VirtualnaMasina>();
		
		for(VirtualnaMasina r : this.getVmovi().values()) {
			virtuelne.add(r);
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
