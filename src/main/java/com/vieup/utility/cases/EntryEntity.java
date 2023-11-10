package com.vieup.utility.cases;

import lombok.Data;

/**
 * 一个基础条目包含标题，描述，
 */
@Data
public class EntryEntity {

    /**
     * the locale of current entry
     */
    String locale;

    /**
     * the title of entry, which is base on the locale
     */
    String title;

    /**
     * 口语表达，用于日常沟通的语言
     */
    String speaking;

    /**
     * 书面语，通常用于写作
     */
    String writing;

    /**
     * the detail text description of the entry.
     */
    String rheme;

    /**
     * the image of entry
     */
    String image;

    /**
     * the audio of entry
     */
    String audio;

    /**
     * the notes
     */
    String annotation;
}
