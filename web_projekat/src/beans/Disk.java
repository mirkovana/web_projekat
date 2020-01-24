package beans;

import javax.swing.Spring;

public class Disk {

	private String ime;
	public enum tipDiska{SSD, HDD}
	private tipDiska tip;
	private double kapacitet;
	private String vm;
	
	public Disk() {}
	
	public Disk(String ime, tipDiska tip, double kapacitet, String vm) {
		super();
		this.ime = ime;
		this.tip = tip;
		this.kapacitet = kapacitet;
		this.vm = vm;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public tipDiska getTip() {
		return tip;
	}

	public void setTip(tipDiska tip) {
		this.tip = tip;
	}

	public double getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(double kapacitet) {
		this.kapacitet = kapacitet;
	}

	public String getVm() {
		return vm;
	}

	public void setVm(String vm) {
		this.vm = vm;
	}
	
	
	
	
}
