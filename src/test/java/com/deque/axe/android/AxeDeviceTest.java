package com.deque.axe.android;

import org.junit.Assert;
import org.junit.Test;

public class AxeDeviceTest {

  @Test
  public void construct() {

    final String name = "blah";
    final float dpi = 2.1f;
    final String osVersion = "L";
    final int width = 10;
    final int height = 10;

    AxeDevice axeDevice = new AxeDevice(dpi, name, osVersion, height, width);

    Assert.assertEquals(name, axeDevice.name);
    Assert.assertEquals(dpi, axeDevice.dpi, 0.0);
    Assert.assertEquals("Android " + osVersion, axeDevice.osVersion);
    Assert.assertEquals(width, axeDevice.screenWidth);
    Assert.assertEquals(height, axeDevice.screenHeight);
  }
}