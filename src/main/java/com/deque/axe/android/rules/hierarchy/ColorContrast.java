package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.AxeContext;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.colorcontrast.AxeColor;
import com.deque.axe.android.colorcontrast.AxeImage;
import com.deque.axe.android.colorcontrast.ColorContrastResult;
import com.deque.axe.android.colorcontrast.ColorContrastRunner.Confidence;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.rules.hierarchy.base.InformativeView;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeProps.Name;

public class ColorContrast extends InformativeView {

  private transient AxeImage axeBitmap;

  public ColorContrast() {
    super(AxeStandard.WCAG_21, AxeImpact.SERIOUS,
        "Text adequately contrasts with its background.");
  }

  public void setup(final AxeContext axeContext, final AxeProps axeProps) {
    axeBitmap = axeContext.screenshot;
  }

  @Override
  public void collectProps(AxeView axeView, AxeProps axeProps) {

    super.collectProps(axeView, axeProps);

    axeProps.put(AxeProps.Name.VISIBLE_TEXT, axeView.text);

    if (AxeTextUtils.isNullOrEmpty(axeView.text)) {
      return;
    }

    if (!axeView.boundsInScreen.isWithin(axeBitmap.frame())) {
      return;
    }

    try {
      ColorContrastResult result = axeBitmap.runColorContrastCalculation(axeView.boundsInScreen);
      AxeColor background = result.getMostLikelyBackgroundColor();
      AxeColor foreground = result.getMostLikelyTextColor();

      axeProps.put(Name.CLASS_NAME, axeView.className);
      axeProps.put(Name.COLOR_BACKGROUND, background);
      axeProps.put(Name.COLOR_FOREGROUND, foreground);
      axeProps.put(Name.CONFIDENCE, result.getConfidence());

      if (background != null && foreground != null) {
        axeProps.put(Name.COLOR_CONTRAST, background.contrast(foreground));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String runRule(AxeProps axeProps) {

    String confidence = axeProps.get(Name.CONFIDENCE, String.class);

    if (EditTextValue.EDITABLE_TYPE_NAMES.contains(axeProps.get(Name.CLASS_NAME, String.class))) {
      return AxeStatus.INCOMPLETE;
    }

    if (confidence == null || !confidence.contains(Confidence.HIGH)) {
      return AxeStatus.PASS;
    }

    double contrast = axeProps.get(Name.COLOR_CONTRAST, Double.class);

    if (contrast < 3.0) {
      return AxeStatus.FAIL;
    }

    if (contrast < 4.5) {
      return AxeStatus.INCOMPLETE;
    }

    return AxeStatus.PASS;
  }
}
