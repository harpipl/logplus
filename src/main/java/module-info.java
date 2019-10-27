module pl.harpi.logplus {
    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;
    requires static org.mapstruct.processor;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;

    opens pl.harpi.logplus to javafx.fxml;
    opens pl.harpi.logplus.controllers to javafx.fxml;

    exports pl.harpi.logplus.services;
    exports pl.harpi.logplus.controllers;
    exports pl.harpi.logplus;
}
