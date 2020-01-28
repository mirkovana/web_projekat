package beans;

public class Korisnik {

	private String email;
	private String lozinka;
	private String ime;
	private String prezime;
	private String organizacija;
	private Uloga uloga;
	
	public Korisnik() {}
	

	public Korisnik(String email, String lozinka, String ime, String prezime, String organizacija, Uloga uloga) {
		super();
		this.email = email;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.organizacija = organizacija;
		this.uloga = uloga;
	}
	
	public Korisnik(Korisnik k) {
		this.email = k.email;
		this.lozinka = k.lozinka;
		this.ime = k.ime;
		this.prezime = k.prezime;
		this.organizacija = k.organizacija;
		this.uloga = k.uloga;
	}

	public Korisnik(String email, String lozinka, String ime, String prezime, Uloga uloga) {
		this.email = email;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.organizacija = null;
		this.uloga = uloga;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	
	
	public String getOrganizacija() {
		return organizacija;
	}

	public void setOrganizacija(String organizacija) {
		this.organizacija = organizacija;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Korisnik) {
			Korisnik k = (Korisnik)o;
			
			return k.getEmail().equals(email)
					&& k.getLozinka().equals(lozinka);
		}
		
		return false;
	}
	
	/*public boolean hasOrganizacija(String ime) {
		return Aplikacija.cl.contains(ime);
	}
	
	public void addRacun(String ime) {
		racuni.add(ime);
	}*/
	
}
