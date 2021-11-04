package com.deque.axe.android.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("checkstyle:allowedAbbreviationLength")
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {
  /**
   * The set of FindBugs warnings that are to be suppressed in annotated element.
   * The value can be a bug category, kind or pattern.
   */
  String[] value() default {};

  /**
   * Optional documentation of the reason why the warning is suppressed.
   */
  String justification() default "";
}
