package com.vieup.utility.media;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TextToSpeachUtil {

    public static String googleTranslateVoiceUrl(String language, String message) {
        return "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=" + language + "&q=" + message;
    }

    public static void downlaod(String url, OutputStream outputStream) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                outputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
