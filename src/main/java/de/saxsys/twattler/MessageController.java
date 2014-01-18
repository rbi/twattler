package de.saxsys.twattler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.opendolphin.core.PresentationModel;

import java.net.URL;
import java.util.ResourceBundle;

import static de.saxsys.twattler.ChatterConstants.ATTR_DATE;
import static de.saxsys.twattler.ChatterConstants.ATTR_MESSAGE;
import static de.saxsys.twattler.ChatterConstants.ATTR_NAME;
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
    private Label messageText;
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

        bind(ATTR_NAME).of(presentationModel).to("text").of(messageName);
        bind(ATTR_MESSAGE).of(presentationModel).to("text").of(messageText);
        bind(ATTR_DATE).of(presentationModel).to("text").of(messageDate);
    }
}
