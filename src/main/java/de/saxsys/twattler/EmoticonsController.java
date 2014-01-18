package de.saxsys.twattler;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class EmoticonsController {

    @FXML
    private GridPane emoticonGrid;
	
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

	private EmoticonPressedListener listener;

	@FXML
	public void initialize() {
		
		
		
		biggrin.setOnAction((e) -> informListener(EmoticonPicker.EMOTION_BIGGRIN));
		smile.setOnAction((e) -> informListener(EmoticonPicker.EMOTION_SMILE));
		confused.setOnAction((e) -> informListener(EmoticonPicker.EMOTION_CONFUSED));
		mad.setOnAction((e) -> informListener(EmoticonPicker.EMOTION_MAD));
		sad.setOnAction((e) -> informListener(EmoticonPicker.EMOTION_SAD));// worried
		eek.setOnAction((e) -> informListener(EmoticonPicker.EMOTION_EEK));// face with open mouth
	}

	/**
	 * Registers a listener that is informed of emoticons the user pressed.
	 * @param listener
	 */
	public void setEmoticonPressedListener(final EmoticonPressedListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Whether the emoticons panel is visible or not.
	 *  
	 * @return the property
	 */
	public BooleanProperty visibleProperty() {
		return emoticonGrid.visibleProperty();
	}

	private void informListener(final String emoticon) {
		if(listener != null) {
			listener.emoticonPressed(emoticon);
		}
	}
	
	public interface EmoticonPressedListener {
		void emoticonPressed(final String emoticon);
	}
}
