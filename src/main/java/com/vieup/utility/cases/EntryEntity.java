package com.vieup.utility.cases;

import lombok.Data;

/**
 * 一个基础条目包含标题，描述，
 */
@Data
public class EntryEntity {

    /**
     * the locale of current concept
     */
    String locale;

    /**
     * the title of concept, which is base on the locale
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
     * the detail text description of the concept.
     */
    String rheme;

    /**
     * the image of concept
     */
    String image;

    /**
     * the audio of concept
     */
    String audio;

    /**
     * the notes
     */
    String annotation;
}
