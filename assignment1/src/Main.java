//libraries
import java.io.File;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


//application main class
public class Main extends Application {
    private TableView<TestFile> files;
    private TextField prec;
    private TextField acc;
    
    public static void main(String[] args) { Application.launch(args); }


    @Override public void start(Stage primaryStage) {
    	//opens directory chooser and gets path
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setInitialDirectory(new File("."));
    	File mainDirectory = directoryChooser.showDialog(primaryStage);
    	
    	SpamFilter.FILENAME = mainDirectory.getPath();
    	
        primaryStage.setTitle("----------------------SPAM FILTER-----------------------");
        
        BorderPane layout = new BorderPane();

        //Create the table of file records
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
        
        //observable list to iterate files
        ObservableList<TestFile> fb = SpamFilter.getAllFiles();
        
        //monitors accuracy
        float correctGuesses = 0.0f;
    	for (TestFile f: fb) {
    		//cheks for ham or spam and increases guesses
    		if(f.getActualClass().equals("Ham") && f.getSpamProbability() < 1.0) {
    			correctGuesses += 1.0f;
    		} else if(f.getActualClass().equals("Spam") && f.getSpamProbability() >= 1.0) {
    			correctGuesses += 1.0f;
    		}
    	}
    	//
        this.acc = new TextField();
        this.acc.setText(String.valueOf(correctGuesses/2800.0f));

        this.prec = new TextField();
        
        //gridpane addition at the bottom
        GridPane bottom = new GridPane();
        bottom.setPadding(new Insets(10));
        bottom.setHgap(10);
        bottom.setVgap(10);

        bottom.add(new Label("Accuracy: "), 0, 0);
        bottom.add(acc, 1,0 );

        bottom.add(new Label("Precision:"), 0, 1);
        bottom.add(prec, 1, 1);
        
        //layout 
        layout.setCenter(files);
        layout.setBottom(bottom);
        
        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
 
        this.files.setItems(fb);

    }

}