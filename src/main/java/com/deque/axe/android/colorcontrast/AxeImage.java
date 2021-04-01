package com.deque.axe.android.colorcontrast;

import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxePoint;
import com.deque.axe.android.wrappers.AxeRect;

public abstract class AxeImage implements JsonSerializable {

  public abstract AxeRect frame();

  public abstract AxeColor pixel(final int x, final int y);

  @SuppressWarnings("unused")
  public abstract String toBase64Png();

  @SuppressWarnings("unused")
  public ColorContrastResult runColorContrastCalculation() {
    return runColorContrastCalculation(frame());
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

    for (final AxePoint point : frame.binaryRowSearch(5)) {

      //If this is a new valueY, clear stuff out and start over.
      if (point.isLeadingEdge(frame)) {
        runner.onRowBegin();
      }

      final AxeColor color = pixel(point.valueX, point.valueY);
      final AxeColor colorAbove = pixel(point.valueX, point.valueY - 1);
      final AxeColor colorOneMoreAbove = pixel(point.valueX, point.valueY - 2);
      final AxeColor colorBelow = pixel(point.valueX, point.valueY + 1);
      final AxeColor colorOneMoreBelow = pixel(point.valueX, point.valueY + 2);

      runner.onPixel(colorOneMoreAbove, previousColor);
      previousColor = colorOneMoreAbove;
      runner.onPixel(colorAbove, previousColor);
      previousColor = colorAbove;
      runner.onPixel(color, previousColor);
      previousColor = color;
      runner.onPixel(colorBelow, previousColor);
      previousColor = colorBelow;
      runner.onPixel(colorOneMoreBelow, previousColor);
      previousColor = colorOneMoreBelow;

      // If this is the end of a valueY, see if we can make conclusions about our color maps.
      if (point.isTrailingEdge(frame)) {

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
