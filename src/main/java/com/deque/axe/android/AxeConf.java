package com.deque.axe.android;

import static com.deque.axe.android.constants.AxeImpact.CRITICAL;
import static com.deque.axe.android.constants.AxeImpact.MODERATE;
import static com.deque.axe.android.constants.AxeImpact.SERIOUS;

import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.Constants;

import com.deque.axe.android.rules.hierarchy.ActiveViewName;
import com.deque.axe.android.rules.hierarchy.CheckBoxName;
import com.deque.axe.android.rules.hierarchy.ColorContrast;
import com.deque.axe.android.rules.hierarchy.EditTextName;
import com.deque.axe.android.rules.hierarchy.EditTextValue;
import com.deque.axe.android.rules.hierarchy.ImageViewName;
import com.deque.axe.android.rules.hierarchy.SwitchName;
import com.deque.axe.android.rules.hierarchy.TouchSizeWcag;
import com.deque.axe.android.rules.stateful.DontMoveAccessibilityFocus;
import com.deque.axe.android.utils.JsonSerializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AxeConf {

  static class RuleConf implements JsonSerializable {

    final int impact;
    final String standard;
    final String summary;
    boolean ignored = false;

    RuleConf(
        final int impact,
        final String standard,
        final String summary
    ) {
      this.impact = impact;
      this.standard = standard;
      this.summary = summary;
    }
  }

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
   * AxeConf Constructor to instantiate the legacy rules.
   */
  public AxeConf() {
    rules.put(
        ActiveViewName.class.getSimpleName(),
        new RuleConf(
            CRITICAL.getValue(),
            AxeStandard.WCAG_20,
            "Views that users can interact with must have a Name."
        )
    );

    rules.put(
         CheckBoxName.class.getSimpleName(),
         new RuleConf(
             MODERATE.getValue(),
             AxeStandard.BEST_PRACTICE,
             "Views that have modifiable Values should get their name from a nearby Label."
         )
    );

    rules.put(
         ColorContrast.class.getSimpleName(),
         new RuleConf(
             MODERATE.getValue(),
             AxeStandard.WCAG_20,
             "Text adequately contrasts with its background."
         )
    );

    rules.put(
         EditTextName.class.getSimpleName(),
         new RuleConf(
             MODERATE.getValue(),
             AxeStandard.BEST_PRACTICE,
             "Views that have modifiable Values should get their name from a nearby Label."
         )
    );

    rules.put(
         EditTextValue.class.getSimpleName(),
         new RuleConf(
             CRITICAL.getValue(),
             AxeStandard.WCAG_20,
             "Editable Views must not override the Value spoken by TalkBack."
         )
    );

    rules.put(
         ImageViewName.class.getSimpleName(),
         new RuleConf(
             CRITICAL.getValue(),
             AxeStandard.WCAG_20,
             "Focusable Informative Views must have Text or a ContentDescription."
         )
    );

    rules.put(
         SwitchName.class.getSimpleName(),
         new RuleConf(
             MODERATE.getValue(),
             AxeStandard.BEST_PRACTICE,
             "Views that have modifiable Values should get their name from a nearby Label."
         )
    );

    rules.put(
         TouchSizeWcag.class.getSimpleName(),
         new RuleConf(
             MODERATE.getValue(),
             AxeStandard.WCAG_21,
             "Active views adhere to WCAG Touch Target Size requirements."
         )
    );

    rules.put(
         DontMoveAccessibilityFocus.class.getSimpleName(),
         new RuleConf(
             SERIOUS.getValue(),
             AxeStandard.BEST_PRACTICE,
             "Applications should not forcibly move focus around."
         )
    );
  }

  /**
   * A Set of AxeTypes to include in this AxeRun.
   */
  final @AxeStandard Set<String> standards = DEFAULT_STANDARDS;

  /**
   * A set of AxeRules that will be included Regardless of any other setting.
   */
  @Deprecated
  final Set<String> ruleIds = new HashSet<>();

  final Map<String, RuleConf> rules = new HashMap<>();

  public final transient Set<Class<? extends AxeRuleViewHierarchy>> customRules = new HashSet<>();

  public AxeConf ignore(final List<String> ruleIds, boolean ignore) {
    ruleIds.forEach(s -> ignore(s, ignore));
    return this;
  }

  /**
   * A set of AxeRules that will be excluded from result.
   */
  public AxeConf ignore(final String ruleId, final boolean ignore) {
    rules.get(ruleId).ignored = ignore;

    if (ignore) {
      ruleIds.remove(ruleId);
    } else {
      ruleIds.add(ruleId);
    }

    return this;
  }

  /**
   * Deprecated: add rule to rules map.
   */
  @Deprecated
  public AxeConf addRule(final Class<? extends AxeRuleViewHierarchy> rule) {

    final AxeRule axeRule;

    try {
      axeRule = rule.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    ruleIds.add(rule.getSimpleName());

    rules.put(rule.getSimpleName(), new RuleConf(
        axeRule.impact,
        axeRule.standard,
        axeRule.summary
    ));

    return this;
  }

  @Deprecated
  public AxeConf removeStandard(@AxeStandard String standard) {
    standards.remove(standard);
    return this;
  }

  private void normalize() {
    // Make sure that all RulesIDs were trying to run are classes we have access to.
    ruleIds.forEach(s -> {
      if (!rules.containsKey(s)) {
        throw new RuntimeException("Tried to run a RuleID that doesn't exist.");
      }
    });

    // Make sure that the RuleIDs list matches the rules data structure.
    ruleIds.clear();
    rules.forEach((s, ruleConf) -> {
      if (!ruleConf.ignored) {
        ruleIds.add(s);
      }
    });
  }

  Set<AxeRule> ruleInstances() {

    if (ruleIds.size() == 0) {
      normalize();
    }

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

  @Deprecated
  public AxeConf includeBestPractices() {
    standards.add(AxeStandard.BEST_PRACTICE);
    return this;
  }
}
