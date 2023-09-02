package com.vieup.utility.media.freemarker;

import com.vieup.utility.media.DisplayEntity;
import com.vieup.utility.media.ItemEntity;
import com.vieup.utility.media.StyleEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SlideContext {

    StyleEntity style;

    ItemEntity item;

    DisplayEntity display;

}
