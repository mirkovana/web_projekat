package json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import beans.Disk;
import beans.Disk.tipDiska;
import beans.Kategorija;
import beans.Korisnik;
import beans.Organizacija;
import beans.Uloga;
import beans.VirtualnaMasina;

public class GenerisanjeJSONa {
	
	public GenerisanjeJSONa(){
		generisi();
	}

	private void generisi() {
		// TODO Auto-generated method stub
		Kategorija cat1 = new Kategorija("Kategorija1", 4, 4, 2);
		Kategorija cat2 = new Kategorija("Kategorija2", 8, 4, 2);
		Kategorija cat3 = new Kategorija("Kategorija3", 8, 8, 4);
		
		Korisnik Korisnik1 = new Korisnik("peraperic@gmail.com","1234", "Pera", "Peric",Uloga.SUPERADMIN);
		Korisnik Korisnik2 = new Korisnik("anaanic@gmail.com","aaa","Ana", "Anic", Uloga.ADMIN);
		Korisnik Korisnik3 = new Korisnik("markomarkovic@gmail.com","bbb","Marko", "Markovic", Uloga.KORISNIK);
		
		Disk disk1 = new Disk("Disk1", tipDiska.SSD, 500, "");
		Disk disk2 = new Disk("Disk2", tipDiska.SSD, 250, "");
		Disk disk3 = new Disk("Disk3", tipDiska.HDD, 1000, "");
		Disk disk4 = new Disk("Disk4", tipDiska.HDD, 1000, "");
		
		VirtualnaMasina vm1 = new VirtualnaMasina("vm1", cat1.getIme(), cat1.getBrojJezgara(), cat1.getRAM(), cat1.getGPU());
		VirtualnaMasina vm2 = new VirtualnaMasina("vm2", cat2.getIme(), cat2.getBrojJezgara(), cat2.getRAM(), cat2.getGPU());
		VirtualnaMasina vm3 = new VirtualnaMasina("vm3", cat3.getIme(), cat3.getBrojJezgara(), cat3.getRAM(), cat3.getGPU());
		
		vm1.dodajDisk(disk1);
		vm1.dodajDisk(disk2);
		
		vm2.dodajDisk(disk3);
				
		vm3.dodajDisk(disk4);
		
		disk1.setVm(vm1.getIme());
		disk2.setVm(vm1.getIme());
		disk3.setVm(vm2.getIme());
		disk4.setVm(vm3.getIme());
			
		Organizacija org1 = new Organizacija("Org1", "Opis1", "");
		Organizacija org2 = new Organizacija("Org2", "Opis2", "");
		
		org1.dodajKorisnika(Korisnik1);
		org1.dodajKorisnika(Korisnik2);
		org1.dodajVM(vm1);
		org1.dodajVM(vm2);
		
		org2.dodajKorisnika(Korisnik3);
		org2.dodajVM(vm3);
		
		HashMap<String, Organizacija> organizacije = new HashMap<String, Organizacija>();
		HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
		HashMap<String, VirtualnaMasina> vmovi = new HashMap<String, VirtualnaMasina>();
		HashMap<String, Kategorija> kategorije = new HashMap<String, Kategorija>();
		HashMap<String, Disk> diskovi = new HashMap<String, Disk>();
		
		organizacije.put(org1.getIme(), org1);
		organizacije.put(org2.getIme(), org2);
		
		korisnici.put(Korisnik1.getEmail(), Korisnik1);
		korisnici.put(Korisnik2.getEmail(), Korisnik2);
		korisnici.put(Korisnik3.getEmail(), Korisnik3);
		
		vmovi.put(vm1.getIme(), vm1);
		vmovi.put(vm2.getIme(), vm2);
		vmovi.put(vm3.getIme(), vm3);
		
		kategorije.put(cat1.getIme(), cat1);
		kategorije.put(cat2.getIme(), cat2);
		kategorije.put(cat3.getIme(), cat3);
		
		diskovi.put(disk1.getIme(), disk1);
		diskovi.put(disk2.getIme(), disk2);
		diskovi.put(disk3.getIme(), disk3);
		diskovi.put(disk4.getIme(), disk4);
		
		Gson gson = new Gson();
		kreirajIUpisiUFajl("organizacije.txt", gson.toJson(organizacije));
		kreirajIUpisiUFajl("korisnici.txt", gson.toJson(korisnici));
		kreirajIUpisiUFajl("vmasine.txt", gson.toJson(vmovi));
		kreirajIUpisiUFajl("kategorije.txt", gson.toJson(kategorije));
		kreirajIUpisiUFajl("diskovi.txt", gson.toJson(diskovi));
	}
	
	private void kreirajIUpisiUFajl(String putanja, String json) {
		FileWriter myWriter;
		try {
			myWriter = new FileWriter(putanja);
			myWriter.write(json);
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
