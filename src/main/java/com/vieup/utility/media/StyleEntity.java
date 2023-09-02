package com.vieup.utility.media;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StyleEntity {

    Integer width = 1280;
    Integer height = 720;

    String fontSize = "1em";

    Integer coverLineHeight = 140;

    Integer imageHeight = 480;
}
