package components;


import application.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class SurahCard extends AnchorPane {
	private int surahNumber, numberOfAyahs;
	private String arabicText, translatedText, surahType;
	@FXML
	private Label surah_number, arabic_text, translated_text, surah_type;
	
	public SurahCard(int surahNumber, int numberOfAyahs, String arabicText, String translatedText, String surahType) {
		this.surahNumber = surahNumber;
		this.arabicText = arabicText;
		this.translatedText = translatedText;
		this.surahType = surahType;
		this.numberOfAyahs = numberOfAyahs;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/resources/components/SurahCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(SurahCard.this);
		
		try {			
			fxmlLoader.load();
			// Set Navigation for click
			setNavigationHandler();
		} catch (Exception e) {
			System.out.println("[Surah Card]: "+ e);
		}
	}
	
	public void initialize() {
		try {
			surah_number.setText(Integer.toString(surahNumber) + ". ");
			arabic_text.setText(arabicText);
			translated_text.setText(translatedText);
			surah_type.setText(surahType);			
		} catch (Exception e) {
			System.out.println("[Surah Card initialize]: "+ e);
		}
	}
	
	private void setNavigationHandler() {
		this.setOnMouseClicked(e-> {
			System.out.println(surahNumber);
			NavigationController.goToSurah(surahNumber, 1, 20, numberOfAyahs);
		});
	}
}
