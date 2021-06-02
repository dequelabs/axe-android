package com.deque.axe.android.colorcontrast;

import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxePoint;
import com.deque.axe.android.wrappers.AxeRect;

import java.util.ArrayList;

public abstract class AxeImage implements JsonSerializable {

  public abstract AxeRect frame();

  public abstract AxeColor pixel(final int x, final int y);

  @SuppressWarnings("unused")
  public abstract String toBase64Png();

  @SuppressWarnings("unused")
  public ColorContrastResult runColorContrastCalculation() {
    return runColorContrastCalculation(frame());
  }

  public ColorContrastResult runColorContrastCalculation(AxeRect frame, AxeColor actualTextColor) {

    if (frame == null) {
      frame = frame();
    }

    ColorContrastResult result = null;

    ColorContrastRunner runner = new ColorContrastRunner();

    AxeColor previousColor = null;

    ArrayList<AxeColor> middleRowColors = new ArrayList<>();

    ArrayList<AxeColor> belowMiddleRowColors = new ArrayList<>();

    for (final AxePoint point : frame.binaryRowSearch(5)) {

      //If this is a new valueY, clear stuff out and start over.
      if (point.isLeadingEdge(frame)) {
        runner.onRowBegin();
        middleRowColors.clear();
        belowMiddleRowColors.clear();
      }

      final AxeColor colorAbove = pixel(point.valueX, point.valueY - 1);
      final AxeColor color = pixel(point.valueX, point.valueY);
      final AxeColor colorBelow = pixel(point.valueX, point.valueY + 1);

      middleRowColors.add(color);
      belowMiddleRowColors.add(colorBelow);

      runner.onPixel(colorAbove, previousColor);
      previousColor = colorAbove;

      // If this is the end of a valueY, see if we can make conclusions about our color maps.
      if (point.isTrailingEdge(frame)) {

        for (AxeColor middleRowColor : middleRowColors) {
          runner.onPixel(middleRowColor, previousColor);
          previousColor = middleRowColor;
        }

        for (AxeColor belowMiddleRowColor : belowMiddleRowColors) {
          runner.onPixel(belowMiddleRowColor, previousColor);
          previousColor = belowMiddleRowColor;
        }

        final ColorContrastResult newEntry = runner.onRowEnd(actualTextColor);

        if (result == null) {
          result = newEntry;
        }

        if (newEntry.confidence.equals(ColorContrastRunner.Confidence.HIGH)) {
          return newEntry;
        } else if (newEntry.confidence.equals(ColorContrastRunner.Confidence.MID)
                && result.confidence.equals(ColorContrastRunner.Confidence.LOW)) {
          result = newEntry;
        }
      }
    }

    return result;
  }

  /**
   * Run the ColorContrast calculation on a portion of the Image.
   * @param frame The frame to run on.
   * @return The result.
   */
  public ColorContrastResult runColorContrastCalculation(AxeRect frame) {

    if (frame == null) {
      frame = frame();
    }

    ColorContrastResult result = null;

    ColorContrastRunner runner = new ColorContrastRunner();

    AxeColor previousColor = null;

    ArrayList<AxeColor> middleRowColors = new ArrayList<>();

    ArrayList<AxeColor> belowMiddleRowColors = new ArrayList<>();

    for (final AxePoint point : frame.binaryRowSearch(5)) {

      //If this is a new valueY, clear stuff out and start over.
      if (point.isLeadingEdge(frame)) {
        runner.onRowBegin();
        middleRowColors.clear();
        belowMiddleRowColors.clear();
      }

      final AxeColor colorAbove = pixel(point.valueX, point.valueY - 1);
      final AxeColor color = pixel(point.valueX, point.valueY);
      final AxeColor colorBelow = pixel(point.valueX, point.valueY + 1);

      middleRowColors.add(color);
      belowMiddleRowColors.add(colorBelow);

      runner.onPixel(colorAbove, previousColor);
      previousColor = colorAbove;

      // If this is the end of a valueY, see if we can make conclusions about our color maps.
      if (point.isTrailingEdge(frame)) {

        for (AxeColor middleRowColor : middleRowColors) {
          runner.onPixel(middleRowColor, previousColor);
          previousColor = middleRowColor;
        }

        for (AxeColor belowMiddleRowColor : belowMiddleRowColors) {
          runner.onPixel(belowMiddleRowColor, previousColor);
          previousColor = belowMiddleRowColor;
        }

        final ColorContrastResult newEntry = runner.onRowEnd();

        if (result == null) {
          result = newEntry;
        }

        if (newEntry.confidence.equals(ColorContrastRunner.Confidence.HIGH)) {
          return newEntry;
        } else if (newEntry.confidence.equals(ColorContrastRunner.Confidence.MID)
            && result.confidence.equals(ColorContrastRunner.Confidence.LOW)) {
          result = newEntry;
        }
      }
    }

    return result;
  }
}
