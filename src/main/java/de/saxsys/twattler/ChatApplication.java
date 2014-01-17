package de.saxsys.twattler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;

import static de.saxsys.twattler.ChatterConstants.*;

public class ChatApplication extends Application {


    static ClientDolphin clientDolphin;

    private TextField nameField;
    private TextArea  postField;
    private Button    newButton;

    private PresentationModel postModel;


    public ChatApplication() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ChatApplication.class.getResource("/twaddlerMain.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(getClass().getName());
//        scene.getStylesheets().add("/path/to/css");

        stage.show();


    }

    private void setupBinding() {

        clientDolphin.addModelStoreListener(TYPE_POST, new ModelStoreListener() {
            @Override
            public void modelStoreChanged(ModelStoreEvent event) {
                if (event.getType() == ModelStoreEvent.Type.ADDED) {
                    System.out.println(" wir haben den pm bekommen:  " + event.getPresentationModel().getId());
                }
                if (event.getType() == ModelStoreEvent.Type.REMOVED) {
                    System.out.println(" wir haben den pm geloescht:  " + event.getPresentationModel().getId());
                }
            }
        });

        // on select : pm per id:
        // pm = clientDolphin.getAt("meineid")
        // clientDolphin.apply(pm).to(postModel);
        // release();



    }

    private void addClientSideAction() {
        newButton.setOnAction((ActionEvent event) -> {
            clientDolphin.send(CMD_POST);
        });
    }

}

