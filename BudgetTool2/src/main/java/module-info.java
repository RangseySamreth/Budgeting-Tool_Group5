module com.budget.tool{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.budget.tool to javafx.fxml;
    exports com.budget.tool;
    exports com.budget.tool.Controller;
    exports com.budget.tool.Controller.Admin;
    exports com.budget.tool.Controller.Client;
    exports com.budget.tool.Models;
    exports com.budget.tool.Views;
}