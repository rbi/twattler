package de.saxsys.twattler;

import groovy.util.Eval;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.opendolphin.binding.Converter;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static de.saxsys.twattler.ChatterConstants.*;
import static org.opendolphin.binding.JFXBinder.bind;

/**
 * Created by michael.thiele on 17.01.14.
 */
public class TwattlerController {

    private boolean channelBlocked = false;
    private void release() {
        if (!channelBlocked) return; // avoid too many unblocks
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

    private final Converter withRelease = value -> {
        release();
        return value;
    };



        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private TableColumn<?, ?> tableMessage;

        @FXML
        private TextField name;

        @FXML
        private TextArea newMessage;

        @FXML
        private TableView<?> oldMessages;

        @FXML
        private Button send;

        @FXML
        private TableColumn<?, ?> tableDatum;

        @FXML
        private TableColumn<?, ?> tableName;

        @FXML
        public void initialize() {
            assert tableMessage != null : "fx:id=\"tableMessage\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
            assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
            assert newMessage != null : "fx:id=\"newMessage\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
            assert oldMessages != null : "fx:id=\"oldMessages\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
            assert send != null : "fx:id=\"send\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
            assert tableDatum != null : "fx:id=\"tableDatum\" was not injected: check your FXML file 'twaddlerMain.fxml'.";
            assert tableName != null : "fx:id=\"tableName\" was not injected: check your FXML file 'twaddlerMain.fxml'.";

            ClientAttribute nameAttribute = new ClientAttribute(ATTR_NAME, "");
            ClientAttribute postAttribute = new ClientAttribute(ATTR_MESSAGE, "");
            ClientAttribute dateAttribute = new ClientAttribute(ATTR_DATE, "");
            ClientPresentationModel postModel = ChatApplication.clientDolphin.presentationModel(PM_ID_INPUT, nameAttribute, postAttribute, dateAttribute);

            ChatApplication.clientDolphin.send(CMD_INIT, new OnFinishedHandlerAdapter() {
                @Override
                public void onFinished(List<ClientPresentationModel> presentationModels) {
                    System.out.println("" + presentationModels.size() + "bekommen");
                    // visualisieren, dass wir die initialen Daten haben.

                    longPoll();

                }
            });

            bind("text").of(name).to(ATTR_NAME).of(postModel, withRelease);
            bind(ATTR_NAME).of(postModel).to("text").of(name);

            bind("text").of(newMessage).to(ATTR_MESSAGE).of(postModel, withRelease);
            bind(ATTR_MESSAGE).of(postModel).to("text").of(newMessage);
        }
    }

