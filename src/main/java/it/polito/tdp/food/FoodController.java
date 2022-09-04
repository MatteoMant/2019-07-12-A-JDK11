/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCalories;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	boxFood.getItems().clear();
    	
    	try {
    		int nPorzioni = Integer.parseInt(txtPorzioni.getText());
    		
    		this.model.creaGrafo(nPorzioni);
    		
    		txtResult.appendText("Grafo creato!\n");
        	txtResult.appendText("# Vertici " + this.model.getNumVertici() + "\n");
        	txtResult.appendText("# Archi " + this.model.getNumArchi() + "\n");
        	
        	// Dopo aver creato il grafo possiamo popolare il menu a tendina dei cibi
        	boxFood.getItems().addAll(this.model.getVertici());
        	
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Per favore inserire un numero di calorie valido!\n");
    		return;
    	}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	
    	Food f = boxFood.getValue();
    	if (f == null) {
    		txtResult.appendText("Per favore selezionare un cibo dalla tendina!\n");
    		return;
    	}
    	
    	List<FoodCalories> adiacenti = this.model.getAdiacentiCalorieMassime(f);
    	txtResult.appendText("I cibi con le calorie congiunte massime a '" + f + "' sono: \n");
    	
    	for (int i = 0; i < 5 && i < adiacenti.size(); i++) {
    		txtResult.appendText(adiacenti.get(i).getFood() + " " + adiacenti.get(i).getCalories() + "\n");
    	}
    
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	
    	Food f = boxFood.getValue();
    	if (f == null) {
    		txtResult.appendText("Per favore selezionare un cibo dalla tendina!\n");
    		return;
    	}
    	
    	try {
    		int K = Integer.parseInt(txtK.getText());
    		
    		if (K < 1 || K > 10) {
    			txtResult.appendText("Devi inserire un numero di stazioni tra 1 e 10!\n");
        		return;
    		}
    		
    		String messaggio = model.simula(f, K);
    		txtResult.appendText(messaggio);
    		
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Inserire un numero valido di stazioni di lavoro!\n");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
