package com.deque.axe.android;

import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.rules.hierarchy.ImageViewName;

/**
 * Main AxeRule class.
 */
public abstract class AxeRule {

  public final String id;

  public final @AxeImpact int impact;

  public final @AxeStandard String standard;

  public final String summary;

  /**
   * Construct an AxeRule.
   * @param standard The Standard the rule applies to.
   * @param impact How high an impact the rule has on users.
   * @param summary A simple description of what the rule is looking for.
   */
  public AxeRule(@AxeStandard String standard, @AxeImpact int impact, final String summary) {
    this.id = getClass().getSimpleName();
    this.impact = impact;
    this.summary = summary;
    this.standard = standard;
  }
}
