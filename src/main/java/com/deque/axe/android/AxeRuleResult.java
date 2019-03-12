package com.deque.axe.android;

import android.support.annotation.NonNull;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxeProps;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public class AxeRuleResult implements Comparable<AxeRuleResult>, JsonSerializable {

  /**
   * A unique identifier for an AxeView within a given set of axeRuleResults.
   */
  public final Integer axeViewId;

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
   * One of [PASS, FAIL, INCOMPLETE, INAPPLICABLE].
   */
  public final @AxeStatus String status;


  AxeRuleResult(@AxeStatus String status,
      AxeRule axeRule,
      AxeProps axeProps,
      AxeView axeView) {
    this.ruleId = axeRule.id;
    this.ruleSummary = axeRule.summary;
    this.props = axeProps;
    this.status = status;
    this.axeViewId = axeView == null ? null : axeView.axeViewId;
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

    AxeRuleResult axeRuleResult = (AxeRuleResult) object;

    return axeRuleResult.compareTo(this) == 0;
  }

  @Override
  public String toString() {
    return toJson();
  }
}
