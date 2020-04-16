package com.deque.axe.android.constants;

import com.deque.axe.android.AxeRule;
import com.deque.axe.android.rules.hierarchy.ActiveViewName;
import com.deque.axe.android.rules.hierarchy.CheckBoxName;
import com.deque.axe.android.rules.hierarchy.ColorContrast;
import com.deque.axe.android.rules.hierarchy.EditTextName;
import com.deque.axe.android.rules.hierarchy.EditTextValue;
import com.deque.axe.android.rules.hierarchy.ImageViewName;
import com.deque.axe.android.rules.hierarchy.SwitchName;
import com.deque.axe.android.rules.hierarchy.TouchSizeWcag;
import com.deque.axe.android.rules.stateful.DontMoveAccessibilityFocus;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class Constants {

  static final @AxeStandard Set<String> AXE_STANDARDS = new HashSet<>();

  static final @AxeStatus Set<String> AXE_STATUSES = new HashSet<>();

  public static final Set<Class<? extends AxeRule>> AXE_RULE_CLASSES;

  private static final Map<Integer, String> AXE_EVENT_TYPE_NAMES = new HashMap<>();

  public static final String DEFAULT_SCREEN_TITLE = "No Screen Title Available";

  static {
    AXE_STANDARDS.add(AxeStandard.WCAG_21);
    AXE_STANDARDS.add(AxeStandard.WCAG_20);
    AXE_STANDARDS.add(AxeStandard.PLATFORM);
    AXE_STANDARDS.add(AxeStandard.BEST_PRACTICE);

    AXE_STATUSES.add(AxeStatus.FAIL);
    AXE_STATUSES.add(AxeStatus.PASS);
    AXE_STATUSES.add(AxeStatus.INAPPLICABLE);
    AXE_STATUSES.add(AxeStatus.INCOMPLETE);

    Set<Class<? extends AxeRule>> temp = new HashSet<>();
    temp.add(ActiveViewName.class);
    temp.add(EditTextName.class);
    temp.add(EditTextValue.class);
    temp.add(ImageViewName.class);
    temp.add(TouchSizeWcag.class);
    temp.add(DontMoveAccessibilityFocus.class);
    temp.add(ColorContrast.class);
    temp.add(CheckBoxName.class);
    temp.add(SwitchName.class);

    AXE_RULE_CLASSES = Collections.unmodifiableSet(temp);

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.ANNOUNCEMENT, "accessibility_announcement");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.GESTURE_DETECTION_END, "gesture_detection_end");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.GESTURE_DETECTION_START, "gesture_detection_start");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.NOTIFICATION_STATE_CHANGED,
        "notification_state_changed");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.TOUCH_EXPLORATION_GESTURE_END,
        "touch_exploration_gesture_end");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.TOUCH_EXPLORATION_GESTURE_START,
        "touch_exploration_gesture_start");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.TOUCH_INTERACTION_END, "touch_interaction_end");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.TOUCH_INTERACTION_START, "touch_interaction_start");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_ACCESSIBILITY_FOCUS_CLEARED,
        "view_accessibility_focus_cleared");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_ACCESSIBILITY_FOCUSED,
        "view_accessibility_focused");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_CLICKED, "view_clicked");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_FOCUSED, "view_input_focused");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_HOVER_ENTER, "view_hover_enter");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_HOVER_EXIT, "view_hover_exit");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_LONG_CLICKED, "view_long_clicked");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_SCROLLED, "view_scrolled");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_SELECTED, "view_selected");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_TEXT_CHANGED, "view_text_changed");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.VIEW_TEXT_SELECTION_CHANGED,
        "view_text_selection_changed");

    AXE_EVENT_TYPE_NAMES.put(AxeEventType.WINDOW_CONTENT_CHANGED, "window_content_changed");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.WINDOW_STATE_CHANGED, "window_state_changed");
    AXE_EVENT_TYPE_NAMES.put(AxeEventType.WINDOWS_CHANGED, "windows_changed");
  }

  public static String getEventTypeName(@AxeEventType Integer type) {
    return AXE_EVENT_TYPE_NAMES.get(type);
  }
}
