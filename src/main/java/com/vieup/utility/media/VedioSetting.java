package com.vieup.utility.media;

import lombok.Data;

@Data
public class VedioSetting {

    int frameRate = 24;

    int width = 1280;
    int height = 720;

    int audioBitrate = 128000;
}
