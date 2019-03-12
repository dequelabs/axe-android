package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.AxeRuleViewHierarchy;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;

import java.util.ArrayList;
import java.util.List;

public class EditTextValue extends AxeRuleViewHierarchy {

  static List<String> EDITABLE_TYPE_NAMES = new ArrayList<>();

  static {
    EDITABLE_TYPE_NAMES.add("android.widget.EditText");
  }

  public EditTextValue() {
    super(AxeStandard.WCAG_20, AxeImpact.CRITICAL,
        "Editable Views must not override the Value spoken by TalkBack.");
  }

  @Override
  public void collectProps(AxeView axeView, AxeProps props) {
    props.put(AxeProps.Name.CLASS_NAME, axeView.className);
    props.put(AxeProps.Name.CONTENT_DESCRIPTION, axeView.contentDescription);
  }

  @Override
  public boolean isApplicable(AxeProps axeProps) {

    final String className = axeProps.get(AxeProps.Name.CLASS_NAME, String.class);

    return EDITABLE_TYPE_NAMES.contains(className);
  }

  @Override
  public String runRule(AxeProps axeProps) {

    final String contDesc = axeProps.get(AxeProps.Name.CONTENT_DESCRIPTION, String.class);

    if (AxeTextUtils.isNullOrEmpty(contDesc)) {
      return AxeStatus.PASS;
    }

    return AxeStatus.FAIL;
  }
}
