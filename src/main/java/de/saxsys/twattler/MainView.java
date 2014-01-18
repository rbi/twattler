package de.saxsys.twattler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class MainView extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    SplitPane page = (SplitPane) FXMLLoader.load(MainView.class.getResource("/twaddlerMain.fxml"));
    Scene scene = new Scene(page);
    stage.setScene(scene);
    stage.setTitle("Twaddler");
    stage.show();
  }

  public static void main(final String args[]) {
    launch(args);
  }
}
