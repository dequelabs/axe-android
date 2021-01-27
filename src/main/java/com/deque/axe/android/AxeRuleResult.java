package com.deque.axe.android;

import android.support.annotation.NonNull;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.utils.AxeTree;
import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxeProps;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public class AxeRuleResult implements Comparable<AxeRuleResult>, JsonSerializable {

  /**
   * A simple interface that allows us to query AxeResult objects.
   */
  public interface Matcher {
    boolean matches(AxeRuleResult ruleResult);
  }

  /**
   * A unique identifier for an AxeView within a given set of axeRuleResults.
   */
  public final String axeViewId;

  /**
   * True when result belongs to a view not visible to user.
   */
  public final Boolean isVisibleToUser;

  /**
   * The properties used in determining that the AxeView was in violation.
   */
  public final AxeProps props;

  /**
   * The ID of the rule that generated the result. Very often also the ClassName of the Rule.
   */
  public final String ruleId;

  /**
   * The basic description of the Rule that failed.
   */
  public final String ruleSummary;

  /**
   * The impact of the rule [0=MINOR, 1=MODERATE, 2=SERIOUS, 3=CRITICAL, 4=BLOCKER].
   */
  public int impact;

  /**
   * One of [PASS, FAIL, INCOMPLETE, INAPPLICABLE].
   */
  public final @AxeStatus String status;

  /**
   * Construct a RuleResult from its components.
   * @param ruleId The id of the rule.
   * @param ruleSummary A summary of the rule.
   * @param axeViewId The id of the view it's associated with.
   * @param status The status of the rule (PASS, FAIL, etc)
   * @param impact How sever the issue is.
   * @param axeProps Properties analyzed to come to these conclusions.
   */
  public AxeRuleResult(
      final String ruleId,
      final String ruleSummary,
      final String axeViewId,
      final String status,
      final int impact,
      final AxeProps axeProps,
      final Boolean isVisibleToUser
  ) {
    this.ruleId = ruleId;
    this.ruleSummary = ruleSummary;
    this.axeViewId = axeViewId;
    this.status = status;
    this.impact = impact;
    this.props = axeProps;
    this.isVisibleToUser = isVisibleToUser;
  }

  AxeRuleResult(
      @AxeStatus String status,
      AxeRule axeRule,
      AxeProps axeProps,
      AxeView axeView
  ) {
    this(
        axeRule != null ? axeRule.id : null,
        axeRule != null ? axeRule.summary : null,
        axeView == null ? null : axeView.getNodeId(),
        status,
        axeRule != null ? axeRule.impact : 0,
        axeProps,
        axeView != null
          && (axeView.calculatedProps == null
              || axeView.calculatedProps.get(AxePropCalculator.Props.IS_VISIBLE_TO_USER.getProp()) == null
              || Boolean.parseBoolean(axeView.calculatedProps.get(
                      AxePropCalculator.Props.IS_VISIBLE_TO_USER.getProp())
                  )
              )
    );
  }

  @Override
  public Gson getGsonComparison() {
    return new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .addSerializationExclusionStrategy(new ExclusionStrategy() {

          @Override
          public boolean shouldSkipField(FieldAttributes f) {
            return "props".equalsIgnoreCase(f.getName());
          }

          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        }).create();
  }

  @Override
  public int hashCode() {
    return JsonSerializable.hashCode(this);
  }

  @Override
  public int compareTo(@NonNull AxeRuleResult o) {
    return JsonSerializable.compareTo(this, o);
  }

  @Override
  public boolean equals(Object object) {

    if (object == this) {
      return true;
    }

    if (!(object instanceof AxeRuleResult)) {
      return false;
    }

    final AxeRuleResult axeRuleResult = (AxeRuleResult) object;

    if (impact != axeRuleResult.impact) {
      return false;
    }

    return axeRuleResult.compareTo(this) == 0;
  }

  @Override
  public String toString() {
    return toJson();
  }
}
