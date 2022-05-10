package fxKirjat;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import kirjarekisteri.Kirja;
import kirjarekisteri.Kirjanmerkki;
import kirjarekisteri.Kirjarekisteri;
import kirjarekisteri.SailoException;
import static fxKirjat.TietueDialogController.getFieldId;
import javafx.scene.layout.GridPane;


/**
 * Controlleri kirjarekisteri ohjelmalle
 * Tulostus toiminta ei vielä toimi
 * @author tuomas
 * @version 11.2.2022
 *
 */
public class FxKirjatGUIController implements Initializable{   
	
	@FXML private TextField hakuehto;
	@FXML private ComboBoxChooser<String> cbKentat;
	@FXML private Label labelVirhe;
	@FXML private ListChooser<Kirja> chooserKirjat;
	@FXML private GridPane gridKirja;
	@FXML private StringGrid<Kirjanmerkki> tableKirjanmerkit;
	
	
	
	@Override
    public void initialize(URL url, ResourceBundle bundle) {  
		alusta();
    }
	
	
	@FXML private void handleHakuehto() {
		hae(0);
	}
	

	@FXML private void handleTallenna() {
		tallenna();
	}
	
	
	@FXML private void handleTulosta() {
        TulostusController tulostusCtrl = TulostusController.tulosta("");
        tulostaValitut(tulostusCtrl.getTextArea()); 
    }

	
	
	@FXML private void handleLopeta() {
		Platform.exit();
	}
	
	
    @FXML private void handleUusiKirja() throws SailoException {
    	uusiKirja();
    }
    
    
    @FXML private void handleMuokkaakirjaa() {
    	muokkaa(1);
    }
    
    
    @FXML private void handleLisaaKirjanmerkki() {
    	uusiMerkki();
    }
    
    
    @FXML private void handleMuokkaaKirjanmerkkia() {
    	muokkaaKirjanmerkkia();
    }
    
    
    @FXML private void handlePoistaKirja() {
    	poistaKirja();
    }
    
    
    @FXML private void handlePoistaKirjamerkki() {
    	poistaKirjanmerkki();
    }
    
    
    
    @FXML private void handleApua() {
    	avustus();
    }
    
    
    @FXML private void handleTietoja() {
        ModalController.showModal(FxKirjatGUIController.class.getResource("AboutView.fxml"), "Kirjat", null, "");
    }
    
    
    @FXML private void handleEdistyminen() {
    	LineChartController.avaa(null, kirjaKohdalla, kirjarekisteri);
    }

    //___________________________________________________________________________________________________________________
    //___________________________________________________________________________________________________________________
    //___________________________________________________________________________________________________________________
    
    private Kirjarekisteri kirjarekisteri;
    private Kirja kirjaKohdalla;
    private TextField edits[];
    private int kentta = 0;
    private static Kirja apukirja = new Kirja();
    private static Kirjanmerkki apumerkki = new Kirjanmerkki();
    
    
    /**
     * tyhjentää uuden listan.
     */
    private void alusta() {
    	kirjarekisteri = new Kirjarekisteri();
    	
    	chooserKirjat.clear();
    	chooserKirjat.addSelectionListener(e -> naytaKirja());
    	cbKentat.clear(); 
        for (int k = apukirja.ekaKentta(); k < apukirja.getKenttia(); k++) 
            cbKentat.add(apukirja.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0);
        
        edits = TietueDialogController.luoKentat(gridKirja, apukirja);  
        for (TextField edit: edits)  
            if ( edit != null ) {  
                edit.setEditable(false);  
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
                edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);}); 
            }
        
        int eka = apumerkki.ekaKentta();
        int lkm = apumerkki.getKenttia();
        String[] headings = new String[lkm-eka];
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apumerkki.getKysymys(k);
        tableKirjanmerkit.initTable(headings);
        tableKirjanmerkit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableKirjanmerkit.setEditable(false);
        tableKirjanmerkit.setPlaceholder(new Label("Ei vielä kirjanmerkkejä"));
        
        tableKirjanmerkit.setColumnSortOrderNumber(1);
        tableKirjanmerkit.setColumnSortOrderNumber(2);
        tableKirjanmerkit.setColumnWidth(1, 60);
        tableKirjanmerkit.setColumnWidth(2, 60);
        
        tableKirjanmerkit.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaKirjanmerkkia(); } );
        tableKirjanmerkit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaKirjanmerkkia();}); 

    	lueTiedosto("kirjat");
    	kirjaKohdalla = chooserKirjat.getSelectedObject();
    }
    
    
    /**
     * sulkee ohjelman tallentamalla
     * @return
     */
    protected boolean voikoSulkea() {
    	tallenna();
    	return true;
    }
    
    
    /**
     * lukee kirjarekisteriä kutsumalla tiedostot
     * @param nimi
     */
    private void lueTiedosto(String nimi) {
    	try {
    		kirjarekisteri.lueTiedostosta(nimi);
    		hae(0);
    	} catch (SailoException ex) {
    		hae(0);
    		Dialogs.showMessageDialog(ex.getMessage());
    	}
    }
    
    
    /**
     * tallentaa tiedostoon
     */
    private void tallenna() {
    	try {
    		kirjarekisteri.tallenna();
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog(ex.getMessage());
    	}
    }
    
    
    /**
     * avaa googlen
     */
    private void avustus() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://google.com/");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    
    /**
     * @param kirjarekisteri
     */
    public void setKirjarekisteri(Kirjarekisteri kirjarekisteri) {
    	this.kirjarekisteri = kirjarekisteri;
    	naytaKirja();
    }
    
    
    /**
     * hakee valitun kirjan
     * @param knro indeksi mikä kirja haetaan
     */
    protected void hae(int knr) {
    	int knro = knr;
    	if (knro <= 0) {
    		Kirja kohdalla = chooserKirjat.getSelectedObject();
    		if (kohdalla != null) knro = kohdalla.getTunnusNro();
    	}
    	
    	int k = cbKentat.getSelectionModel().getSelectedIndex() + apukirja.ekaKentta(); 
    	chooserKirjat.clear();
    	String ehto = hakuehto.getText();
    	if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
    	int index = 0;
    	int ci = 0;
    	Collection<Kirja> kirjat = kirjarekisteri.etsi(ehto, k);
    	for (Kirja kirja: kirjat) {
    		if (kirja.getTunnusNro() == knro) index = ci;
    		chooserKirjat.add(""+kirja.getNimi(), kirja);
    		ci++;
    	}

    	chooserKirjat.setSelectedIndex(index);

    }

    
    /**
     * näyttää kirjan tiedot kirjanmerkkeineen. 
     */
    private void naytaKirja() {
    	kirjaKohdalla = chooserKirjat.getSelectedObject();
    	if (kirjaKohdalla == null) return;
    	
    	TietueDialogController.naytaTietue(edits, kirjaKohdalla);
    	naytaKirjanmerkit(kirjaKohdalla);
    }
    
    
    private void muokkaa(int k) {
    	if (kirjaKohdalla == null) return;
    	try {
    		Kirja kirja;
    		kirja = TietueDialogController.kysyTietue(null, kirjaKohdalla.clone(), k);
    		if (kirja == null) return;
    		kirjarekisteri.korvaaTaiLisaa(kirja);
    		hae(kirja.getTunnusNro());
    	} catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 

    }
    
    
    /**
     * lisätään uusi kirja rekisteriin
     * @throws SailoException 
     */
    private void uusiKirja() throws SailoException {
    	try {
    		Kirja uusi = new Kirja();
    		uusi = TietueDialogController.kysyTietue(null, uusi, 1);
    		if (uusi == null) return;
            uusi.rekisteroi();
            kirjarekisteri.lisaa(uusi);
            hae(uusi.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
    }
    
    
    private void naytaKirjanmerkit(Kirja kirja) {
    	tableKirjanmerkit.clear();
    	if (kirja == null) return;
    	 List<Kirjanmerkki> merkit = kirjarekisteri.annaMerkit(kirja);
		 if (merkit.size() == 0) return;
		 for (Kirjanmerkki mer: merkit) 
			 naytaKirjanmerkki(mer);
    }
    
    
    private void naytaKirjanmerkki(Kirjanmerkki mer) {
    	int kenttia = mer.getKenttia();
    	String[] rivi = new String[kenttia-mer.ekaKentta()];
    	for (int i=0, k=mer.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = mer.anna(k); 
        tableKirjanmerkit.add(mer,rivi);

    }
    
    
    /**
     * lisää kirjalle uuden kirjanmerkin
     */
    private void uusiMerkki() {
    	if (kirjaKohdalla == null) return;
    	
    	Kirjanmerkki uusi = new Kirjanmerkki(kirjaKohdalla.getTunnusNro());
		uusi = TietueDialogController.kysyTietue(null, uusi, 0);
		if ( uusi == null ) return;
		uusi.rekisteroi();
		kirjarekisteri.lisaa(uusi);
		naytaKirjanmerkit(kirjaKohdalla); 
		tableKirjanmerkit.selectRow(1000);  // järjestetään viimeinen rivi valituksi

    }
    
    
    private void muokkaaKirjanmerkkia() {
    	int r = tableKirjanmerkit.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Kirjanmerkki mer = tableKirjanmerkit.getObject();
        if ( mer == null ) return;
        int k = tableKirjanmerkit.getColumnNr() + mer.ekaKentta();
        try {
            mer = TietueDialogController.kysyTietue(null, mer.clone(), k);
            if ( mer == null ) return;
            kirjarekisteri.korvaaTaiLisaa(mer); 
            naytaKirjanmerkit(kirjaKohdalla); 
            tableKirjanmerkit.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (Exception e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }

    }
    
    
    /**
     * Poistetaan kirjanmerkkitaulukosta valitulla kohdalla oleva merkki. 
     */
    private void poistaKirjanmerkki() {
        int rivi = tableKirjanmerkit.getRowNr();
        if ( rivi < 0 ) return;
        Kirjanmerkki merkki = tableKirjanmerkit.getObject();
        if ( merkki == null ) return;
        kirjarekisteri.poistaKirjanmerkki(merkki);
        naytaKirjanmerkit(kirjaKohdalla);
        int merkkeja = tableKirjanmerkit.getItems().size(); 
        if ( rivi >= merkkeja ) rivi = merkkeja -1;
        tableKirjanmerkit.getFocusModel().focus(rivi);
        tableKirjanmerkit.getSelectionModel().select(rivi);
    }


    /**
     * Poistetaan listalta valittu kirja
     */
    private void poistaKirja() {
        Kirja kirja = kirjaKohdalla;
        if ( kirja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko kirja: " + kirja.getNimi(), "Kyllä", "Ei") )
            return;
        kirjarekisteri.poista(kirja);
        int index = chooserKirjat.getSelectedIndex();
        hae(0);
        chooserKirjat.setSelectedIndex(index);
    }

    
    
    /**
     * tulostaa esitäytetyn kirjan tiedot ja kirjanmerkit
     * @param os
     * @param kirja
     */
    public void tulosta(PrintStream os, final Kirja kirja) {
    	os.println("----------------------------------------------");
    	kirja.tulosta(os);
    	os.println("----------------------------------------------");
    	try {
            List<Kirjanmerkki> merkit = kirjarekisteri.annaMerkit(kirja);
            for (Kirjanmerkki mer: merkit) 
                mer.tulosta(os);     
        } catch (Exception ex) {
        	Dialogs.showMessageDialog("Merkkien hakemisessa ongelmia! " + ex.getMessage());
        }

    }
    
    
    /**
     * Tulostaa listassa olevat jäsenet tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki kirjat");
            for (Kirja kirja: chooserKirjat.getObjects()) { 
                tulosta(os, kirja);
                os.println("\n\n");
            }
        }
    }

    

}