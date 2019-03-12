package com.deque.axe.android.constants;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    AxeStatus.PASS,
    AxeStatus.FAIL,
    AxeStatus.INAPPLICABLE,
    AxeStatus.INCOMPLETE
})
public @interface AxeStatus {
  String PASS = "PASS";
  String FAIL = "FAIL";
  String INAPPLICABLE = "INAPPLICABLE";
  String INCOMPLETE = "INCOMPLETE";
}