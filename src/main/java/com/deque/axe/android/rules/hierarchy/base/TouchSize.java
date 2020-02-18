package com.deque.axe.android.rules.hierarchy.base;

import com.deque.axe.android.AxeContext;
import com.deque.axe.android.AxeDevice;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeRect;

public abstract class TouchSize extends ActiveView {

  private final int expectedSize;
  private AxeDevice axeDevice;

  protected TouchSize(String standard, int impact, String summary, int expectedSize) {
    super(standard, impact, summary);

    this.expectedSize = expectedSize;
  }

  @Override
  public void setup(AxeContext axeContext, AxeProps axeProps) {
    axeDevice = axeContext.axeDevice;

    axeProps.put(AxeProps.Name.DPI, axeDevice == null ? -1 : axeDevice.dpi);
    axeProps.put(AxeProps.Name.SCREEN_HEIGHT, axeDevice == null ? -1 : axeDevice.screenHeight);
    axeProps.put(AxeProps.Name.SCREEN_WIDTH, axeDevice == null ? -1 : axeDevice.screenWidth);
  }

  @Override
  public void collectProps(AxeView axeView, AxeProps axeProps) {
    super.collectProps(axeView, axeProps);

    axeProps.put(AxeProps.Name.IS_RENDERED,
            axeView.isRendered(axeDevice.dpi, axeDevice.screenHeight, axeDevice.screenWidth));
    axeProps.put(AxeProps.Name.IS_OFF_SCREEN,
            axeView.isOffScreen(axeView.boundsInScreen,
                    axeDevice.screenHeight, axeDevice.screenWidth));
    axeProps.put(AxeProps.Name.IS_PARTIALLY_VISIBLE,
            axeView.isPartiallyVisible(axeView.boundsInScreen,
                    axeDevice.screenHeight, axeDevice.screenWidth));
    axeProps.put(AxeProps.Name.FRAME, axeView.boundsInScreen);
  }

  @Override
  public String runRule(AxeProps axeProps) {

    final float dpi = axeProps.get(AxeProps.Name.DPI, Float.class);
    final AxeRect frame = axeProps.get(AxeProps.Name.FRAME, AxeRect.class);

    final long height = frame.height();
    final long width = frame.width();

    final boolean isRendered = axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class);
    final boolean isOffScreen = axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class);
    final boolean isPartiallyVisible = axeProps.get(
            AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class);

    if (isRendered || isOffScreen || isPartiallyVisible) {
      return AxeStatus.INCOMPLETE;
    }

    final long adjustedHeight = Math.round(height / dpi);
    final long adjustedWidth = Math.round(width / dpi);

    if (adjustedHeight < expectedSize || adjustedWidth < expectedSize) {
      return AxeStatus.FAIL;
    } else {
      return AxeStatus.PASS;
    }
  }
}
