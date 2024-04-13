package application;

public class SurahModel {
	public String
				name,
				englishName,
				englishNameTranslation,
				revelationType;
	public int number, numberOfAyahs;
	
	public SurahModel(int number, int numberOfAyahs, String name, String englishName, String englishNameTranslation, String revelationType) {
		this.number = number;
		this.name = name;
		this.englishName = englishName;
		this.englishNameTranslation = englishNameTranslation;
		this.numberOfAyahs = numberOfAyahs;
		this.revelationType = revelationType;
	}
}
