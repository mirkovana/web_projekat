package beans;

public class Disk {

	private String ime;
	private enum tipDiska{SSD, HDD}
	private tipDiska tip;
	private double kapacitet;
	private VirtualnaMasina vm;
	
	public Disk() {}
	
	public Disk(String ime, tipDiska tip, double kapacitet, VirtualnaMasina vm) {
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

	public VirtualnaMasina getVm() {
		return vm;
	}

	public void setVm(VirtualnaMasina vm) {
		this.vm = vm;
	}
	
	
	
	
}
