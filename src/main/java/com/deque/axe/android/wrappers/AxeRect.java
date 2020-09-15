package com.deque.axe.android.wrappers;

import android.support.annotation.NonNull;

import com.deque.axe.android.utils.JsonSerializable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.NotNull;

public class AxeRect implements Comparable<AxeRect>, JsonSerializable {

  public final int left;
  public final int right;
  public final int top;
  public final int bottom;

  /**
   * Similar to the Android.Rect object, but does way cooler things.
   *
   * @param left   The left most pixel. (left {@literal <} right)
   * @param right  The right most pixel. (right {@literal >} left)
   * @param top  The top most pixel. (top {@literal <} bottom)
   * @param bottom The bottom most pixel. (bottom {@literal >} top)
   */
  public AxeRect(final int left, final int right, final int top, final int bottom) {
    this.left = left < right ? left : right;
    this.right = left < right ? right : left;
    this.top = top < bottom ? top : bottom;
    this.bottom = top < bottom ? bottom : top;
  }

  public AxeRect(@NonNull AxeRect rect) {
    this(rect.left, rect.right, rect.top, rect.bottom);
  }

  public boolean isWithin(@NonNull AxeRect container) {
    return container.top <= top && container.bottom >= bottom
        && container.left <= left && container.right >= right;
  }

  public boolean overlaps(@NonNull AxeRect other) {
    int minX = Math.max(this.left, other.left);
    int minY = Math.max(this.top, other.top);
    int maxX = Math.min(this.right, other.right);
    int maxY = Math.min(this.bottom, other.bottom);

    double interArea = Math.max(0, maxX - minX) * Math.max(0, maxY - minY);

    double predictionBoundsArea = (this.right - this.left) * (this.bottom - this.top);

    double otherArea = (other.right - other.left) * (other.bottom - other.top);

    return interArea / (predictionBoundsArea + otherArea - interArea) > 0;
  }

  private boolean overlapsFromAbove(AxeRect other) {
    return this.top <= other.top || this.right <= other.right;
  }

  private boolean overlapsFromBelow(AxeRect other) {
    return this.bottom >= other.bottom || this.left >= other.left;
  }

  boolean isLeadingEdge(final AxePoint point) {
    return point.valueX == left;
  }

  boolean isTrailingEdge(final AxePoint point) {
    return isTrailingEdge(point.valueX);
  }

  private boolean isTrailingEdge(final int column) {
    return column == right;
  }

  public int width() {
    return right - left;
  }

  public int height() {
    return bottom - top;
  }

  @Override
  public String toString() {
    return "Rect("
        + left + ", "
        + top + " - "
        + right + ", "
        + bottom + ")";
  }

  public Iterable<AxePoint> binaryRowSearch(final int maxWidthBetweenRows) {
    return () -> binaryRowSearch(top, bottom, maxWidthBetweenRows);
  }

  private Iterator<AxePoint> binaryRowSearch(int y1, int y2, int widthBetweenRows) {

    final int maxY = y1 < y2 ? y2 : y1;
    final int minY = y1 < y2 ? y1 : y2;

    if ((maxY - minY) <= widthBetweenRows) {
      return new Iterator<AxePoint>() {
        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public AxePoint next() throws NoSuchElementException {
          throw new NoSuchElementException();
        }
      };
    }

    return new Iterator<AxePoint>() {

      int valueX = left;

      final int middle = (minY + maxY) / 2;

      final Iterator<AxePoint> topHalf = binaryRowSearch(minY, middle, widthBetweenRows);

      final Iterator<AxePoint> bottomHalf = binaryRowSearch(middle, maxY, widthBetweenRows);

      @Override
      public boolean hasNext() {

        if (!isTrailingEdge(valueX - 1)) {
          return true;
        }

        return topHalf.hasNext() || bottomHalf.hasNext();
      }

      @Override
      public AxePoint next() throws NoSuchElementException {

        if (!isTrailingEdge(valueX - 1)) {
          return new AxePoint(valueX++, middle);
        }

        if (topHalf.hasNext()) {
          return topHalf.next();
        }

        if (bottomHalf.hasNext()) {
          return bottomHalf.next();
        }

        throw new NoSuchElementException();
      }
    };
  }

  @Override
  public int hashCode() {
    return JsonSerializable.hashCode(this);
  }

  @Override
  public boolean equals(Object object) {

    if (object == this) {
      return true;
    }

    if (!(object instanceof AxeRect)) {
      return false;
    }

    AxeRect axerect = (AxeRect) object;

    return axerect.compareTo(this) == 0;
  }

  @Override
  public int compareTo(@NotNull AxeRect axeRect) {

    int temp = Integer.compare(top, axeRect.top);

    if (temp != 0) {
      return temp;
    }

    temp = Integer.compare(left, axeRect.left);

    if (temp != 0) {
      return temp;
    }

    temp = Integer.compare(bottom, axeRect.bottom);

    if (temp != 0) {
      return temp;
    }

    return Integer.compare(right, axeRect.right);
  }
}
