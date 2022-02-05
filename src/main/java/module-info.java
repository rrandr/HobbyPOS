module hobbypos.hobbyktvpos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;


    exports hobbypos.ralphfx;
    opens hobbypos.ralphfx.model to javafx.base;
    opens hobbypos.ralphfx;
}