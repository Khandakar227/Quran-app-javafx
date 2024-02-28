package application;
	
import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
//			Font.loadFont(getClass().getResourceAsStream("resources/UthmanicScriptHAFS.otf"), 24);
			Connection connection = DbController.getInstance();			
			Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
			Scene scene = new Scene(root,500,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
