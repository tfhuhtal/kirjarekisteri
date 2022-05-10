package kirjarekisteri;

import java.io.*;
import java.util.Comparator;
import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;


/**
 * - tietää kirjan kentät(nimi, kirjailija, jne.)
 * - osaa tarkistaa kenttien oikeellisuuden.
 * - osaa muuttaa |Tennisesseet|David Foster|... -merkkijonon kirjan tiedoiksi
 * - osaa antaa merkkijonona i:n kentän tiedot
 * - osaa laittaa merkkijonon i:neksi kentäksi
 * @author tuomas
 * @version 22 Feb 2022
 *
 */
public class Kirja implements Cloneable, Tietue {
	
	private int tunnusNro;
	private String nimi 		= "";
	private String kirjailija	= "";
	private int julkaisuvuosi;
	private String genre 		= "";
	private int sivumaara;
	
	private static int seuraavaNro = 1;
	
	
	public static class Vertailija implements Comparator<Kirja> {
		private int k;  
        
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 

        @Override 
        public int compare(Kirja kirja1, Kirja kirja2) {
        	return kirja1.getAvain(k).compareToIgnoreCase(kirja2.getAvain(k));
        }

	}
	
	
	public void setTunnusNro(int nr) {
		tunnusNro = nr;
		if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
	}
	
	
	public String getAvain(int k) {
		switch ( k ) {
			case 0:
				return tunnusNro + "";
			case 1:
				return nimi.toLowerCase();
			case 2:
				return kirjailija.toLowerCase();
			case 3:
				return julkaisuvuosi + "";
			case 4:
				return genre.toLowerCase();
			case 5: 
				return sivumaara + "";
			default:
				return "Kissanhiekkaa";
		}
	}


	/**
	 * alustaa kirjantiedot tyhjiksi
	 */
	public Kirja() {
		//toimii näin
	}
	
	
	/**
	 * @return kirjan nimi
	 * @example
	 * <pre name="test">
	 * Kirja tennis = new Kirja();
	 * tennis.taytaTiedoillaTennis();
	 * tennis.getNimi() =R= "Tennisesseet"; 
	 * </pre>
	 */
	public String getNimi() {
		return nimi;
	}
	
	
    /**
     * Antaa kirjalle seuraavan rekisterinumeron.
     * @return kirjan uusi tunnusNro
     * @example
     * <pre name="test">
     *   Kirja tennis = new Kirja();
     *   tennis.getTunnusNro() === 0;
     *   tennis.rekisteroi();
     *   Kirja tennis2 = new Kirja();
     *   tennis2.rekisteroi();
     *   int n1 = tennis.getTunnusNro();
     *   int n2 = tennis2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
	
	
    /**
     * Palauttaa kirjan tunnusnumeron.
     * @return kirjan tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Tulostetaan kirjan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
    	out.println(String.format("%03d", tunnusNro, 3) + "  " + nimi + "  "
                + kirjailija);
    	out.println("  " + julkaisuvuosi + "  " + genre + "  " + sivumaara);
    }


    /**
     * Tulostetaan kirjan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Täyttää kirjan tiedot 
     */
    public void taytaTiedoillaTennis() {
    	nimi = "Tennisesseet";
    	kirjailija = "David Foster Wallace";
    	julkaisuvuosi = 2012;
    	genre = "essee";
    	sivumaara = 158;
    }

    
    /**
     * @example
     * <pre name="test">
     * Kirja k = new Kirja(); k.taytaTiedoillaTennis();
     * Kirja s = new Kirja(); s.taytaTiedoillaTennis();
     * k.toString().equals(s.toString()) === true;
     * k.setTunnusNro(6);
     * k.toString().equals(s.toString()) === false;
     * </pre>
     */
    @Override
    public String toString() {
    	return "" + 
    			getTunnusNro() + "|" +
    			getNimi() + "|" +
    			kirjailija + "|" +
    			julkaisuvuosi + "|" +
    			genre + "|" +
    			sivumaara;
    }


	public void parse(String s) {
		var sb = new StringBuilder(s);
		setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
		nimi = Mjonot.erota(sb, '|', nimi);
		kirjailija = Mjonot.erota(sb, '|', kirjailija);
		julkaisuvuosi = Mjonot.erota(sb, '|', julkaisuvuosi);
		genre = Mjonot.erota(sb, '|', genre);
		sivumaara = Mjonot.erota(sb, '|', sivumaara);
		
	}


	public String getKirjailija() {
		return kirjailija;
	}


	public String getVuosi() {
		return "" + julkaisuvuosi;
	}


	/**
	 * palauttaa genren.
	 * @return
	 */
	public String getGenre() {
		return genre;
	}

	
	/**
	 * palauttaa kirjan sivujen määrän.
	 * @return sivumäärä
	 */
	public String getSivuja() {
		return "" + sivumaara;
	}

	
	/**
	 * palauttaa kenttien määrän.
	 */
	public int getKenttia() {
		return 6;
	}

	
	/**
	 * palauttaa ensimmäisen käyttöliittymässä näytettävän kentän.
	 */
	public int ekaKentta() {
		return 1;
	}


	public String getKysymys(int k) {
		switch ( k ) {
		case 0: return "tunnusNro";
		case 1: return "nimi";
		case 2: return "kirjailija";
		case 3: return "julkaisuvuosi";
		case 4: return "genre";
		case 5: return "sivumäärä";
		default: return "Ääliö";
		}
	}


	public String aseta(int k, String jono) {
		String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2: 
        	kirjailija = tjono;
        	return null;
        case 3:
        	try {
        		if (Integer.valueOf(tjono) > -1 || tjono.length() < 5)
        			julkaisuvuosi = Mjonot.erotaEx(sb, '§', julkaisuvuosi);
            } catch ( NumberFormatException ex ) {
                return "Julkaisuvuosi väärin " + ex.getMessage();
            }
            return null;
        case 4:
        	genre = tjono;
        	return null;
        case 5: 
        	try {
        		if (Integer.valueOf(tjono) > -1)
        			sivumaara = Integer.valueOf(tjono);
        	} catch (Exception e) {
        		return e.getMessage();
        	}
        	return null;
        default: 
        	return "ÄÄliö";
        }
	}


	/**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
	@Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + kirjailija;
        case 3: return "" + julkaisuvuosi;
        case 4: return "" + genre;
        case 5: return "" + sivumaara;
        default: return "Äääliö";
        }
    }
    
	
	/**
	 * <pre name="test">
     * #THROWS CloneNotSupportedException 
     * Kirja mer = new Kirja();
     * mer.parse("   2   |  10  |   158  | 12.1.2022 | 14.1.2022 | kissa ");
     * Kirja kopio = mer.clone();
     * kopio.toString() === mer.toString();
     * mer.parse("   1   |  11  |  1122  | 19.1.2000 | 1.1.2100 | koira ");
     * kopio.toString().equals(mer.toString()) === false;
     * </pre>
	 */
    @Override
    public Kirja clone() throws CloneNotSupportedException {
    	Kirja uusi;
    	uusi = (Kirja)super.clone();
    	return uusi;	
    }

    
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kirja tennis = new Kirja();
		Kirja tennis2 = new Kirja();
		
		tennis.rekisteroi();
		tennis2.rekisteroi();
		tennis.tulosta(System.out);
		tennis.taytaTiedoillaTennis();
		tennis.tulosta(System.out);
		tennis2.taytaTiedoillaTennis();
		tennis2.tulosta(System.out);
	}
}
