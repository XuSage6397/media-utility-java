package com.vieup.utility.media;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
public class VideoSchema {

    String baseDir;

    String logo;
    String title;

    String subtitle;

    String desLang = "EN";
    String srcLang;

    Collection<ItemEntity> items;

    List<SlideEntity> slides;

    StyleEntity style;

}
