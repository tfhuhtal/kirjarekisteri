package fxKirjat;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * pääohjelma
 * @author tuomas
 * @version 11 Feb 2022
 *
 */
public class FxKirjatMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("FxKirjatGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final FxKirjatGUIController fxkirjatCtrl = (FxKirjatGUIController)ldr.getController();
            
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("fxkirjat.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kirjarekisteri");
            
            primaryStage.setOnCloseRequest((event) -> {
            	if ( !fxkirjatCtrl.voikoSulkea() ) event.consume();
            });
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}