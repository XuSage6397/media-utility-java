package com.vieup.utility.media;

import com.google.gson.Gson;
import com.vieup.utility.media.freemarker.SlideContext;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.springframework.util.StreamUtils;

import java.io.*;


import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.bytedeco.opencv.global.opencv_core.cvReleaseImage;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class VideoBuilder {
    static {
        System.setProperty("java.library.path", "/usr/local/lib");
    }

    static Gson gson = new Gson();

    public void imagesToVideoByPath(String[] images, OutputStream videoOutput) {
    }


    private VideoSchema loadVideoSchema(String baseDir, String lang, String model) throws IOException {

        Map<String, Object> jsonMap = new HashMap<>();
        {
            String jsonPath = baseDir + lang + "-" + MATERIAL_JSON;
            InputStream inputStream = new FileInputStream(jsonPath);
            String jsonString = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            Map<String, Object> map = gson.fromJson(jsonString, Map.class);
            jsonMap.putAll(map);
//            VideoSchema schema = videoBuilder.loadVideoSchema(inputStream);
//            BeanUtils.copyProperties(schema, videoSchema);
            inputStream.close();
        }
        {
            String jsonPath = baseDir + lang + "-" + model + ".json";
            InputStream inputStream = new FileInputStream(jsonPath);
            String jsonString = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            Map<String, Object> map = gson.fromJson(jsonString, Map.class);
            jsonMap.putAll(map);
//            VideoSchema schema = videoBuilder.loadVideoSchema(inputStream);
//            BeanUtils.copyProperties(schema, videoSchema);
            inputStream.close();
        }
        VideoSchema videoSchema = gson.fromJson(gson.toJson(jsonMap), VideoSchema.class);
        videoSchema.setBaseDir(baseDir);
        Collections.shuffle(videoSchema.items.stream().toList());
        return videoSchema;
    }

    private void makeCoverImage(VideoSchema videoSchema, String image) throws Exception {
        String html = CommonUtility.freeMarkerHtml("cover.html", videoSchema);
        File htmlFile = new File(image + ".html");
        {
            FileOutputStream htmlOut = new FileOutputStream(htmlFile);
            htmlOut.write(html.getBytes(StandardCharsets.UTF_8));
            htmlOut.close();
        }
        String imagePath = image + ".png";
        CommonUtility.html2Image(htmlFile, imagePath, videoSchema.style.getWidth(), videoSchema.style.getHeight());
        htmlFile.delete();
    }

    private File makeSlideImage(VideoSchema videoSchema, ItemEntity item, SlideEntity slide) throws Exception {
        SlideContext slideContext = new SlideContext();
        slideContext.setItem(item);
        slideContext.setStyle(videoSchema.getStyle());
        slideContext.setDisplay(slide.getDisplay());
        String html = CommonUtility.freeMarkerHtml(slide.getTemplate() + ".html", videoSchema);
        File htmlFile = new File(videoSchema.getBaseDir() + "/slideTemp.html");
        {
            FileOutputStream htmlOut = new FileOutputStream(htmlFile);
            htmlOut.write(html.getBytes(StandardCharsets.UTF_8));
            htmlOut.close();
        }
        String imagePath = videoSchema.baseDir + "tmp.png";
        CommonUtility.html2Image(htmlFile, imagePath, videoSchema.style.getWidth(), videoSchema.style.getHeight());
        htmlFile.delete();
        return new File(imagePath);
    }

    private void makeMp4(VideoSchema videoSchema, String videoFile) throws Exception {
        int frameRate = 24;
        int audioBitrate = 128000;

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoFile, videoSchema.getStyle().getWidth(), videoSchema.getStyle().getHeight());
        try {
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFrameRate(frameRate);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setAudioChannels(2);
            recorder.setAudioBitrate(audioBitrate);
            recorder.start();

            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            for (ItemEntity item: videoSchema.getItems()) {
                for (SlideEntity slide: videoSchema.getSlides()) {
                    File imageFile = makeSlideImage(videoSchema, item, slide);
                    IplImage image = cvLoadImage(imageFile.getAbsolutePath());
                    Frame frame = converter.convert(image);
                    for (int i = 0; i < frameRate * slide.getDuring(); i++) {
                        recorder.record(frame);
                        cvReleaseImage(image);
                    }
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

    private static final String MATERIAL_JSON = "material.json";
    private void buildVideo(String baseDir, String lang, String model) throws Exception {
        VideoBuilder videoBuilder = new VideoBuilder();

        VideoSchema videoSchema = loadVideoSchema(baseDir, lang, model);

        {
            String coverPath = baseDir + lang + "-" + model + "-cover";
            videoBuilder.makeCoverImage(videoSchema, coverPath);
        }

        {
            String videoFile = baseDir + lang + "-" + model + ".mp4";

        }
    }

    public static void main(String[] args) throws Exception {
        VideoBuilder videoBuilder = new VideoBuilder();
        videoBuilder.buildVideo("/Users/xusage/Workspaces/Vieup/media-videos/animals_vocabulary-1/", "en", "3-7-5-v");

//        String imageFolder = "/Users/xusage/Workspaces/Vieup/media-videos/images-english/basic-numeric/resources";
//        String audioFile = "path/to/audio.wav";
//
////        // 设置输出视频文件路径和参数
//        String outputVideo = "/Users/xusage/Workspaces/Vieup/media-videos/images-english/basic-numeric/images.mp4";
//        int frameRate = 24;
//        int width = 1280;
//        int height = 720;
//
//        int audioBitrate = 128000;
//
//        File[] imageFiles = new File(imageFolder).listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
//        if (imageFiles == null || imageFiles.length == 0) {
//            System.out.println("No image files found in the specified folder.");
//            return;
//        }
//
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideo, width, height);
//        try {
//            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//            recorder.setFrameRate(frameRate);
//            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
//            recorder.setAudioChannels(2);
//            recorder.setAudioBitrate(audioBitrate);
//            recorder.start();
//
//            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//            for (File imageFile : imageFiles) {
//                IplImage image = cvLoadImage(imageFile.getAbsolutePath());
//                Frame frame = converter.convert(image);
//                for (int i = 0; i < frameRate; i ++) {
//                    recorder.record(frame);
//                    cvReleaseImage(image);
//                }
//            }
//
//            recorder.stop();
//            recorder.release();
//
//            System.out.println("Video created successfully.");
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
