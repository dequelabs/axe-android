package com.deque.axe.android.wrappers;

import com.deque.axe.android.wrappers.AxeRect;

public class AxePoint {

  public final int valueY;
  public final int valueX;

  AxePoint(final int x, final int y) {

    this.valueY = y;
    this.valueX = x;
  }

  public boolean isTrailingEdge(final AxeRect frame) {
    return frame.isTrailingEdge(this);
  }

  public boolean isLeadingEdge(final AxeRect frame) {
    return frame.isLeadingEdge(this);
  }

  @Override public String toString() {
    return "Row: " + valueY + " Col: " + valueX;
  }
}
