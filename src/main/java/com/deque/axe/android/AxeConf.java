package com.deque.axe.android;

import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.Constants;

import java.util.HashSet;
import java.util.Set;

public class AxeConf {

  /**
   * The default standards to apply to every new Axe Instance.
   */
  private static final @AxeStandard Set<String> DEFAULT_STANDARDS = new HashSet<>();

  static {
    DEFAULT_STANDARDS.add(AxeStandard.BEST_PRACTICE);
    DEFAULT_STANDARDS.add(AxeStandard.PLATFORM);
    DEFAULT_STANDARDS.add(AxeStandard.WCAG_20);
    DEFAULT_STANDARDS.add(AxeStandard.WCAG_21);
  }

  /**
   * A Set of AxeTypes to include in this AxeRun.
   */
  final @AxeStandard Set<String> standards = DEFAULT_STANDARDS;

  /**
   * A set of AxeRules that will be included Regardless of any other setting.
   */
  final Set<String> ruleIds = new HashSet<>();

  public AxeConf addRule(final Class<? extends AxeRuleViewHierarchy> rule) {
    ruleIds.add(rule.getSimpleName());
    return this;
  }

  public AxeConf removeStandard(@AxeStandard String standard) {
    standards.remove(standard);
    return this;
  }

  Set<AxeRule> ruleInstances() {

    final Set<AxeRule> ruleInstances = new HashSet<>();

    try {

      for (Class<? extends AxeRule> axeRuleClass : Constants.AXE_RULE_CLASSES) {

        final AxeRule axeRule = axeRuleClass.newInstance();

        if (standards.contains(axeRule.standard)
            || ruleIds.contains(axeRule.getClass().getSimpleName())) {

          ruleInstances.add(axeRule);

          ruleIds.add(axeRule.getClass().getSimpleName());
        }

      }

      for (Class<? extends AxeRuleViewHierarchy> axeRuleClass : customRules) {
        ruleInstances.add(axeRuleClass.newInstance());
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return ruleInstances;
  }

  public AxeConf includeBestPractices() {
    standards.add(AxeStandard.BEST_PRACTICE);
    return this;
  }

  public final transient Set<Class<? extends AxeRuleViewHierarchy>> customRules = new HashSet<>();

}
