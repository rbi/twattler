package de.saxsys.twattler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class EmoticonsController {

    @FXML
    private Button biggrin;

    @FXML
    private Button smile;
    
    @FXML
    private Button confused;

    @FXML
    private Button mad;

    @FXML
    private Button sad;

    @FXML
    private Button eek;
    
    private StringProperty lastSelectedEmoticon = new SimpleStringProperty("");
    
    public EmoticonsController() {
    	biggrin.setOnAction((e)-> lastSelectedEmoticon.set("😀"));
    	smile.setOnAction((e)-> lastSelectedEmoticon.set("😃"));
    	confused.setOnAction((e)-> lastSelectedEmoticon.set("😕"));
    	mad.setOnAction((e)-> lastSelectedEmoticon.set("😠"));
    	sad.setOnAction((e)-> lastSelectedEmoticon.set("😟"));// worried
    	eek.setOnAction((e)-> lastSelectedEmoticon.set("😮"));// face with open mouth
    }

    /**
     * Contains the emoticon that was selected last.
     * 
     * @return the property containing the emoticon.
     */
	public StringProperty getLastSelectedEmoticon() {
		return lastSelectedEmoticon;
	}
}
