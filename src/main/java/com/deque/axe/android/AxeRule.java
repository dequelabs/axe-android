package com.deque.axe.android;

import com.deque.axe.android.AxeConf.RuleConf;
import com.deque.axe.android.constants.AxeStandard;

/**
 * Main AxeRule class.
 */
public abstract class AxeRule {

  public final String id;

  public final int impact;

  public final @AxeStandard String standard;

  public final String summary;

  public AxeRule(final RuleConf ruleConf) {
    this(ruleConf.standard, ruleConf.impact, ruleConf.summary);
  }

  /**
   * Construct an AxeRule.
   * @param standard The Standard the rule applies to.
   * @param impact How high an impact the rule has on users.
   * @param summary A simple description of what the rule is looking for.
   */
  @Deprecated
  private AxeRule(@AxeStandard String standard, int impact, final String summary) {
    this.id = getClass().getSimpleName();
    this.impact = impact;
    this.summary = summary;
    this.standard = standard;
  }
}
