package beans;

import java.util.List;

public class VirtualnaMasina {

	private String ime;
	private Kategorija kategorija;
	private List<Disk> diskovi;
	private List<Aktivnost> aktivnosti;
	private boolean ukljucena;
	
	public VirtualnaMasina() {}
	
	public VirtualnaMasina(String ime, Kategorija kategorija, List<Disk> diskovi, List<Aktivnost> aktivnosti,
			boolean ukljucena) {
		super();
		this.ime = ime;
		this.kategorija = kategorija;
		this.diskovi = diskovi;
		this.aktivnosti = aktivnosti;
		this.ukljucena = ukljucena;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public List<Disk> getDiskovi() {
		return diskovi;
	}

	public void setDiskovi(List<Disk> diskovi) {
		this.diskovi = diskovi;
	}

	public List<Aktivnost> getAktivnosti() {
		return aktivnosti;
	}

	public void setAktivnosti(List<Aktivnost> aktivnosti) {
		this.aktivnosti = aktivnosti;
	}

	public boolean isUkljucena() {
		return ukljucena;
	}

	public void setUkljucena(boolean ukljucena) {
		this.ukljucena = ukljucena;
	}
	
	
	
	
}
