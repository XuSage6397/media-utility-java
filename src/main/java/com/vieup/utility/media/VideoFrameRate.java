package com.vieup.utility.media;

public enum VideoFrameRate {
    HD_720P_WH(1280, 720),
    HD_720P_HW(720, 1080),
    HD_FULL_1080P_WH(1920, 1080),
    HD_FULL_1080P_HW(1080, 1920),
    HD_QUAD_1440P_WH(2560, 1440),
    HD_QUAD_1440P_HW(1440, 2560),
    HD_ULTRA_4K_WH(3840, 2160),
    HD_ULTRA_4K_HW(2160, 3840);

    int width;
    int height;

    VideoFrameRate(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
