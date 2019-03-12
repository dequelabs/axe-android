package com.deque.axe.android;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

public abstract class AxeRuleViewHierarchy extends AxeRule {

  protected AxeRuleViewHierarchy(String standard, int impact, String summary) {
    super(standard, impact, summary);
  }

  public void setup(final AxeContext axeContext, final AxeProps axeProps) { }

  public abstract void collectProps(final AxeView axeView, final AxeProps axeProps);

  public abstract boolean isApplicable(final AxeProps axeProps);

  public abstract @AxeStatus String runRule(final AxeProps axeProps);

  public void tearDown() { }
}
