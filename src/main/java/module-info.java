module com.mycompany.libary_system {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.libary_system to javafx.fxml;
    exports com.mycompany.libary_system;
}
