package kirjarekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * pitää yllä varsinaista kirjarekisteriä, eli osaa lisätä ja poistaa jäsenen
 * lukee ja kirjoittaa kirjat tiedostoon
 * osaa etsiä ja lajitella  
 * @author tuomas
 * @version 21 Feb 2022
 *
 */
public class Kirjat {
	
	private static final int MAX_KIRJOJA = 5; 
	
	private int lkm = 0;
	private Kirja[] alkiot;
	
	
	/**
	 * Luodaan alustava taulukko
	 */
	public Kirjat() {
		alkiot = new Kirja[MAX_KIRJOJA];
	}

	
	/**
     * Palauttaa viitteen i:teen kirjaan.
     * @param i monennenko kirjan viite halutaan
     * @return viite kirjaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    protected Kirja anna(int i) throws IndexOutOfBoundsException {
        if ( i < 0 || lkm <= i ) 
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        if (alkiot[i] == null) return null;
        return alkiot[i];
    }

	
	
	/**
	 * palauttaa taulukon alkioiden luku määrän
	 * @return
	 */
	public int getLkm() {
		return lkm;
	}
	
	
	/**
	 * lisaa kirjan alkiot taulukkoon
	 * @param kirja
	 * @example
	 */
	public void lisaa(Kirja kirja) throws SailoException {
		if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
		alkiot[lkm] = kirja;
		lkm++;
	}
	
	/**
	 * Tallentaa tiedostoon
	 * @param nimi
	 */
	public void tallenna(String hakemisto) throws SailoException {
		File ftied = new File(hakemisto + "/kirjat.dat");
		try (PrintStream fo = new PrintStream(ftied)) {
			fo.println("; Kirjat");
			fo.println("; alkioiden määrä: " + getLkm());
			for (int i = 0; i < getLkm(); i++) {
				Kirja kirja = this.anna(i);
				fo.println(kirja.toString());
			}
		} catch (Exception ex) {
			throw new SailoException("Tiedosto " + ftied.getAbsolutePath());
		}
 	}
	
	
	public void lueTiedostosta(String hakemisto) throws SailoException {
		String nimi = hakemisto + "/kirjat.dat";
		File ftied = new File(nimi);
		
		try (Scanner fi = new Scanner(new FileInputStream(ftied))) {
			while (fi.hasNext()) {
				String s = fi.nextLine().trim();
				if (s == null || s.equals("") || s.charAt(0) == ';') continue;
				Kirja kirja = new Kirja();
				kirja.parse(s); // kertoisi onnistumisesta
				lisaa(kirja);
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Ei saa luettua" + nimi);
		}
	}
	

	public void korvaaTaiLisaa(Kirja kirja) throws SailoException {
		int id = kirja.getTunnusNro();
		for (int i = 0; i < lkm; i++) {
			if (alkiot[i].getTunnusNro() == id) {
				alkiot[i] = kirja;
				return;
			}
		}
		lisaa(kirja);
	}


	/** 
     * Poistaa kirjan jolla on valittu tunnusnumero  
     * @param id poistettavan kirjan tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Kirjat kirjat = new Kirjat(); 
     * Kirja aku1 = new Kirja(), aku2 = new Kirja(), aku3 = new Kirja(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * kirjat.lisaa(aku1); kirjat.lisaa(aku2); kirjat.lisaa(aku3); 
     * kirjat.poista(id1+1) === 1; 
     * kirjat.annaId(id1+1) === null; kirjat.getLkm() === 2; 
     * kirjat.poista(id1) === 1; kirjat.getLkm() === 1; 
     * kirjat.poista(id1+3) === 0; kirjat.getLkm() === 1; 
     * </pre> 
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        return 1; 
    } 
    
    
    /** 
     * Etsii kirjan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen kirjan indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Kirjat kirjat = new Kirjat(); 
     * Kirja aku1 = new Kirja(), aku2 = new Kirja(), aku3 = new Kirja(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * kirjat.lisaa(aku1); kirjat.lisaa(aku2); kirjat.lisaa(aku3); 
     * kirjat.etsiId(id1+1) === 1; 
     * kirjat.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    }
    
    
	public Collection<Kirja> etsi(String ehto, int k) {
		var<Kirja> loytyneet = new ArrayList<Kirja>();
		int hk = k;
		if (hk < 0) hk = 1;
		for (int i = 0; i < getLkm(); i++) {
			Kirja kirja = anna(i);
			String s = kirja.anna(hk);
			if (WildChars.onkoSamat(s,ehto))
				loytyneet.add(kirja);
		}
		Collections.sort(loytyneet, new Kirja.Vertailija(hk));
		return loytyneet;
	}

	
	/**
	 * antaa id:tä vastaavan kirjan.
	 * @param i
	 * @return
	 * @example
	 * <pre name="test">
	 * 
	 * </pre>
	 */
	public Kirja annaId(int i) {
		for (Kirja kirja: alkiot) {
			if (i == kirja.getTunnusNro()) return kirja;
		}
		return null;
	}

	
	public static void main(String[] args) throws Exception {
		Kirjat kirjat = new Kirjat();
		
		Kirja kirja = new Kirja(), tennis = new Kirja();
		kirja.rekisteroi();
		tennis.rekisteroi();
		kirja.taytaTiedoillaTennis();
		tennis.taytaTiedoillaTennis();
		
		kirjat.lisaa(kirja);
		kirjat.lisaa(tennis);
		
		try {
			kirjat.tallenna("kirjat");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

}
