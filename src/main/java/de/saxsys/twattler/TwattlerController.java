package de.saxsys.twattler;

import groovy.util.Eval;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.opendolphin.binding.Converter;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.saxsys.twattler.ChatterConstants.*;
import static org.opendolphin.binding.JFXBinder.bind;

/**
 * Created by michael.thiele on 17.01.14.
 */
public class TwattlerController {

    private ClientPresentationModel postModel;

    private final Converter withRelease = value -> {
        release();
        return value;
    };
    private boolean channelBlocked = false;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ToggleButton emoticonButton;
	@FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messages;
    @FXML
    private TextField myName;
    @FXML
    private TextArea myMessage;
    @FXML
    private EmoticonsController emoticonSelectorController;

    private void release() {
        if (!channelBlocked)
            return; // avoid too many unblocks
        channelBlocked = false;
        String url = "https://klondike.canoo.com/dolphin-grails/chatter/release";
        String result = Eval.x(url, "x.toURL().text").toString();
        System.out.println("release result = " + result);
    }

    private void longPoll() {
        channelBlocked = true;
        ChatApplication.clientDolphin.send(CMD_POLL, new OnFinishedHandlerAdapter() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
                longPoll();
            }
        });
    }

    @FXML
    public void initialize() {
        ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
        ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
        ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
        postModel = ChatApplication.clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute,
                postAttribute, dateAttribute);

        ChatApplication.clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
                Logger.getLogger(TwattlerController.class.getName()).log(Level.INFO,
                        presentationModels.size() + " Messages bekommen");
                // TODO visualisieren, dass wir die initialen Daten haben.
                longPoll();
                myName.requestFocus();
                myName.selectAll();
                scrollToBottom();
            }
        });

        bind("text").of(myName).to(ATTR_NAME).of(postModel, withRelease);
        bind(ATTR_NAME).of(postModel).to("text").of(myName);

        bind("text").of(myMessage).to(ATTR_MESSAGE).of(postModel, withRelease);
        bind(ATTR_MESSAGE).of(postModel).to("text").of(myMessage);

        ChatApplication.clientDolphin.addModelStoreListener(TYPE_POST, new ModelStoreListener() {
            @Override
            public void modelStoreChanged(ModelStoreEvent event) {
                if (event.getType() == ModelStoreEvent.Type.ADDED) {
                    System.out.println(" wir haben den pm bekommen:  " + event.getPresentationModel().getId());
                    onPostAdded(event.getPresentationModel());
                }
                if (event.getType() == ModelStoreEvent.Type.REMOVED) {
                    System.out.println(" wir haben den pm geloescht:  " + event.getPresentationModel().getId());
                    onPostRemoved(event.getPresentationModel());
                }
            }
        });
        initializeSendMessageEventHandler();


        // send message
        // send.setOnAction((ActionEvent event) -> sendMessage());

        //emoticonSelectorController.visibleProperty().bind(toggleEmoticons.selectedProperty());

    }

    private void initializeSendMessageEventHandler() {
        myMessage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (KeyCode.ENTER.equals(event.getCode()) && event.isControlDown()) {
                sendMessage();
            }
        });

        // send message
        // send.setOnAction((ActionEvent event) -> sendMessage());

        emoticonSelectorController.setEmoticonPressedListener((emoticon) -> myMessage.appendText(emoticon));
        emoticonSelectorController.visibleProperty().bind(emoticonButton.selectedProperty());
    }

    private void sendMessage() {
        if (myMessage.getText().isEmpty())
            return;

        ChatApplication.clientDolphin.send(CMD_POST);
        release();
        myMessage.requestFocus();
    }

    private void onPostRemoved(PresentationModel presentationModel) {


    }

    private void onPostAdded(final PresentationModel presentationModel) {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(TwattlerController.class.getResource("/twaddlerMessage.fxml"));
        try {
            fxmlLoader.load();
            MessageController messageController = fxmlLoader.getController();
            messageController.setPresentationModel(presentationModel, () -> {
                ChatApplication.clientDolphin.apply((ClientPresentationModel) presentationModel).to(postModel);
                release();
            });
            Parent message = fxmlLoader.getRoot();
            messages.getChildren().add(message);
            scrollToBottom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        Platform.runLater(() -> scrollPane.setVvalue(scrollPane.getVmax()));
    }
}
