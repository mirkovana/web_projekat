package beans;

import java.util.ArrayList;
import java.util.List;

public class Organizacija {

	private String ime;
	private String opis;
	private String logo;
	private List<String> korisnici;
	private List<String> resursi;
	
	public Organizacija() {}
	
	public Organizacija(String ime, String opis, String logo) {
		super();
		this.ime = ime;
		this.opis = opis;
		this.logo = logo;
		this.korisnici = new ArrayList<String>();
		this.resursi = new ArrayList<String>();
	}

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public List<String> getKorisnici() {
		return korisnici;
	}
	public void setKorisnici(List<String> korisnici) {
		this.korisnici = korisnici;
	}
	public List<String> getResursi() {
		return resursi;
	}
	public void setResursi(List<String> resursi) {
		this.resursi = resursi;
	}
	
	public void dodajKorisnika(Korisnik k) {
		this.korisnici.add(k.getEmail());
	}
	
	public void dodajVM(VirtualnaMasina vm) {
		this.resursi.add(vm.getIme());
	}

	@Override
	public String toString() {
		return "Organizacija [ime=" + ime + ", opis=" + opis + ", logo=" + logo + "]";
	}
}
