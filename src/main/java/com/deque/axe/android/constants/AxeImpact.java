package com.deque.axe.android.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Enums in Android are sketchy, so we have this simple Static class for accessing Impact constants.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
  0,
  1,
  2,
  3,
  4
})
public @interface AxeImpact {
  Integer MINOR = 0;
  Integer MODERATE = 1;
  Integer SERIOUS = 2;
  Integer CRITICAL = 3;
  Integer BLOCKER = 4;
}