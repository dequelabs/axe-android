package com.deque.axe.android.rules.hierarchy.base;

import android.support.annotation.CallSuper;

import com.deque.axe.android.AxeRuleViewHierarchy;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.wrappers.AxeProps;

public abstract class ActiveView extends AxeRuleViewHierarchy {

  protected ActiveView(String standard, int impact, String summary) {
    super(standard, impact, summary);
  }

  @Override
  @CallSuper
  public void collectProps(AxeView axeView, AxeProps axeProps) {
    axeProps.put(AxeProps.Name.IS_CLICKABLE, axeView.isClickable);
    axeProps.put(AxeProps.Name.CLASS_NAME, axeView.className);
  }

  @Override
  public boolean isApplicable(AxeProps axeProps) {
    //TODO: Respect isEnabled.
    return axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class);
  }
}
