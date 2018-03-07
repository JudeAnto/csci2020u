
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
//import sun.util.resources.cldr.lag.CalendarData_lag_TZ;

public class Main extends Application {
    private TableView<TestFile> files;
    private TextField prec;
    private TextField acc;

    public static void main(String[] args) { Application.launch(args); }


    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("Spam Filter");

        BorderPane layout = new BorderPane();

        //Create the table of student records
        TableColumn<TestFile, String> idCol = new TableColumn<>("File");
        idCol.setPrefWidth(300);
        idCol.setCellValueFactory(new PropertyValueFactory<>("filename"));

        TableColumn<TestFile, Float> assCol = new TableColumn<>("Actual Class");
        assCol.setPrefWidth(100);
        assCol.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        TableColumn<TestFile, String> mtCol = new TableColumn<>("Spam Probability");
        mtCol.setPrefWidth(300);
        mtCol.setCellValueFactory(new PropertyValueFactory<>("spamProbRounded"));

        
        this.files = new TableView<>();
        this.files.getColumns().add(idCol);
        this.files.getColumns().add(assCol);
        this.files.getColumns().add(mtCol);
        
        this.acc = new TextField();
        this.acc.setPromptText("a");

        this.prec = new TextField();
        this.prec.setPromptText("a");
        
        GridPane bottom = new GridPane();
        bottom.setPadding(new Insets(10));
        bottom.setHgap(10);
        bottom.setVgap(10);

        bottom.add(new Label("Accuracy: "), 0, 0);
        bottom.add(acc, 1,0 );

        bottom.add(new Label("Precision:"), 0, 1);
        bottom.add(prec, 1, 1);

        layout.setCenter(files);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.files.setItems(SpamFilter.getAllFiles());
    }

}