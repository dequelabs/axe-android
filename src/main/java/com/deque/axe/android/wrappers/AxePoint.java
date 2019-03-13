package com.deque.axe.android.wrappers;

public class AxePoint {

  public final int valueY;
  public final int valueX;

  /**
   * Constructor for AxePoint.
   * @param x The x value or column.
   * @param y The y value or row.
   */
  public AxePoint(final int x, final int y) {

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
