module com.example.convexhullvisualization {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.convexhullvisualization to javafx.fxml;
    exports com.example.convexhullvisualization;
    exports com.example.convexhullvisualization.view;
    opens com.example.convexhullvisualization.view to javafx.fxml;
}