package com.deque.axe.android.constants;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    AndroidClassNames.CHECKBOX,
    AndroidClassNames.EDIT_TEXT,
    AndroidClassNames.IMAGE_VIEW,
    AndroidClassNames.SWITCH,
    AndroidClassNames.TEXT_VIEW
})
public @interface AndroidClassNames {
  String CHECKBOX = "android.widget.Checkbox";
  String EDIT_TEXT = "android.widget.EditText";
  String IMAGE_VIEW = "android.widget.ImageView";
  String SWITCH = "android.widget.Switch";
  String TEXT_VIEW = "android.widget.TextVIew";
}