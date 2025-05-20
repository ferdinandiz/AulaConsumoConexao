module com.javabd.aulaconsumoconexao {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;

    opens com.javabd.aulaconsumoconexao.model to com.fasterxml.jackson.databind;

    opens com.javabd.aulaconsumoconexao to javafx.fxml;
    exports com.javabd.aulaconsumoconexao;
    exports com.javabd.aulaconsumoconexao.controller;
    opens com.javabd.aulaconsumoconexao.controller to javafx.fxml;
}