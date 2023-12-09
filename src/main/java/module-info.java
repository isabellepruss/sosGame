module sprint2.sprint2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens sprint2.sprint2 to javafx.fxml;
    exports sprint2.sprint2;
}