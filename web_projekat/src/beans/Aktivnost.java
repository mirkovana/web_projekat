package beans;

import java.util.Date;

public class Aktivnost {
	
	private Date paljenje;
	private Date gasenje;
	
	public Aktivnost() {}
	
	public Aktivnost(Date paljenje, Date gasenje) {
		this.paljenje = paljenje;
		this.gasenje = gasenje;
	}

	public Date getPaljenje() {
		return paljenje;
	}

	public void setPaljenje(Date paljenje) {
		this.paljenje = paljenje;
	}

	public Date getGasenje() {
		return gasenje;
	}

	public void setGasenje(Date gasenje) {
		this.gasenje = gasenje;
	}
	
	

}
