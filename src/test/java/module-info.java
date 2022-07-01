module com.example.gui_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;

    exports com.gui;
    exports com.gui.glowingCircle;
    opens com.gui.glowingCircle to javafx.fxml;
    exports com.gui.model;
    exports com.gui.network;
    exports com.gui.messages.fromServer;
    exports com.gui.messages.fromClient;

    opens com.gui to javafx.fxml;
    opens com.gui.model to javafx.fxml;
    opens com.gui.messages.fromClient to com.google.gson;
    opens com.gui.messages.fromServer to com.google.gson;
    opens com.gui.messages to com.google.gson;
}