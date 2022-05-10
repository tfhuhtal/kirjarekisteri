package fxKirjat;

import java.util.List;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import kirjarekisteri.*;

/**
 * näyttää dialogissa kirjan edistymisen kuvaajan.
 * @author tuomas
 * @version 6 Apr 2022
 *
 */
public class LineChartController implements ModalControllerInterface<Kirja>{

    @FXML private AreaChart graph;
    private Kirja kirjaKohdalla;
    private Kirjarekisteri rekisteri;
    
	
	@Override
	public Kirja getResult() {
		return null;
	}


	@Override
	public void handleShown() {
		nayta();
	}


	@Override
	public void setDefault(Kirja oletus) {
		kirjaKohdalla = oletus;
	}
	
	
	public void handleOK() {
		ModalController.closeStage(graph);
	}
	
	
	/**
	 * alustaa kirjareksiterin jotta sitä voidaan käyttää
	 * @param kirjarekisteri
	 */
	public void setRekisteri(Kirjarekisteri kirjarekisteri) {
		this.rekisteri = kirjarekisteri;
	}
	
	
	/**
	 * asettaa kuvaajalle arvot ja näyttää sen.
	 */
	public void nayta() {
		@SuppressWarnings("rawtypes")
		final XYChart.Series<Number, Number> series = new XYChart.Series();
		
		List<Kirjanmerkki> merkit = rekisteri.annaMerkit(kirjaKohdalla);
		if (merkit.size() == 0) return;
		int i = 0;
		graph.setTitle(kirjaKohdalla.getNimi());
		for (Kirjanmerkki mer: merkit) {
			series.getData().add(new XYChart.Data(i++, mer.getSivulla()));
		}
		graph.getData().addAll(series);
	}
	
	
	/**
	 * avaa dialogin kuvaajan näyttämiselle
	 * @param modalityStage 
	 * @param kirja joka tuodaan
	 * @param kirjarekisteri rekisteri joka tuodaan 
	 * @return ikkunan
	 */
	public static Kirja avaa(Stage modalityStage ,Kirja kirja, Kirjarekisteri kirjarekisteri) {
		return ModalController.showModal(LineChartController.class.getResource("GraphView.fxml"),
				"edistyminen" , modalityStage, kirja, ctrl -> ((LineChartController) ctrl).setRekisteri(kirjarekisteri));
	}

}
