package com.deque.axe.android;

import static org.junit.Assert.assertEquals;

import com.deque.axe.android.colorcontrast.AxeColor;
import com.deque.axe.android.colorcontrast.ColorContrastResult;
import com.deque.axe.android.colorcontrast.ColorContrastRunner;
import com.deque.axe.android.colorcontrast.ColorContrastRunner.Confidence;
import com.deque.axe.android.utils.AxeFile;
import com.deque.axe.android.utils.AxeJankyPng;
import com.deque.axe.android.wrappers.AxeRect;

import org.junit.Test;

public class ColorContrastTests {

  class ImageResourceResult {

    final String resourcePath;
    final AxeColor expectedBackgroundColor;
    final AxeColor expecteTextColor;
    final @Confidence String confidence;

    ImageResourceResult(
        final String resourceName,
        final AxeColor expectedBackgroundColor,
        final AxeColor expectedTextColor,
        final @Confidence String confidence
    ) {
      this.resourcePath = "color_contrast_tests/" + resourceName;
      this.expectedBackgroundColor = expectedBackgroundColor;
      this.expecteTextColor = expectedTextColor;
      this.confidence = confidence;
    }

    void runTest() {
      runTest(null);
    }

    void runTest(AxeRect frame) {

      final String fileName = resourcePath;

      final AxeFile axeFile = new AxeFile(fileName);

      final AxeJankyPng axeJankyPng = new AxeJankyPng(axeFile);

      ColorContrastResult entry = axeJankyPng.runColorContrastCalculation(frame);

      assertEquals(fileName, confidence, entry.getConfidence());

      assertEquals(fileName, expectedBackgroundColor, entry.getMostLikelyBackgroundColor());

      assertEquals(fileName, expecteTextColor, entry.getMostLikelyTextColor());
    }
  }

  @Test
  public void imageTest_textWithIcon() {
    new ImageResourceResult(
        "text_with_icon.png",
        new AxeColor(255,255,255,255),
        new AxeColor(255,0,92,208),
        ColorContrastRunner.Confidence.HIGH
    ).runTest();
  }

  @Test
  public void imageTest_intellijCaptureHard() {
    new ImageResourceResult(
        "intellij_capture_hard.png",
        new AxeColor("fff5f5f5"),
        new AxeColor("ff113247"),
        Confidence.LOW
    ).runTest();
  }

  @Test
  public void imageTest_intellijCapture() {
    new ImageResourceResult(
        "intellij_capture.png",
        new AxeColor(255,245,245,245),
        new AxeColor(255,17,50,71),
        Confidence.HIGH
    ).runTest();
  }

  @Test
  public void imageTest_whiteTextBlueBackground() {
    new ImageResourceResult(
        "white_text_blue_background.png",
        new AxeColor(255,255,255,255),
        new AxeColor(255,0,90,208),
        ColorContrastRunner.Confidence.HIGH
    ).runTest();
  }

  @Test
  public void imageTest_largeWhiteTextBlueBackground() {
    new ImageResourceResult(
        "white_text_blue_background_large.png",
        new AxeColor(255,255,255,255),
        new AxeColor(255,0,90,208),
        ColorContrastRunner.Confidence.HIGH
    ).runTest();
  }

  @Test
  public void imageTest_textOnlyAtTop() {
    new ImageResourceResult(
        "text_only_at_top.png",
        new AxeColor(255,245,247,246),
        new AxeColor(255,238,240,239),
        ColorContrastRunner.Confidence.HIGH
    ).runTest();
  }

  @Test
  public void example_Inaccessible() {
    new ImageResourceResult(
        "color_contrast_example.png",
        new AxeColor(255,242,242,244),
        new AxeColor(255,153,153,153),
        ColorContrastRunner.Confidence.HIGH
    ).runTest(new AxeRect(126, 764, 682, 756));
  }

  @Test
  public void example_ManualTestingRequired() {
    new ImageResourceResult(
        "color_contrast_example.png",
        new AxeColor(255,136,134,102),
        new AxeColor(255,242,242,244),
        ColorContrastRunner.Confidence.HIGH
    ).runTest(new AxeRect(126, 1017, 966, 1017));
  }

  @Test
  public void example_Accessible() {
    new ImageResourceResult(
        "color_contrast_example.png",
        new AxeColor(255,0,0,0),
        new AxeColor(255,242,242,244),
        ColorContrastRunner.Confidence.HIGH
    ).runTest(new AxeRect(126, 764, 1553, 1627));
  }
}