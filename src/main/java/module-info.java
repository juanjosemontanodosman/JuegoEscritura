module com.example.fastwritinggame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fastwritinggame to javafx.fxml;
    exports com.example.fastwritinggame;
}