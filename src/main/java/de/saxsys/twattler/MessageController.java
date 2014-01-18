package de.saxsys.twattler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.opendolphin.core.PresentationModel;

import java.net.URL;
import java.util.ResourceBundle;

import static de.saxsys.twattler.ChatterConstants.*;
import static org.opendolphin.binding.JFXBinder.bind;

/**
 * Created by michael.thiele on 18.01.14.
 */
public class MessageController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Label messageName;
    @FXML
    private Pane messageText;
    @FXML
    private Label messageDate;
    private PresentationModel presentationModel;

    @FXML
    void initialize() {
        assert messageName != null : "fx:id=\"messageName\" was not injected: check your FXML file 'twaddlerMessage.fxml'.";
        assert messageText != null : "fx:id=\"messageText\" was not injected: check your FXML file 'twaddlerMessage.fxml'.";
        assert messageDate != null : "fx:id=\"messageDate\" was not injected: check your FXML file 'twaddlerMessage.fxml'.";

    }

    public void setPresentationModel(PresentationModel presentationModel) {
        this.presentationModel = presentationModel;

        TextFlow textFlow = new TextFlow();
        messageText.getChildren().add(textFlow);

        StringProperty messageProperty = new SimpleStringProperty();
        messageProperty.addListener((obs, oldVal, newVal) ->{
            textFlow.getChildren().clear();
            new EmoticonPicker().checkText(textFlow, newVal);
        });

        bind(ATTR_NAME).of(presentationModel).to("text").of(messageName);
        bind(ATTR_MESSAGE).of(presentationModel).to("value").of(messageProperty);
        bind(ATTR_DATE).of(presentationModel).to("text").of(messageDate);

    }
}
