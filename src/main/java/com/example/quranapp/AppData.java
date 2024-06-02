package com.example.quranapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;


public class AppData {
	public static String currentPage = "Home";
	public static int currentSurahNumber = 0,
					  fromAyahNumber = 1,
					  toAyahNumber = 20,
					  numberOfAyahs;
	public static String searchedKeyword = "";
	public static SurahModel currentSurah;
	public static HashMap<String, String> translationMap = new HashMap<>();
	
	public static SurahModel getSurah() {
		Connection connection = DbController.getInstance();	
		try {
			String sql = "SELECT * FROM surahs where number = ?";
			PreparedStatement  statement = connection.prepareStatement(sql);
			statement.setInt(1, currentSurahNumber);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				int surahNumber = resultSet.getInt("number");
				int numberOfAyahs = resultSet.getInt("numberOfAyahs");
				String name = resultSet.getString("name");
				String englishName = resultSet.getString("englishName");
				String englishNameTranslation = resultSet.getString("englishNameTranslation");
				String revelationType = resultSet.getString("revelationType");
				currentSurah = new SurahModel(surahNumber, numberOfAyahs, name, englishName, englishNameTranslation, revelationType);
			}
			resultSet.close();
		} catch (Exception e) {
			System.out.println("App data: " + e);
		}
		return currentSurah;
	}
}
