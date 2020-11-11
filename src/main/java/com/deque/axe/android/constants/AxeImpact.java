package com.deque.axe.android.constants;

/**
 * Enums in Android are sketchy, so we have this simple Static class for accessing Impact constants.
 */
public enum AxeImpact {
  MINOR(0),
  MODERATE(1),
  SERIOUS(2),
  CRITICAL(3),
  BLOCKER(4);

  private final int impact;

  AxeImpact(int impact) {
    this.impact = impact;
  }

  public int getValue() {
    return impact;
  }
}