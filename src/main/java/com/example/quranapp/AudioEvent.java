package com.example.quranapp;

import javafx.event.Event;
import javafx.event.EventType;

public class AudioEvent extends Event {
    private final int ayahNumber;
    public static final EventType<AudioEvent> AUDIO_EVENT_EVENT_TYPE = new EventType<>(Event.ANY, "AUDIO_EVENT");

    public AudioEvent(int ayahNumber) {
        super(AUDIO_EVENT_EVENT_TYPE);
        this.ayahNumber = ayahNumber;
    }
    public int getAyahNumber() {
        return ayahNumber;
    }

}
