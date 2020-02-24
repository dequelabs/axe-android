package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.rules.hierarchy.base.ModifiableViewName;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;

public class EditTextName extends ModifiableViewName {

  public EditTextName() {
    super(AndroidClassNames.EDIT_TEXT);
  }

  @Override
  public String runRule(AxeProps axeProps) {

    final String labeledBy = axeProps.get(AxeProps.Name.LABELED_BY, String.class);

    if (AxeTextUtils.isNullOrEmpty(labeledBy)) {
      return AxeStatus.FAIL;
    } else {
      return AxeStatus.PASS;
    }
  }
}
