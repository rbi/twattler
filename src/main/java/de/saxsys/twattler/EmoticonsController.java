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

  private final StringProperty lastSelectedEmoticon = new SimpleStringProperty("");

  public EmoticonsController() {
    biggrin.setOnAction((e) -> lastSelectedEmoticon.set(EmoticonPicker.EMOTION_BIGGRIN));
    smile.setOnAction((e) -> lastSelectedEmoticon.set(EmoticonPicker.EMOTION_SMILE));
    confused.setOnAction((e) -> lastSelectedEmoticon.set(EmoticonPicker.EMOTION_CONFUSED));
    mad.setOnAction((e) -> lastSelectedEmoticon.set(EmoticonPicker.EMOTION_MAD));
    sad.setOnAction((e) -> lastSelectedEmoticon.set(EmoticonPicker.EMOTION_SAD));// worried
    eek.setOnAction((e) -> lastSelectedEmoticon.set(EmoticonPicker.EMOTION_EEK));// face with open mouth
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
