package de.saxsys.twattler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opendolphin.core.client.ClientDolphin;

public class ChatApplication extends Application {

  static ClientDolphin clientDolphin;

  public ChatApplication() {

  }

  @Override
  public void start(Stage stage) throws Exception {

    FXMLLoader fxloader = new FXMLLoader();
    fxloader.setLocation(ChatApplication.class.getResource("/twaddlerChat.fxml"));
    Parent root = fxloader.load();
    TwattlerController controller = fxloader.<TwattlerController> getController();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle(getClass().getName());
    // scene.getStylesheets().add("/path/to/css");
    setupBinding();

    stage.show();
  }

  private void setupBinding() {

    // on select : pm per id:
    // pm = clientDolphin.getAt("meineid")
    // clientDolphin.apply(pm).to(postModel);
    // release();
  }
}
