module com.mycompany.libary_system {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.library_system.Controllers to javafx.fxml;
    exports com.mycompany.library_system.Controllers;
    exports com.mycompany.library_system;
}
