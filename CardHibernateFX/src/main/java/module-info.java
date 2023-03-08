module com.iaroslaveremeev {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.prefs;

    opens com.iaroslaveremeev to javafx.fxml;
    exports com.iaroslaveremeev;
    exports com.iaroslaveremeev.model;
    exports com.iaroslaveremeev.dto;
    exports com.iaroslaveremeev.controllers;
    exports com.iaroslaveremeev.repository;
    exports com.iaroslaveremeev.util;
    opens com.iaroslaveremeev.controllers to javafx.fxml;
}
