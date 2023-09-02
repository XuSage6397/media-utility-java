package com.vieup.utility.media;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemEntity {

    String script;
    String image;
    String audio;

}
