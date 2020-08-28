package com.deque.axe.android.rules.hierarchy.base;

import android.support.annotation.CallSuper;
import com.deque.axe.android.AxeRuleViewHierarchy;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeProps.Name;

public abstract class InformativeView extends AxeRuleViewHierarchy {

  protected InformativeView(String standard, int impact, String summary) {
    super(standard, impact, summary);
  }

  @Override
  @CallSuper
  public void collectProps(AxeView axeView, AxeProps axeProps) {
    super.collectProps(axeView, axeProps);

    axeProps.put(AxeProps.Name.VISIBLE_TEXT, axeView.text);
  }

  @Override
  public boolean isApplicable(AxeProps axeProps) {
    return !AxeTextUtils.isNullOrEmpty(axeProps.get(Name.VISIBLE_TEXT, String.class))
      && super.isApplicable(axeProps);
  }
}
