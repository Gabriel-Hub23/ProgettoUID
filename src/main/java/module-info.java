module com.pptattoo.pptatto {

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.security.crypto;
    requires itextpdf;
    requires MaterialFX;
    opens com.pptattoo.pptattoo.Model;
    opens com.pptattoo.pptattoo to javafx.fxml;
    exports com.pptattoo.pptattoo;
    exports com.pptattoo.pptattoo.Controller;
    opens com.pptattoo.pptattoo.Controller to javafx.fxml;
    opens com.pptattoo.pptattoo.View;

}