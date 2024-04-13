package components;

import application.SurahModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SurahName extends VBox {
	@FXML
	Label arabic_name, translated_name;
	
	String surahNameArabic, surahNameTransliterated, translated;
	int surahNumber;
	
	public SurahName(SurahModel surah) {
		this.surahNumber = surah.number;
		this.surahNameArabic = surah.name;
		this.surahNameTransliterated = surah.englishName;
		this.translated = surah.englishNameTranslation;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/resources/components/SurahName.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(SurahName.this);
		
		try {			
			fxmlLoader.load();
		} catch (Exception e) {
			System.out.println("[Surah Name]: "+ e);
		}
	}
	
	public SurahName(int surahNumber, String surahNameArabic, String surahNameTransliterated, String translated) {
		this.surahNumber = surahNumber;
		this.surahNameArabic = surahNameArabic;
		this.surahNameTransliterated = surahNameTransliterated;
		this.translated = translated;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/resources/components/SurahName.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(SurahName.this);
		
		try {			
			fxmlLoader.load();
		} catch (Exception e) {
			System.out.println("[Surah Name]: "+ e);
		}
	}
	
	public void initialize() {
		arabic_name.setText(surahNameArabic);
		translated_name.setText(
				Integer.toString(surahNumber) + ". " + surahNameTransliterated + "(" + translated +")"
				);
	}
}
