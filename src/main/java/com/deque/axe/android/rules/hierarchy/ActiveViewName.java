package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.AxeView;
import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.rules.hierarchy.base.ActiveView;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeProps.Name;

@SuppressWarnings("unused")
public class ActiveViewName extends ActiveView {

  public ActiveViewName() {
    super(AxeStandard.WCAG_20, AxeImpact.CRITICAL,
        "Views that users can interact with must have a Name.");
  }

  @Override
  public boolean isApplicable(AxeProps axeProps) {
    final String className = axeProps.get(Name.CLASS_NAME, String.class);
    if (className.equals(AndroidClassNames.CHECKBOX)
            || className.equals(AndroidClassNames.SWITCH)) {
      return false;
    }
    return super.isApplicable(axeProps);
  }

  @Override
  public void collectProps(AxeView axeView, AxeProps axeProps) {

    super.collectProps(axeView, axeProps);

    axeProps.put(Name.SPEAKABLE_TEXT, axeView.speakableTextRecursive());
  }

  @Override
  public String runRule(AxeProps axeProps) {

    final String speakableText = axeProps.get(Name.SPEAKABLE_TEXT, String.class);
    final boolean isActive = axeProps.get(Name.IS_CLICKABLE, Boolean.class);

    if (!isActive) {
      return AxeStatus.INAPPLICABLE;
    }

    if (AxeTextUtils.isNullOrEmpty(speakableText)) {
      return AxeStatus.FAIL;
    } else {
      return AxeStatus.PASS;
    }
  }
}
