package com.vieupt.media.utility;

import org.bytedeco.javacv.*;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.opencv.opencv_core.IplImage;
import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;
import java.io.OutputStream;
import java.nio.ShortBuffer;

import static org.bytedeco.opencv.global.opencv_core.cvReleaseImage;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class VideoBuilder {
    static {
        System.setProperty("java.library.path", "/usr/local/lib");
    }

    public void imagesToVideoByPath(String[] images, OutputStream videoOutput) {
    }





    public static void main(String[] args) {

        String imageFolder = "/Users/xusage/Workspaces/Vieup/media-videos/images-english/basic-numeric/resources";
        String audioFile = "path/to/audio.wav";

//        // 设置输出视频文件路径和参数
        String outputVideo = "/Users/xusage/Workspaces/Vieup/media-videos/images-english/basic-numeric/images.mp4";
        int frameRate = 24;
        int width = 1280;
        int height = 720;

        int audioBitrate = 128000;

        File[] imageFiles = new File(imageFolder).listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No image files found in the specified folder.");
            return;
        }

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideo, width, height);
        try {
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFrameRate(frameRate);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setAudioChannels(2);
            recorder.setAudioBitrate(audioBitrate);
            recorder.start();

            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            for (File imageFile : imageFiles) {
                IplImage image = cvLoadImage(imageFile.getAbsolutePath());
                Frame frame = converter.convert(image);
                for (int i = 0; i < frameRate; i ++) {
                    recorder.record(frame);
                    cvReleaseImage(image);
                }
            }

            recorder.stop();
            recorder.release();

            System.out.println("Video created successfully.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
