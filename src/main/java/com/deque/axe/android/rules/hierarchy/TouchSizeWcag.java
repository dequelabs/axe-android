package com.deque.axe.android.rules.hierarchy;

import static com.deque.axe.android.constants.AxeImpact.MODERATE;

import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.rules.hierarchy.base.TouchSize;

public class TouchSizeWcag extends TouchSize {

  private static final int WCAG_TARGET_SIZE = 44;

  /**
   * Active views adhere to WCAG Touch Target Size requirements.
   */
  public TouchSizeWcag() {
    super(AxeStandard.WCAG_21, MODERATE.getValue(),
        "Active views adhere to WCAG Touch Target Size requirements.",
        WCAG_TARGET_SIZE);
  }
}
