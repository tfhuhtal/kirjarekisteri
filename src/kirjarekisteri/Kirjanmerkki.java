package kirjarekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Tietue;


/**
 * - tietää kirjanmerkin kentät(sivu, aloitettu, jne.
 * - osaa tarkistaa kenttien oikeellisuuden.
 * - osaa muuttaa |158|24.12.2021|14.1.2022|"" -merkkijonon kirjan tiedoiksi
 * - osaa antaa merkkijonona i:n kentän tiedot
 * - osaa laittaa merkkijonon i:neksi kentäksi
 * 
 * @author tuomas
 * @version 4 Mar 2022
 *
 */
public class Kirjanmerkki implements Cloneable, Tietue {
	
	private int tunnusNro;
	private int kirjaNro;
	private int sivulla;
	private String aloitettu = "";
	private String lopetettu = "";
	private String muistiinpanot = "";
	
	private static int seuraavaNro = 1;
	
	
	public void setTunnusNro(int nr) {
		tunnusNro = nr;
		if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
	}
	
	
	public Kirjanmerkki() {
		//
	}
	
	
	/**
	 * muodostaja kirja parametrillä (parametri pakollinen viitteiden saamisen takia)
	 * @param kirja Kirja luokan kirja olio
	 */
	public Kirjanmerkki(int kirjaNro) {
		this.kirjaNro = kirjaNro;
	}
	
	
	/**
	 * rekisteröi jokaiselle oliolle oman tunnus numeron.
	 * @return
	 * @example
	 * <pre name="test">
	 * Kirjanmerkki merkki = new Kirjanmerkki();
	 * merkki.getTunnusNro() === 0;
	 * merkki.rekisteroi();
	 * var merkki2 = new Kirjanmerkki();
	 * merkki2.rekisteroi();
	 * int n1 = merkki.getTunnusNro();
	 * int n2 = merkki2.getTunnusNro();
	 * n1 === n2-1;
	 * </pre>
	 */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    public int getTunnusNro() {
    	return tunnusNro;
    }
    
    
    public int getKirjaNro() {
    	return kirjaNro;
    }
    
    
    public int getSivulla() {
    	return sivulla;
    }

	
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", kirjaNro, 3) + " " + String.format("%03d", tunnusNro, 3) + " " + sivulla + " " + aloitettu + " " + lopetettu);
		out.println(muistiinpanot);
	}
	
	
    /**
     * Tulostetaan kirjanmerkin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * täyttää kutsuttaessa oliolle vakiotiedot
     * @param nro
     */
    public void taytaTiedoillaTennisMerkki(int nro) {
    	kirjaNro = nro;
    	sivulla = 158;
    	aloitettu = "24.12.2021";
    	lopetettu = "14.1.2022";
    	muistiinpanot = "Ei valittamista!";
    }


	public void parse(String s) {
		var sb = new StringBuilder(s);
		kirjaNro = Mjonot.erota(sb, '|', kirjaNro);
		setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
		sivulla = Mjonot.erota(sb, '|', sivulla);
		aloitettu = Mjonot.erota(sb, '|', aloitettu);
		lopetettu = Mjonot.erota(sb, '|', lopetettu);
		muistiinpanot = Mjonot.erota(sb, '|', muistiinpanot);
		
	}
	
	
	@Override
	public String toString() {
		return ""+ kirjaNro + "|" + getTunnusNro() + "|" + sivulla + "|" + aloitettu + "|" + lopetettu + "|" + muistiinpanot;
	}


	@Override
	public int getKenttia() {
		return 6;
	}


	@Override
	public int ekaKentta() {
		return 2;
	}


	@Override
	public String getKysymys(int k) {
		switch ( k ) {
			case 0:
				return "kirjaId";
			case 1:
				return "id";
			case 2:
				return "sivulla";
			case 3:
				return "aloitettu";
			case 4: 
				return "lopetettu";
			case 5: 
				return "muistiinpanot";
			default:
				return "???";
		}
	}

	/**
	 * antaa arvoa k vastaavan alkion oliosta.
	 */
	@Override
	public String anna(int k) {
		switch ( k ) {
			case 0:
				return kirjaNro + "";
			case 1:
				return tunnusNro + "";
			case 2:
				return sivulla + "";
			case 3:
				return aloitettu;
			case 4:
				return lopetettu;
			case 5:
				return muistiinpanot;
			default:
				return "???";
		}
	}

	 
	@Override
	public String aseta(int k, String s) {
		String st = s.trim();
		var sb = new StringBuilder(st);
		switch (k) {
        case 0:
        	kirjaNro = Mjonot.erota(sb, '$', kirjaNro);
            return null;
        case 1:
        	setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
            return null;
        case 2:
        	try {
                sivulla = Mjonot.erotaEx(sb, '§', sivulla);
            } catch (NumberFormatException ex) {
                return "sivulla: Ei kokonaisluku ("+st+")";
            }
            return null;
        case 3:
            aloitettu = Mjonot.erota(sb, '§', aloitettu);
            return null;
        case 4:
        	lopetettu = Mjonot.erota(sb, '§', lopetettu);
            return null;
        case 5:
        	muistiinpanot = st;
        	return null;
        default:
            return "Väärä kentän indeksi";
		}
    }

		
	/**
     * Tehdään identtinen klooni kirjasta
     * @return Object kloonattu kirja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Kirjanmerkki mer = new Kirjanmerkki();
     *   mer.parse("   2   |  10  |   158  | 12.1.2022 | 14.1.2022 | kissa ");
     *   Kirjanmerkki kopio = mer.clone();
     *   kopio.toString() === mer.toString();
     *   mer.parse("   1   |  11  |  1122  | 19.1.2000 | 1.1.2100 | koira ");
     *   kopio.toString().equals(mer.toString()) === false;
     * </pre>
     */
	@Override
	public Kirjanmerkki clone() throws CloneNotSupportedException {
		return (Kirjanmerkki)super.clone();
	}
	
	
	@Override
    public int hashCode() {
        return tunnusNro;
    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
