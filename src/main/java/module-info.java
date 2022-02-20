module hobbypos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires escpos.coffee;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires com.fazecast.jSerialComm;


    exports hobbypos.ralphfx;
    opens hobbypos.ralphfx.model to javafx.base;
    opens hobbypos.ralphfx;

}