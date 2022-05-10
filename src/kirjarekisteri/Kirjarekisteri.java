package kirjarekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;




/**
 * - huolehtii kirjat- ja kirjanmerkki-luokkien välisestä yhteistyöstä 
 * ja välittää näitä tietoja pyydettäessä.
 * - lukee ja kirjoittaa kirjat tiedostoo pyytämällä apua avustajiltaan 
 * Avustajat:
 * - Kirja
 * - Kirjat
 * - Kirjanmerkit
 * - Kirjanmerkki
 * 
 * @author tuomas
 * @version 22 Feb 2022
 *
 */
public class Kirjarekisteri {
	
	private Kirjat kirjat = new Kirjat();
	private Kirjanmerkit merkit = new Kirjanmerkit();
	
	private String hakemisto = "kirjat";
	
	/**
	 * Lisätään uusi kirja
	 * @param kirja
	 */
	public void lisaa(Kirja kirja) throws SailoException {
		kirjat.lisaa(kirja);
	}
	
	
	public Kirja annaKirja(int i) {
		return kirjat.anna(i);
	}
	
	
	/**
	 * lisää annetun kirjanmerkin kirjanmerkki taulukkoon.
	 * @param merkki
	 */
	public void lisaa(Kirjanmerkki merkki) {
		merkit.lisaa(merkki);
	}
	
	
	/**
	 * haetaan kaikki kirjan kirjanmerkit
	 * @param kirja
	 * @return tietorakenne jossa viitteet löydettyihin kirjanmerkkeihin
	 * @example
	 * <pre name="test">
	 * #import java.util.List;
	 * Kirjarekisteri rekisteri = new Kirjarekisteri();
	 * Kirja kz = new Kirja(), kz1 = new Kirja(), kz2 = new Kirja();
	 * kz.rekisteroi(); kz1.rekisteroi(); kz2.rekisteroi();
	 * int id1 = kz.getTunnusNro();
	 * int id2 = kz1.getTunnusNro();
	 * Kirjanmerkki tennis = new Kirjanmerkki(id1); rekisteri.lisaa(tennis);
	 * Kirjanmerkki tennis2 = new Kirjanmerkki(id2); rekisteri.lisaa(tennis2);
	 * 
	 * List<Kirjanmerkki> loytyneet;
	 * loytyneet = rekisteri.annaMerkit(kz2);
	 * loytyneet.size() === 0;
	 * loytyneet = rekisteri.annaMerkit(kz);
	 * loytyneet.size() === 1;
	 * </pre>
	 */
	public List<Kirjanmerkki> annaMerkit(Kirja kirja) {
		return merkit.annaKirjanmerkit(kirja.getTunnusNro());
	}
	
	
	/**
	 * kertoo kirjojen lukumäärän
	 * @return
	 */
	public int getKirjoja() {
		return kirjat.getLkm();
	}
	
	
	/**
	 * lukee tiedostosta tiedot
	 * @param nimi
	 * @throws SailoException
	 */
	public void lueTiedostosta(String nimi) throws SailoException {
		File dir = new File(nimi);
		dir.mkdir();
		kirjat = new Kirjat();
		merkit = new Kirjanmerkit();
		
		hakemisto = nimi;
		kirjat.lueTiedostosta(nimi);
		merkit.lueTiedostosta(nimi);
	}
	
	
	public void tallenna() throws SailoException {
		String virhe = "";
		try {
			kirjat.tallenna(hakemisto);
		} catch (SailoException ex) {
			virhe = ex.getMessage();
		}
		
		try {
			merkit.tallenna(hakemisto);
		} catch (SailoException ex) {
			virhe += ex.getMessage();
		}
		
		if (! "".equals(virhe)) throw new SailoException(virhe);
	}


	public void korvaaTaiLisaa(Kirja kirja) throws SailoException {
		kirjat.korvaaTaiLisaa(kirja);
	}


	public void korvaaTaiLisaa(Kirjanmerkki mer) {
		merkit.korvaaTaiLisaa(mer);
	}


	public void poistaKirjanmerkki(Kirjanmerkki merkki) {
		merkit.poista(merkki);
		
	}


	public int poista(Kirja kirja) {
		if (kirja == null) return 0;
		int ret = kirjat.poista(kirja.getTunnusNro());
		merkit.poistaKirjanKirjanmerkit(kirja.getTunnusNro());
		return ret;
	}


	public Collection<Kirja> etsi(String ehto, int k) {
		return kirjat.etsi(ehto, k);
	}

}
