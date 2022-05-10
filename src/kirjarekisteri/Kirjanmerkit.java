package kirjarekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;


/**
 * - pitää yllä varsinaista kirjanmerkkirekisteriä, eli osaa lisätä ja poistaa kirjanmerkin
 * - lukee ja kirjoittaa kirjanmerkit tiedostoon
 * - osaa etsiä ja lajitella
 * @author tuomas
 * @version 4 Apr 2022
 *
 */
public class Kirjanmerkit implements Iterable<Kirjanmerkki>{
	
	private final List<Kirjanmerkki> alkiot = new ArrayList<Kirjanmerkki>();
	
	
	public Kirjanmerkit() {
		//täytettä...
	}
	
	
	public void lisaa(Kirjanmerkki merkki) {
		alkiot.add(merkki);
	}
	
	
	public int getLkm() {
		return alkiot.size();
	}

	
	@Override
	public Iterator<Kirjanmerkki> iterator() {
		return alkiot.iterator();
	}
	
	
	public List<Kirjanmerkki> annaKirjanmerkit(int tunnusnro) {
		List<Kirjanmerkki> loydetyt = new ArrayList<Kirjanmerkki>();
		for (Kirjanmerkki kir: alkiot) {
			if (kir.getKirjaNro() == tunnusnro) loydetyt.add(kir);
		}
		return loydetyt;
	}
	
	
	/**
	 * Tallentaa tiedostoon
	 * @param nimi
	 */
	public void tallenna(String hakemisto) throws SailoException {
		File ftied = new File(hakemisto + "/kirjanmerkit.dat");
		try (PrintStream fo = new PrintStream(ftied)) {
			fo.println(";Kirjanmerkit");
			fo.println("; alkioiden määrä: " + getLkm());
			for (var mer: alkiot) {
				fo.println(mer.toString());
			}
		} catch (Exception ex) {
			throw new SailoException("Tiedosto " + ftied.getAbsolutePath());
		}
 	}
	
	
	/**
	 * 
	 * @param hakemisto
	 * @throws SailoException
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * #PACKAGEIMPORT 
     * #import java.io.File;
     * #import kirjarekisteri.*;
     * #import java.util.Iterator;
	 * Kirjanmerkit merkit = new Kirjanmerkit(); 
     * Kirjanmerkki tennis1 = new Kirjanmerkki(); tennis1.taytaTiedoillaTennisMerkki(2); 
     * Kirjanmerkki tennis2 = new Kirjanmerkki(); tennis2.taytaTiedoillaTennisMerkki(1); 
     * Kirjanmerkki tennis3 = new Kirjanmerkki(); tennis3.taytaTiedoillaTennisMerkki(2); 
     * Kirjanmerkki tennis4 = new Kirjanmerkki(); tennis4.taytaTiedoillaTennisMerkki(1); 
     * Kirjanmerkki tennis5 = new Kirjanmerkki(); tennis5.taytaTiedoillaTennisMerkki(2); 
     * String hakemisto = "testikelmit"; 
     * File dir = new File(hakemisto);
     * dir.mkdir();
   	 * dir.delete();
     * merkit.lueTiedostosta(hakemisto); #THROWS SailoException
     * merkit.lisaa(tennis1);
     * merkit.lisaa(tennis2);
     * merkit.lisaa(tennis3);
     * merkit.lisaa(tennis4);
     * merkit.lisaa(tennis5);
     * merkit.tallenna(hakemisto); #THROWS SailoException
     * merkit = new Kirjanmerkit();
     * merkit.lueTiedostosta(hakemisto);
     * Iterator<Kirjanmerkki> i = merkit.iterator();
     * i.next().toString() === tennis1.toString();
     * i.next().toString() === tennis2.toString();
     * i.next().toString() === tennis3.toString();
     * i.next().toString() === tennis4.toString();
     * i.next().toString() === tennis5.toString();
     * i.hasNext() === false;
     * merkit.lisaa(tennis5);
     * merkit.tallenna(hakemisto);
     * dir.delete() === true;
	 * </pre>
	 */
	public void lueTiedostosta(String hakemisto) throws SailoException {
		String nimi = hakemisto + "/kirjanmerkit.dat";
		File ftied = new File(nimi);
		
		try (Scanner fi = new Scanner(new FileInputStream(ftied))) {
			while (fi.hasNext()) {
				String s = fi.nextLine().trim();
				if ("".equals(s) || s.charAt(0) == ';') continue;
				Kirjanmerkki merkki = new Kirjanmerkki();
				merkki.parse(s); // kertoisi onnistumisesta
				lisaa(merkki);
				
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Ei saa luettua tiedostoa " + nimi);
		}
	}


	public void korvaaTaiLisaa(Kirjanmerkki mer) {
		int id = mer.getTunnusNro();
		for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
            	alkiot.set(i, mer);
            	return;
            }
		}
		lisaa(mer);
	}
	
	
	/**
     * Poistaa valitun kirjanmerkin
     * @param kirjanmerkki poistettava kirjanmerkki
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * #import java.util.List;
     *  Kirjanmerkit merkit = new Kirjanmerkit();
     *  Kirjanmerkki pitsi21 = new Kirjanmerkki(); pitsi21.taytaTiedoillaTennisMerkki(2);
     *  Kirjanmerkki pitsi11 = new Kirjanmerkki(); pitsi11.taytaTiedoillaTennisMerkki(1);
     *  Kirjanmerkki pitsi22 = new Kirjanmerkki(); pitsi22.taytaTiedoillaTennisMerkki(2); 
     *  Kirjanmerkki pitsi12 = new Kirjanmerkki(); pitsi12.taytaTiedoillaTennisMerkki(1); 
     *  Kirjanmerkki pitsi23 = new Kirjanmerkki(); pitsi23.taytaTiedoillaTennisMerkki(2); 
     *  merkit.lisaa(pitsi21);
     *  merkit.lisaa(pitsi11);
     *  merkit.lisaa(pitsi22);
     *  merkit.lisaa(pitsi12);
     *  merkit.poista(pitsi23) === false ; merkit.getLkm() === 4;
     *  merkit.poista(pitsi11) === true;   merkit.getLkm() === 3;
     *  List<Kirjanmerkki> h = merkit.annaKirjanmerkit(1);
     *  h.size() === 1; 
     *  h.get(0) === pitsi12;
     * </pre>
     */
    public boolean poista(Kirjanmerkki mer) {
        boolean ret = alkiot.remove(mer);
        return ret;
    }
    
    
    /**
     * Poistaa kaikki tietyn tietyn kirjan kirjanmerkit
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     * #import java.util.List;
     *  Kirjanmerkit merkit = new Kirjanmerkit();
     *  Kirjanmerkki pitsi21 = new Kirjanmerkki(); pitsi21.taytaTiedoillaTennisMerkki(2);
     *  Kirjanmerkki pitsi11 = new Kirjanmerkki(); pitsi11.taytaTiedoillaTennisMerkki(1);
     *  Kirjanmerkki pitsi22 = new Kirjanmerkki(); pitsi22.taytaTiedoillaTennisMerkki(2); 
     *  Kirjanmerkki pitsi12 = new Kirjanmerkki(); pitsi12.taytaTiedoillaTennisMerkki(1); 
     *  Kirjanmerkki pitsi23 = new Kirjanmerkki(); pitsi23.taytaTiedoillaTennisMerkki(2); 
     *  merkit.lisaa(pitsi21);
     *  merkit.lisaa(pitsi11);
     *  merkit.lisaa(pitsi22);
     *  merkit.lisaa(pitsi12);
     *  merkit.lisaa(pitsi23);
     *  merkit.poistaKirjanKirjanmerkit(2) === 3;  merkit.getLkm() === 2;
     *  merkit.poistaKirjanKirjanmerkit(3) === 0;  merkit.getLkm() === 2;
     *  List<Kirjanmerkki> h = merkit.annaKirjanmerkit(2);
     *  h.size() === 0; 
     *  h = merkit.annaKirjanmerkit(1);
     *  h.get(0) === pitsi11;
     *  h.get(1) === pitsi12;
     * </pre>
     */
    public int poistaKirjanKirjanmerkit(int tunnusNro) {
        int n = 0;
        for (Iterator<Kirjanmerkki> it = alkiot.iterator(); it.hasNext();) {
            Kirjanmerkki mer = it.next();
            if ( mer.getKirjaNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        return n;
    }


    public static void main(String[] args) {
		Kirjanmerkit merkit = new Kirjanmerkit();
		Kirjanmerkki merkki = new Kirjanmerkki();
		merkki.taytaTiedoillaTennisMerkki(1);
		Kirjanmerkki merkki1 = new Kirjanmerkki();
		merkki1.taytaTiedoillaTennisMerkki(1);
		merkit.lisaa(merkki);
		merkit.lisaa(merkki1);
		try {
			merkit.tallenna("kirjat");
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
		
		try {
			
			merkit.lueTiedostosta("kirjat");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		List<Kirjanmerkki> kirjanmerkit = merkit.annaKirjanmerkit(1);
		for (var mer: kirjanmerkit) {
			System.out.println(mer);
		}
	}
}
