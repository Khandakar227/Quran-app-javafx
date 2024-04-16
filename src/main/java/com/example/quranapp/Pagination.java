package com.example.quranapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class Pagination extends FlowPane {
	@FXML
	Button next_btn, prev_btn;
	int surahNumber, from, to, numberOfAyahs;
	
	public Pagination(int surahNumber, int from, int to, int numberOfAyahs) {
		this.from =  from;
		this.to = to;
		this.surahNumber = surahNumber;
		this.numberOfAyahs = numberOfAyahs;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Pagination.fxml"));
		loader.setRoot(this);
		loader.setController(Pagination.this);
		try {
			loader.load();
		} catch (Exception e) {
			System.out.println("[Pagination]: "+ e);
		}
	}
	
	public void initialize() {
		try {
			if(from <= 1) prev_btn.setVisible(false);
			if(to >= numberOfAyahs) next_btn.setVisible(false);
			
			prev_btn.setOnAction((e) -> {
				NavigationController.goToSurah(surahNumber, from - 20, to - 20, numberOfAyahs);
			});

			next_btn.setOnAction((e) -> {
				NavigationController.goToSurah(surahNumber, from + 20, to + 20, numberOfAyahs);
				
			});
		} catch (Exception e) {
		}
	}
}
