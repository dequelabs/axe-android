package com.deque.axe.android;

import android.support.annotation.CallSuper;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

public abstract class AxeRuleViewHierarchy extends AxeRule {

  protected AxeRuleViewHierarchy(String standard, int impact, String summary) {
    super(new AxeConf.RuleConf(impact, standard, summary));
  }

  public void setup(final AxeContext axeContext, final AxeProps axeProps) { }

  /**
   * Collects Props for each view.
   * @param axeView to collect props from.
   * @param axeProps to which props will be stored in.
   */
  @CallSuper
  public void collectProps(final AxeView axeView, final AxeProps axeProps) {
    axeProps.put(
        AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE,
        axeView.overridesAccessibilityDelegate
    );
    axeProps.put(AxeProps.Name.IS_VISIBLE_TO_USER, axeView.isVisibleToUser);
  }

  @CallSuper
  public boolean isApplicable(final AxeProps axeProps) {
    return !axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class);
  }

  public abstract @AxeStatus String runRule(final AxeProps axeProps);

  public void tearDown() { }
}
