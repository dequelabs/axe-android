package com.deque.axe.android.wrappers;

import com.deque.axe.android.colorcontrast.AxeColor;

import org.junit.Assert;
import org.junit.Test;

public class AxeColorTest {

  @Test
  public void compareTo_different() {
    Assert.assertNotEquals(new AxeColor(Integer.MAX_VALUE), new AxeColor(Integer.MIN_VALUE));
  }

  @Test
  public void compareTo_same() {
    Assert.assertEquals(new AxeColor(Integer.MIN_VALUE), new AxeColor(Integer.MIN_VALUE));
    Assert.assertEquals(new AxeColor(Integer.MAX_VALUE), new AxeColor(Integer.MAX_VALUE));
  }

  @Test
  public void toHex_Black() {
    Assert.assertEquals("0", AxeColor.BLACK.toHex());
  }

  @Test
  public void toHex_White() {
    Assert.assertEquals("ffffffff", AxeColor.WHITE.toHex());
  }

  @Test
  public void toHex_Random() {
    int colorInt = 11250603;
    Assert.assertEquals("ababab", new AxeColor(colorInt).toHex());
  }
}