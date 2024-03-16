package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import components.SurahCard;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class RootPaneController implements Initializable {
	private Scene scene;
	@FXML
	VBox root_pane;
	@FXML
	TextField search_field;
	@FXML
	Button dark_mode_btn;
	
	private FlowPane flowPane;
	private boolean isDarkMode = false;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(root_pane == null) {
			System.out.println("No root_pane");
			return;
		}
		 
		flowPane = new FlowPane();
		ScrollPane scrollPane = new ScrollPane();

		flowPane.setAlignment(Pos.CENTER);
		scrollPane.setContent(flowPane);
		scrollPane.setFitToWidth(true);
		root_pane.getChildren().add(scrollPane);
		
		getAllSurah(flowPane);
		
		// Dark mode button function
		dark_mode_btn.setOnAction(e -> {
			if(!isDarkMode) {
				scene.getStylesheets().add("/application/resources/dark-theme.css");
				setButtonIcon(e, "/application/resources/night-mode.png");
				isDarkMode = true;
			} else {
				scene.getStylesheets().remove("/application/resources/dark-theme.css");
				setButtonIcon(e, "/application/resources/day-mode.png");
				isDarkMode = false;
			}
		});
	 }
	 

	void setButtonIcon(ActionEvent e, String url) {
		Image icon = new Image(getClass().getResource(url).toExternalForm());
		ImageView imageView = new ImageView(icon);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		Button btn = (Button) e.getTarget();
		btn.setGraphic(imageView);
	}
	
	
	 private void getAllSurah(FlowPane flowPane) {
		Connection connection = DbController.getInstance();	
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM surahs";
			ResultSet resultSet = statement.executeQuery(sql);
			
			ObservableList<Node> flowPaneChildren = flowPane.getChildren();
			
			new Thread(() -> {
				try {
					while (resultSet.next()) {
						int surahNumber = resultSet.getInt("number");
						String name = resultSet.getString("name");
						String englishName = resultSet.getString("englishName");
						String englishNameTranslation = resultSet.getString("englishNameTranslation");
						String surahType = resultSet.getString("revelationType");
						Platform.runLater(()-> {
							flowPaneChildren.add(new SurahCard(surahNumber, name, englishName + " (" + englishNameTranslation + ")", surahType));							
						});
					}
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}).start();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 public void setScene(Scene scene) {
        this.scene = scene;
	 }
}
