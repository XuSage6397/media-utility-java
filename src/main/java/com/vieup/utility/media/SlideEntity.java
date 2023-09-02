package com.vieup.utility.media;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SlideEntity {
    DisplayEntity display;

    long during;

    String template;

    public static enum DisplayEnum {

        AUDIO(1),
        IMAGE(2),
        SCRIPT(4),
        EXPLAIN(8)
        ;

        final int value;

        DisplayEnum(int value) {
            this.value = value;
        };

    }

}
