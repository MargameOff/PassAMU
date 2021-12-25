module com.passamu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    opens com.passamu to javafx.fxml;
    opens com.passamu.xml to com.fasterxml.jackson.databind;
    exports com.passamu;
    exports com.passamu.xml;
    exports com.passamu.models;
    exports com.passamu.controllers;
    opens com.passamu.controllers to javafx.fxml;
}