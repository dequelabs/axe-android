package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.AxeRuleViewHierarchy;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeProps.Name;

public class ImageViewName extends AxeRuleViewHierarchy {

  public ImageViewName() {
    super(AxeStandard.WCAG_20, AxeImpact.CRITICAL,
        "Focusable Informative Views must have Text or a ContentDescription.");
  }

  @Override
  public void collectProps(AxeView axeView, AxeProps axeProps) {
    axeProps.put(Name.CONTENT_DESCRIPTION, axeView.contentDescription);
    axeProps.put(Name.CLASS_NAME, axeView.className);
    axeProps.put(Name.IMPORTANT, axeView.isImportantForAccessibility);
  }

  @Override
  public boolean isApplicable(AxeProps axeProps) {

    final String className = axeProps.get(Name.CLASS_NAME, String.class);

    return AxeView.classNameIsOfType(className, AndroidClassNames.IMAGE_VIEW);
  }

  @Override
  public String runRule(AxeProps axeProps) {

    final boolean isImportantForAccessibility = axeProps.get(Name.IMPORTANT, Boolean.class);
    final String contentDescription = axeProps.get(Name.CONTENT_DESCRIPTION, String.class);

    if (!isImportantForAccessibility) {
      return AxeStatus.PASS;
    }

    if (AxeTextUtils.isNullOrEmpty(contentDescription)) {
      return AxeStatus.FAIL;
    } else {
      return AxeStatus.PASS;
    }
  }
}
