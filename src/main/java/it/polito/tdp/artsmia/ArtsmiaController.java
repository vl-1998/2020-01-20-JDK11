package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;
import it.polito.tdp.artsmia.model.Adiacenze;
import it.polito.tdp.artsmia.model.Autore;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;
	private ArtsmiaDAO dao = new ArtsmiaDAO();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	
    	String role = boxRuolo.getValue();
    	List<Adiacenze> adiacenze = this.model.getAdiacenze(role);
    	if (adiacenze==null) {
    		txtResult.appendText("Devi prima creare il grafo");
    	}
    	Collections.sort(adiacenze);
    	
    	/*String res = "";
    	for (Adiacenze a : adiacenze) {
    		if (res==null) {
    			res=a.toString();
    		} else {
    			res = res +"\n" + a.toString();
    		}
    		txtResult.appendText(res);
    	}*/
    	
    	for (Adiacenze a : adiacenze) {
    		txtResult.appendText(String.format("(%d,%d) = %d\n", a.getA1().getAutor_id(), a.getA2().getAutor_id(), a.getPeso()));
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	try {
    		int author_id = Integer.parseInt(txtArtista.getText());
    		Autore a = new Autore(author_id);
    		if (!this.model.getVertici().contains(a)) {
    			txtResult.appendText("Inserire codice di artista valido");
				return;
    		}
    		
    		for (Autore c : this.model.trovaPercorso(author_id)) {
    			txtResult.appendText(c.getAutor_id()+"\n");
    		}
    		
    	} catch (NumberFormatException e) {
			txtResult.appendText("Inserire codice artista numerico.");
			return;
		}
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String role = boxRuolo.getValue();
    	if (role == null) {
    		txtResult.appendText("Inserire un ruolo");
    		return;
    	}
    	this.model.creaGrafo(role);
    	txtResult.appendText("# vertici: "+this.model.vertexNumber()+" # archi: "+this.model.edgeNumber()+"\n\n");
    	
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(dao.elencoRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
