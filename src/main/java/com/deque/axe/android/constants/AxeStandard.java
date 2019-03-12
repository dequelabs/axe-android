package com.deque.axe.android.constants;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
  AxeStandard.WCAG_20,
  AxeStandard.WCAG_21,
  AxeStandard.PLATFORM,
  AxeStandard.BEST_PRACTICE
})
public @interface AxeStandard {
  String WCAG_20 = "WCAG 2.0";
  String WCAG_21 = "WCAG 2.1";
  String PLATFORM = "Platform";
  String BEST_PRACTICE = "Best Practice";
}