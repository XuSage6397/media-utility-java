package com.vieupt.media.utility;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoSlidesBuilder {
    static {
        System.setProperty("java.library.path", "/usr/local/lib");
    }

    static String BASE_PATH = "/Users/xusage/Workspaces/Vieup/media-videos/";

    public static File gapListening() {
        return new File(BASE_PATH + "gaps/listening.png");
    }

    public static File gapReading() {
        return new File(BASE_PATH + "gaps/reading.png");
    }

    public static File gapSpeaking() {
        return new File(BASE_PATH + "gaps/speaking.png");
    }

    public static File gapWriting() {
        return new File(BASE_PATH + "gaps/writing.png");
    }

    /**
     * build a slides video by slides with the prefix.
     * the slides image should be '.png' and the audio should be '.mp3'.
     *
     * @param slides
     * @param prefix
     */
    public static void serialBuilder(String[] slides, String prefix, String output) throws IOException {
        File[] slidesImageFiles = new File[slides.length];
        File[] slidesAudioFiles = new File[slides.length];

        for (int i = 0; i < slides.length; i++) {
            String slide = slides[i];
            File imageFile = new File(prefix + "resources/" + slide + ".png");
            if (!(imageFile.isFile() && imageFile.exists())) {
                throw new FileNotFoundException(imageFile.getAbsolutePath());
            }
            slidesImageFiles[i] = imageFile;
            File audioFile = new File(prefix + "resources/" + slide + ".mp3");
            if (!(audioFile.isFile() && audioFile.exists())) {
                throw new FileNotFoundException(audioFile.getAbsolutePath());
            }
            slidesAudioFiles[i] = audioFile;
        }

        String outputVideo = prefix + "output/" + output + ".mp4";
        int frameRate = 32;
        int width = 1280;
        int height = 720;

        int audioBitrate = 24000;
        int audioChannels = 2;

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideo, width, height);
        try {
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFrameRate(frameRate);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setAudioChannels(audioChannels);
            recorder.setAudioBitrate(audioBitrate);
            recorder.setSampleRate(audioBitrate);
            recorder.start();

            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

            long audioTimestamp = 0L;
            for (int i = 0; i < slides.length; i++) {
                File imageFile = slidesImageFiles[i];
                File audioFile = slidesAudioFiles[i];

                BufferedImage image = ImageIO.read(imageFile);
                Java2DFrameConverter frameConverter = new Java2DFrameConverter();
                Frame imageFrame = frameConverter.convert(image);

                FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(audioFile);
                audioGrabber.start();
                Frame audioFrame;

//              record audio frame, the recorder.getTimestamp doesn't add
                while ((audioFrame = audioGrabber.grabFrame()) != null) {
                    recorder.record(audioFrame);
                }
//              the audio during should add the last frame length.
                audioTimestamp += audioGrabber.getTimestamp() + audioGrabber.getSampleRate();
//              record image frame, the recorder.getTimestamp adds.
                while (recorder.getTimestamp() < audioTimestamp) {
                    recorder.record(imageFrame);
                }

                audioGrabber.stop();
                audioGrabber.stop();
            }

            recorder.stop();
            recorder.release();

            System.out.println("Video created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dictationBuilder() {

    }

    public static Frame generateSilentAudioFrame(int sampleRate, int channels, int frameSize) {
        int frameCount = frameSize / (2 * channels); // 2 bytes per sample
        int dataSize = frameSize * channels * sampleRate;

        // 创建音频帧
        Frame audioFrame = new Frame();
        audioFrame.sampleRate = sampleRate;
        audioFrame.audioChannels = channels;

        byte[] data = new byte[dataSize];
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
        // 将音频数据设置到音频帧中
        audioFrame.data.put(data);

        return audioFrame;
    }

    public static void main(String[] args) throws IOException {
        List<String> slideList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            slideList.add("number_" + i);
        }
        String[] slideArray = slideList.toArray(new String[slideList.size()]);
        serialBuilder(slideArray, "/Users/xusage/Workspaces/Vieup/media-videos/images-english/basic-numeric/", "number_serial");

    }

    public static class Options {
        int width;

        int height;

        int imageDuring;

        int audioDuring;

        int audioGapBefore;

        int audioGapAfter;


    }
}
