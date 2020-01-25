package beans;

import java.util.ArrayList;
import java.util.List;

public class VirtualnaMasina {

	private String ime;
	private String kategorija;
	private int brojJezgara;
	private int RAM;
	private int GPU;
	private List<String> diskovi;
	private List<Aktivnost> aktivnosti;
	private boolean ukljucena;
	
	public VirtualnaMasina() {}	
	

	public VirtualnaMasina(VirtualnaMasina r) {
		super();
		this.ime = r.ime;
		this.kategorija = "";
		this.brojJezgara = r.brojJezgara;
		RAM = r.RAM;
		GPU = r.GPU;
		this.diskovi = r.diskovi;
		this.aktivnosti = r.aktivnosti;
		this.ukljucena = r.ukljucena;
	}


	public VirtualnaMasina(String ime, String kategorija, int brojJezgara, int rAM, int gPU) {
		super();
		this.ime = ime;
		this.kategorija = kategorija;
		this.brojJezgara = brojJezgara;
		RAM = rAM;
		GPU = gPU;
		this.diskovi = new ArrayList<String>();
		this.aktivnosti = new ArrayList<Aktivnost>();
		this.ukljucena = true;
	}


	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getKategorija() {
		return kategorija;
	}
	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}
	public List<String> getDiskovi() {
		return diskovi;
	}
	public void setDiskovi(List<String> diskovi) {
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
	public int getBrojJezgara() {
		return brojJezgara;
	}
	public void setBrojJezgara(int brojJezgara) {
		this.brojJezgara = brojJezgara;
	}
	public int getRAM() {
		return RAM;
	}
	public void setRAM(int rAM) {
		RAM = rAM;
	}
	public int getGPU() {
		return GPU;
	}
	public void setGPU(int gPU) {
		GPU = gPU;
	}
	
	public void dodajDisk(Disk d) {
		this.diskovi.add(d.getIme());
	}

	@Override
	public String toString() {
		return "VirtualnaMasina [ime=" + ime + ", kategorija=" + kategorija + ", brojJezgara=" + brojJezgara + ", RAM="
				+ RAM + ", GPU=" + GPU + ", ukljucena=" + ukljucena + "]";
	}
	
	
	
	
}
