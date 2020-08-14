package com.deque.axe.android;

import com.deque.axe.android.colorcontrast.AxeImage;
import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.utils.RuleUtils;
import com.deque.axe.android.wrappers.AxeEventStream;

/**
 * Manages global state for a set of tests relating back to one particular test run.
 */
public class AxeContext implements JsonSerializable {

  public final AxeView axeView;

  public final AxeDevice axeDevice;

  public final AxeImage screenshot;

  public final AxeEventStream axeEventStream;

  @SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
  public final AxeMetaData axeMetaData;

  /**
   * Construct an Axe Context.
   * @param axeView The serializable view hierarchy at the time the context was built.
   * @param axeDevice The device the context was built on.
   * @param screenshot The screenshot at the time the Context was built.
   * @param axeEventStream The AccessibilityEvent Stream since the last view refresh.
   */
  public AxeContext(
      final AxeView axeView,
      final AxeDevice axeDevice,
      final AxeImage screenshot,
      final AxeEventStream axeEventStream
  ) {
    this.axeView = axeView;
    this.axeDevice = axeDevice;
    this.screenshot = screenshot;
    this.axeEventStream = axeEventStream;

    this.axeMetaData = new AxeMetaData(
        axeView.appIdentifier(),
        axeView.getScreenTitle(),
        System.currentTimeMillis()
    );

    RuleUtils.setViews(axeView, axeDevice);
  }
}
