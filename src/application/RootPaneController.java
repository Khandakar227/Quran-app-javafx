package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import components.SurahCard;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class RootPaneController {
	@FXML
	VBox root_pane;
	@FXML
	TextField search_field;
	
	private FlowPane flowPane;
	private int iterCount = 0;

	 public void initialize() {
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
}
