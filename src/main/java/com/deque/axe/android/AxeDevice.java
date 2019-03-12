package com.deque.axe.android;

public class AxeDevice {

  public final float dpi;

  public final String name;

  public final String osVersion;

  public final int screenHeight;

  public final int screenWidth;

  /**
   * A collection of information useful for identifying a particular device.
   * @param dpi Pixel Density of the device.
   * @param name The common name of the device.
   * @param osVersion The OS Version installed on the device.
   * @param screenHeight The height in pixels of the device.
   * @param screenWidth The width in pixels of the device.
   */
  public AxeDevice(
      final float dpi,
      final String name,
      final String osVersion,
      final int screenHeight,
      final int screenWidth
  ) {
    this.dpi = dpi;
    this.name = name;
    this.osVersion = "Android " + osVersion;
    this.screenHeight = screenHeight;
    this.screenWidth = screenWidth;
  }
}
