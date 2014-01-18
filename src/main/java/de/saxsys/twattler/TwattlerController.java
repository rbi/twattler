package de.saxsys.twattler;

import groovy.util.Eval;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.opendolphin.binding.Converter;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

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

    private final ListProperty<Message> messages = new SimpleListProperty<>(FXCollections.<Message>observableArrayList());
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
    private TextField name;
    @FXML
    private TextArea newMessage;
    @FXML
    private TableView<Message> oldMessages;
    @FXML
    private Button send;
    @FXML
    private TableColumn<Message, String> tableDatum;
    @FXML
    private TableColumn<Message, String> tableName;
    @FXML
    private TableColumn<Message, String> tableMessage;
    @FXML
    private ToggleButton toggleEmoticons;
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
        assert tableMessage != null : "fx:id=\"tableMessage\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
        assert newMessage != null : "fx:id=\"newMessage\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
        assert oldMessages != null : "fx:id=\"oldMessages\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
        assert send != null : "fx:id=\"send\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
        assert tableDatum != null : "fx:id=\"tableDatum\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
        assert tableName != null : "fx:id=\"tableName\" was not injected: check your FXML file 'twaddlerMain.fxml'.";

        this.setTableModel(messages);

        ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
        ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
        ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
        ClientPresentationModel postModel = ChatApplication.clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute,
                postAttribute, dateAttribute);

        emoticonSelectorController.setEmoticonPressedListener((emoticon) -> newMessage.appendText(emoticon));

        ChatApplication.clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
            @Override
            public void onFinished(List<ClientPresentationModel> presentationModels) {
                Logger.getLogger(TwattlerController.class.getName()).log(Level.INFO,
                        presentationModels.size() + " Messages bekommen");
                // TODO visualisieren, dass wir die initialen Daten haben.
                longPoll();
                name.requestFocus();
            }
        });

        bind("text").of(name).to(ATTR_NAME).of(postModel, withRelease);
        bind(ATTR_NAME).of(postModel).to("text").of(name);

        bind("text").of(newMessage).to(ATTR_MESSAGE).of(postModel, withRelease);
        bind(ATTR_MESSAGE).of(postModel).to("text").of(newMessage);

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

        // CTRL + ENTER
        newMessage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (KeyCode.ENTER.equals(event.getCode()) && event.isControlDown()) {
                sendMessage();
            }
        });

        // send message
        send.setOnAction((ActionEvent event) -> sendMessage());

        initColumnRenderer();
        emoticonSelectorController.visibleProperty().bind(toggleEmoticons.selectedProperty());
    }

    private void initColumnRenderer() {
        tableName.setCellValueFactory(new PropertyValueFactory<Message, String>("name"));
        tableMessage.setCellValueFactory(new PropertyValueFactory<Message, String>("text"));
        tableDatum.setCellValueFactory(new PropertyValueFactory<Message, String>("datum"));
    }

    private void sendMessage() {
        if (newMessage.getText().isEmpty())
            return;

        ChatApplication.clientDolphin.send(CMD_POST);
        release();
        newMessage.requestFocus();
    }

    public void setTableModel(ListProperty<Message> model) {

        oldMessages.setItems(model);
    }

    private void onPostRemoved(PresentationModel presentationModel) {


    }

    private void onPostAdded(PresentationModel presentationModel) {

        Message aMessage = new Message();

        bind(ATTR_NAME).of(presentationModel).to("name").of(aMessage);
        bind(ATTR_MESSAGE).of(presentationModel).to("text").of(aMessage);
        bind(ATTR_DATE).of(presentationModel).to("datum").of(aMessage);

        messages.add(aMessage);
    }
}
