package com.vieup.utility.media;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DisplayEntity {

    boolean audio = false;
    boolean image = false;
    boolean script = false;
    boolean explain = false;
}