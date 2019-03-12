package com.deque.axe.android.colorcontrast;

import android.support.annotation.NonNull;

import com.deque.axe.android.utils.JsonSerializable;

public class ColorPair implements JsonSerializable, Comparable<ColorPair> {

  final AxeColor backgroundColor;

  final AxeColor textColor;

  public ColorPair(final AxeColor backgroundColor, final AxeColor textColor) {
    this.backgroundColor = backgroundColor;
    this.textColor = textColor;
  }

  final double getContrast() {
    return backgroundColor.contrast(textColor);
  }

  private AxeColor getLighterColor() {

    double backgroundContrast = backgroundColor.contrast(AxeColor.WHITE);
    double textContrast = textColor.contrast(AxeColor.WHITE);
    return backgroundContrast < textContrast ? backgroundColor : textColor;
  }

  private AxeColor getDarkerColor() {
    return backgroundColor == getLighterColor() ? textColor : backgroundColor;
  }

  boolean isVisiblySimilar(final ColorPair otherPair) {
    return getLighterColor().isVisiblySameColor(otherPair.getLighterColor())
        && getDarkerColor().isVisiblySameColor(otherPair.getDarkerColor());
  }

  @Override
  public boolean equals(Object other) {

    if (other == null) {
      return false;
    }

    if (other == this) {
      return true;
    }

    if (!(other instanceof ColorPair)) {
      return false;
    }

    return compareTo((ColorPair) other) == 0;
  }

  @Override
  public int hashCode() {
    return JsonSerializable.hashCode(this);
  }

  @Override
  public String toString() {
    return backgroundColor.toHex() + " -> " + textColor.toHex();
  }

  @Override
  public int compareTo(@NonNull ColorPair o) {
    return Double.compare(o.getContrast(), getContrast());
  }
}
