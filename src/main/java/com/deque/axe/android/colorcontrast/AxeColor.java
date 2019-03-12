package com.deque.axe.android.colorcontrast;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public class AxeColor implements Comparable<AxeColor> {

  /**
   * Below are constants from the W3C Luminance Calculation
   * http://www.w3.org/TR/WCAG/#dfn-relative-luminance
   */
  private static final double W3C_LUMINANCE_CALCULATION_THRESHOLD = .03928;
  private static final double W3C_LUMINANCE_RED_COMPONENT_MULTIPLIER = 0.2126;
  private static final double W3C_LUMINANCE_GREEN_COMPONENT_MULTIPLIER = 0.7152;
  private static final double W3C_LUMINANCE_BLUE_COMPONENT_MULTIPLIER = 0.0722;

  public static final AxeColor WHITE = new AxeColor(-1);

  public static final AxeColor BLACK = new AxeColor(0);

  private final int colorInt;

  public AxeColor(
      @IntRange(from = 0, to = 255) int alpha,
      @IntRange(from = 0, to = 255) int red,
      @IntRange(from = 0, to = 255) int green,
      @IntRange(from = 0, to = 255) int blue
  ) {
    colorInt = (alpha << 24) | (red << 16) | (green << 8) | blue;
  }

  public AxeColor(final String hexString) {
    colorInt = (int) Long.parseLong(hexString, 16);
  }

  public AxeColor(final int colorInt) {
    this.colorInt = colorInt;
  }

  /**
   * Calculate contrast between this color and another color.
   */
  public double contrast(AxeColor otherColor) {
    double luminance1 = luminance();
    double luminance2 = otherColor.luminance();

    if (luminance1 > luminance2) {
      return (luminance1 + .05) / (luminance2 + .05);
    } else {
      return (luminance2 + .05) / (luminance1 + .05);
    }
  }

  /**
   * The Luminance calculation from the w3c website.
   * http://www.w3.org/TR/2008/REC-WCAG20-20081211/#relativeluminancedef
   */
  private double luminance() {
    // The Magic Numbers are from the W3C Calculation, and I don't know what they represent.
    // Seemed more clear to present it the same way it was presented there.
    double redComponent = luminanceComponent(red()) * W3C_LUMINANCE_RED_COMPONENT_MULTIPLIER;
    double greenComponent = luminanceComponent(green()) * W3C_LUMINANCE_GREEN_COMPONENT_MULTIPLIER;
    double blueComponent = luminanceComponent(blue()) * W3C_LUMINANCE_BLUE_COMPONENT_MULTIPLIER;

    return redComponent + greenComponent + blueComponent;
  }

  /**
   * The W3C Luinance calculation separates the component colors. This function calculates
   * the Luminance component from a given color component.
   */
  private static double luminanceComponent(int color) {
    double colorRatio = color / 255.0;
    return colorRatio <= W3C_LUMINANCE_CALCULATION_THRESHOLD
        ? ((colorRatio) / 12.92)
        : Math.pow((colorRatio + 0.055) / 1.055, 2.4);
  }

  public boolean isVisiblySameColor(AxeColor color) {
    return contrast(color) < ColorContrastConfig.EQUAL_COLORS_CONTRAST_THRESHOLD;
  }

  /**
   * Return the red component of a color int. This is the same as saying
   * (color >> 16) & 0xFF
   */
  @IntRange(from = 0, to = 255)
  public int red() {
    return (colorInt >> 16) & 0xFF;
  }

  /**
   * Return the green component of a color int. This is the same as saying
   * (color >> 8) & 0xFF
   */
  @IntRange(from = 0, to = 255)
  public int green() {
    return (colorInt >> 8) & 0xFF;
  }

  /**
   * Return the blue component of a color int. This is the same as saying
   * color & 0xFF
   */
  @IntRange(from = 0, to = 255)
  public int blue() {
    return colorInt & 0xFF;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public int compareTo(@NonNull AxeColor o) {
    return Integer.compare(colorInt, o.colorInt);
  }

  @Override
  public boolean equals(Object other) {

    if (other == null) {
      return false;
    }

    if (other == this) {
      return true;
    }

    if (!(other instanceof AxeColor)) {
      return false;
    }

    return compareTo((AxeColor)other) == 0;
  }

  public String toHex() {
    return Integer.toHexString(colorInt);
  }

  @Override
  public String toString() {

    return toHex();
  }
}
