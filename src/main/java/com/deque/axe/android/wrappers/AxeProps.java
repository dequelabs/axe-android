package com.deque.axe.android.wrappers;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.deque.axe.android.utils.JsonSerializable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

public class AxeProps extends HashMap<String, Object>
    implements Comparable<AxeProps>, JsonSerializable {

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
      Name.CLASS_NAME,
      Name.CONTENT_DESCRIPTION,
      Name.DPI,
      Name.FRAME,
      Name.HEIGHT,
      Name.IMPORTANT,
      Name.IS_CLICKABLE,
      Name.IS_ENABLED,
      Name.LABELED_BY,
      Name.SPEAKABLE_TEXT,
      Name.WIDTH,
      Name.EXCEPTION,
      Name.STACK_TRACE,
      Name.EVENT_STREAM,
      Name.IS_FOCUS_CHANGE_OK,
      Name.IS_TOUCH_EXPLORATION_GESTURE,
      Name.IS_TOUCH_STARTED,
      Name.ACCESSIBILITY_EVENT,
      Name.VISIBLE_TEXT,
      Name.COLOR_BACKGROUND,
      Name.COLOR_FOREGROUND,
      Name.COLOR_CONTRAST,
      Name.CONFIDENCE,
      Name.SCREEN_HEIGHT,
      Name.SCREEN_WIDTH,
      Name.IS_OFF_SCREEN,
      Name.IS_RENDERED,
      Name.IS_PARTIALLY_VISIBLE,
      Name.HINT_TEXT,
      Name.OVERRIDES_ACCESSIBILITY_DELEGATE,
      Name.LABELED_BY_VIEW_OVERRIDES_ACCESSIBILITY_DELEGATE,
      Name.IS_VISIBLE_TO_USER
  })
  public @interface Name {
    String CLASS_NAME = "className";
    String CONTENT_DESCRIPTION = "contentDescription";
    String DPI = "Screen Dots Per Inch";
    String FRAME = "boundsInScreen";
    String HEIGHT = "height";
    String IMPORTANT = "isImportantForAccessibility";
    String IS_CLICKABLE = "isActive";
    String IS_ENABLED = "isEnabled";
    String LABELED_BY = "labeledBy";
    String SPEAKABLE_TEXT = "Speakable Text";
    String WIDTH = "width";
    String EXCEPTION = "Exception";
    String STACK_TRACE = "Stack Trace";
    String EVENT_STREAM = "Applicable Event Stream";
    String ACCESSIBILITY_EVENT = "AccessibilityEvent";
    String IS_TOUCH_STARTED = "Touch Interaction Started";
    String IS_FOCUS_CHANGE_OK = "Is Focus Change Acceptable";
    String IS_TOUCH_EXPLORATION_GESTURE = "Touch Exploration Started";
    String VISIBLE_TEXT = "Visible Text";
    String COLOR_FOREGROUND = "Foreground Color";
    String COLOR_BACKGROUND = "Background Color";
    String COLOR_CONTRAST = "Color Contrast Ratio";
    String CONFIDENCE = "Confidence in Color Detection";
    String SCREEN_HEIGHT = "Screen Height";
    String SCREEN_WIDTH = "Screen Width";
    String IS_RENDERED = "isRendered";
    String IS_OBSCURED = "isObscured";
    String IS_OFF_SCREEN = "isOffScreen";
    String IS_PARTIALLY_VISIBLE = "isPartiallyVisible";
    String HINT_TEXT = "Hint Text";
    String OVERRIDES_ACCESSIBILITY_DELEGATE = "overridesAccessibilityDelegate";
    String LABELED_BY_VIEW_OVERRIDES_ACCESSIBILITY_DELEGATE =
            "labeledByViewOverridesAccessibilityDelegate";
    String IS_VISIBLE_TO_USER = "isVisibleToUser";
  }

  @Override
  public Object put(@Name String name, Object object) {
    return super.put(name, object);
  }

  @Override
  public int compareTo(@NonNull AxeProps o) {
    return JsonSerializable.compareTo(this, o);
  }

  public <T> T get(final String name, Class<T> clazz) {
    return clazz.cast(get(name));
  }
}
