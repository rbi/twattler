package de.saxsys.twattler;

import static de.saxsys.twattler.ChatterConstants.TYPE_POST;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientDolphin;

public class ChatApplication extends Application {

  static ClientDolphin clientDolphin;

  private final ListProperty<Message> messages = new SimpleListProperty<>(FXCollections.<Message> observableArrayList());

  public ChatApplication() {

  }

  @Override
  public void start(Stage stage) throws Exception {

    FXMLLoader fxloader = new FXMLLoader();
    fxloader.setLocation(ChatApplication.class.getResource("/twaddlerMain.fxml"));
    Parent root = fxloader.load();
    TwattlerController controller = fxloader.<TwattlerController> getController();
    controller.setTableModel(messages);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle(getClass().getName());
    // scene.getStylesheets().add("/path/to/css");
    setupBinding();

    stage.show();
  }

  private void setupBinding() {

    clientDolphin.addModelStoreListener(TYPE_POST, new ModelStoreListener() {
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

      private void onPostRemoved(PresentationModel presentationModel) {

        
      }

      private void onPostAdded(PresentationModel presentationModel) {

        String name = (String) presentationModel.getAt(ChatterConstants.ATTR_NAME).getValue();
        String text = (String) presentationModel.getAt(ChatterConstants.ATTR_MESSAGE).getValue();
        String datum = (String) presentationModel.getAt(ChatterConstants.ATTR_DATE).getValue();

        Message newMessage = new Message();
        newMessage.nameProperty().set(name);
        newMessage.textProperty().set(text);
        newMessage.datumProperty().set(datum);

        messages.add(newMessage);
      }
    });

    // on select : pm per id:
    // pm = clientDolphin.getAt("meineid")
    // clientDolphin.apply(pm).to(postModel);
    // release();
  }
}
