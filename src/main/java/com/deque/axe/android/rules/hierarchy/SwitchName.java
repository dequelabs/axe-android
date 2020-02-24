package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.AxeView;
import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.rules.hierarchy.base.ModifiableViewName;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;

public class SwitchName extends ModifiableViewName {

  public SwitchName() {
    super(AndroidClassNames.SWITCH);
  }

  @Override
  public void collectProps(AxeView axeView, AxeProps axeProps) {
    axeProps.put(AxeProps.Name.VISIBLE_TEXT,
            axeView.hasOperatingSystemTextOnlyOrIsNull(axeView.text) ? null : axeView.text);
    super.collectProps(axeView, axeProps);
  }

  @Override
  public String runRule(AxeProps axeProps) {
    final String visibleText = axeProps.get(AxeProps.Name.VISIBLE_TEXT, String.class);
    final String labeledBy = axeProps.get(AxeProps.Name.LABELED_BY, String.class);

    if ((AxeTextUtils.isNullOrEmpty(labeledBy)
            && (AxeTextUtils.isNullOrEmpty(visibleText)))
            || (!AxeTextUtils.isNullOrEmpty(labeledBy)
            && (!AxeTextUtils.isNullOrEmpty(visibleText)))) {
      return AxeStatus.FAIL;
    } else {
      return AxeStatus.PASS;
    }
  }
}