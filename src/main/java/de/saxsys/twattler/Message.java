package de.saxsys.twattler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {

  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty text = new SimpleStringProperty();
  private final StringProperty datum = new SimpleStringProperty();

  public StringProperty nameProperty() {
    return name;
  }

  public StringProperty textProperty() {
    return text;
  }

  public StringProperty datumProperty() {
    return datum;
  }
}
