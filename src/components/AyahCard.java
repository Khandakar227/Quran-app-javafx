package components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AyahCard extends VBox {
	private int surahNumber, ayahNumber;
	private String arabicText, translatedText;
	
	@FXML
	TextFlow arabic_text;
	@FXML
	Label translated_text, number;
	
	public AyahCard(int surahNumber, int ayahNumber, String arabicText, String translatedText) {
		this.surahNumber = surahNumber;
		this.ayahNumber = ayahNumber;
		this.arabicText = arabicText;
		this.translatedText = translatedText;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/resources/components/AyahCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(AyahCard.this);
		
		try {			
			fxmlLoader.load();
		} catch (Exception e) {
			System.out.println("[Ayah Card]: "+ e);
		}
	}

	
	public void initialize() {
		try {
			Text arabic = (Text) arabic_text.getChildren().get(0);
			arabic.setText(arabicText);
			translated_text.setText(translatedText);
			number.setText(Integer.toString(surahNumber) + ":" + Integer.toString(ayahNumber));
		} catch (Exception e) {
			System.out.println("[Ayah Card initialize]: "+ e);
		}
	}
}
