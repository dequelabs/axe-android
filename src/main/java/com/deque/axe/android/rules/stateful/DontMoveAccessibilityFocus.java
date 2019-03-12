package com.deque.axe.android.rules.stateful;

import com.deque.axe.android.AxeEvent;
import com.deque.axe.android.AxeRuleStateful;
import com.deque.axe.android.constants.AxeEventType;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeEventStream;
import com.deque.axe.android.wrappers.AxeProps;

public class DontMoveAccessibilityFocus extends AxeRuleStateful {

  /**
   * Applications should not forcibly move focus around.
   */
  public DontMoveAccessibilityFocus() {
    super(AxeStandard.BEST_PRACTICE, AxeImpact.SERIOUS,
        "Applications should not forcibly move focus around.");

    applicableEventStream
        .addApplicableEventType(AxeEventType.VIEW_ACCESSIBILITY_FOCUSED)
        .addApplicableEventType(AxeEventType.TOUCH_INTERACTION_START)
        .addApplicableEventType(AxeEventType.TOUCH_INTERACTION_END);
  }

  @Override
  public String run(AxeEventStream axeEventStream, AxeProps axeProps) {

    boolean isTouchInteractionStarted = false;

    boolean isTouchExplorationStarted = false;

    boolean isFocusChangeAcceptable = false;

    for (AxeEvent axeEvent : axeEventStream) {

      if (!applicableEventStream.addEvent(axeEvent)) {
        continue;
      }

      if (axeEvent.isEventType(AxeEventType.TOUCH_INTERACTION_START)) {

        isTouchInteractionStarted = true;
        isFocusChangeAcceptable = true;
      } else if (axeEvent.isEventType(AxeEventType.TOUCH_INTERACTION_END)) {

        isTouchInteractionStarted = false;

        if (isTouchExplorationStarted) {
          //Focus change is not acceptable after the end of a touch exploration gesture.
          isFocusChangeAcceptable = false;
        }

        isTouchExplorationStarted = false;
      } else if (axeEvent.isEventType(AxeEventType.VIEW_ACCESSIBILITY_FOCUSED)) {

        if (!isFocusChangeAcceptable) {

          axeProps.put(AxeProps.Name.ACCESSIBILITY_EVENT, axeEvent);
          axeProps.put(AxeProps.Name.IS_TOUCH_STARTED, false);
          axeProps.put(AxeProps.Name.IS_FOCUS_CHANGE_OK, false);
          axeProps.put(AxeProps.Name.IS_TOUCH_EXPLORATION_GESTURE, false);

          return AxeStatus.FAIL;
        }

        if (isTouchInteractionStarted) {
          isTouchExplorationStarted = true;
        }
      }
    }

    return AxeStatus.PASS;
  }
}
