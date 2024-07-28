package com.mycompany.project.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private MediaPlayer bgmPlayer; // MediaPlayer cho âm thanh nền
    private Map<String, MediaPlayer> soundEffects = new HashMap<>();

    public void playBackgroundMusic(String soundFile, double volume, boolean repeat) {
        try {
            URL resource = getClass().getResource("/sounds/" + soundFile);
            if (resource == null) {
                throw new RuntimeException("Sound file not found: " + soundFile);
            }

            Media media = new Media(resource.toString());

            if (bgmPlayer != null) {
                bgmPlayer.stop(); // Dừng nếu có đang chạy
            }

            bgmPlayer = new MediaPlayer(media);
            bgmPlayer.setVolume(volume);
            bgmPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
            bgmPlayer.setOnReady(() -> bgmPlayer.play());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    public void playSoundEffect(String soundFile, double volume) {
        try {
            URL resource = getClass().getResource("/sounds/" + soundFile);
            if (resource == null) {
                throw new RuntimeException("Sound file not found: " + soundFile);
            }

            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());

            // Thêm vào bản đồ để có thể quản lý nếu cần
            soundEffects.put(soundFile, mediaPlayer);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error playing sound effect: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }
}
