package rest;

import java.util.HashMap;

import beans.Korisnik;
import beans.Organizacija;
import beans.Uloga;
import beans.VirtualnaMasina;



public class Aplikacija {
	private HashMap<String, Organizacija> organizacije;
	private HashMap<String, Korisnik> korisnici;
	private HashMap<String, VirtualnaMasina> vmovi;
	
	
	public Aplikacija(HashMap<String, Organizacija> organizacije, HashMap<String, Korisnik> korisnici,
			HashMap<String, VirtualnaMasina> vmovi) {
		super();
		this.organizacije = organizacije;
		this.korisnici = korisnici;
		this.vmovi = vmovi;
	}

	public Aplikacija() {
		this.organizacije = new HashMap<String,Organizacija>();
		this.korisnici = new HashMap<String,Korisnik>()
				{{
					put("peraperic@gmail.com",new Korisnik("peraperic@gmail.com","1234", "Pera", "Peric",Uloga.SUPERADMIN));
				}};
		this.vmovi =  new HashMap<String,VirtualnaMasina>()
		{{
			put("vm1",new VirtualnaMasina("vm1", "Kategorija1", 3, 4, 4, null));
			put("vm2",new VirtualnaMasina("vm2", "Kategorija3", 5, 7, 1, null));
		}};;
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
}
