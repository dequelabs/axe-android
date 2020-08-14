package com.deque.axe.android.wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

public class AxeRectTest {

  @Test
  public void binaryRowSearch() {

    final int width = 100;
    final int height = 20;
    final int origin = 0;

    final List<Integer> rowInspectionOrder = new ArrayList<>();

    rowInspectionOrder.add(10);
    rowInspectionOrder.add(5);
    rowInspectionOrder.add(15);

    count = 0;

    AxeRect axeRect = new AxeRect(origin, width, origin, height);

    axeRect.binaryRowSearch(5).forEach(new Consumer<AxePoint>() {

      int currentRow = -1;

      @Override
      public void accept(AxePoint point) {

        count++;

        if (currentRow != point.valueY) {

          final int nextExpectedRow = rowInspectionOrder.get(0);

          Assert.assertEquals(nextExpectedRow, point.valueY);

          currentRow = point.valueY;

          rowInspectionOrder.remove(0);
        }
      }
    });

    Assert.assertEquals(303, count);
  }

  private int count = 0;

  @Test
  public void test_NonOrigin() {

    count = 0;

    AxeRect axeRect = new AxeRect(10, 20, 10, 12);

    axeRect.binaryRowSearch(1).forEach(
        point -> count++
    );

    Assert.assertEquals(11, count);
  }

  @Test
  public void test_EqualityCheck() {

    count = 0;

    AxeRect axeRect = new AxeRect(10, 20, 10, 12);

    AxeRect axeRect2 = new AxeRect(axeRect);

    Assert.assertEquals(axeRect, axeRect2);
    Assert.assertNotEquals(axeRect, new AxeRect(11, 20, 10, 12));
  }

  @Test
  public void testEqualityTest() {

  }

  @Test
  public void rectanglesOverlap_overlapDetected() {
    AxeRect perfectOverlap1 = new AxeRect(0, 100, 0, 100);
    AxeRect perfectOverlap2 = new AxeRect(0, 100, 0, 100);

    AxeRect overlapsFromTop1 = new AxeRect(0, 10, 0, 10);
    AxeRect overlapsFromTop2 = new AxeRect(0, 10, 5, 15);

    AxeRect overlapsFromBottom1 = new AxeRect(0, 999, 5, 10);
    AxeRect overlapsFromBottom2 = new AxeRect(0, 999, 0, 6);

    AxeRect overlapsHorizontally1 = new AxeRect(0, 10, 0, 10);
    AxeRect overlapsHorizontally2 = new AxeRect(0, 5, 0, 10);

    AxeRect overlapsHorizontally3 = new AxeRect(5, 15, 0, 10);
    AxeRect overlapsHorizontally4 = new AxeRect(0, 10, 0, 10);

    AxeRect overlapsDiagonally1 = new AxeRect(0, 10, 0, 10);
    AxeRect overlapsDiagonally2 = new AxeRect(9, 11, 9, 11);

    AxeRect overlapsDiagonally3 = new AxeRect(10, 20, 0, 10);
    AxeRect overlapsDiagonally4 = new AxeRect(9, 11, 9, 11);

    Assert.assertTrue(perfectOverlap1.overlaps(perfectOverlap2));
    Assert.assertTrue(perfectOverlap2.overlaps(perfectOverlap1));

    Assert.assertTrue(overlapsFromTop1.overlaps(overlapsFromTop2));
    Assert.assertTrue(overlapsFromTop2.overlaps(overlapsFromTop1));

    Assert.assertTrue(overlapsFromBottom1.overlaps(overlapsFromBottom2));
    Assert.assertTrue(overlapsFromBottom2.overlaps(overlapsFromBottom1));

    Assert.assertTrue(overlapsHorizontally1.overlaps(overlapsHorizontally2));
    Assert.assertTrue(overlapsHorizontally2.overlaps(overlapsHorizontally1));

    Assert.assertTrue(overlapsHorizontally3.overlaps(overlapsHorizontally4));
    Assert.assertTrue(overlapsHorizontally4.overlaps(overlapsHorizontally3));

    Assert.assertTrue(overlapsDiagonally1.overlaps(overlapsDiagonally2));
    Assert.assertTrue(overlapsDiagonally2.overlaps(overlapsDiagonally1));

    Assert.assertTrue(overlapsDiagonally3.overlaps(overlapsDiagonally4));
    Assert.assertTrue(overlapsDiagonally4.overlaps(overlapsDiagonally3));
  }

  @Test
  public void rectanglesDoNotOverlap_overlapNotReported() {
    AxeRect noOverlap1 = new AxeRect(0, 10, 0, 10);
    AxeRect noOverlap2 = new AxeRect(0, 10, 11, 21);

    AxeRect justTouchingRtL1 = new AxeRect(0, 5, 0, 10);
    AxeRect justTouchingRtL2 = new AxeRect(6, 11, 0, 10);

    AxeRect justTouchingCorners1 = new AxeRect(0, 5, 0, 5);
    AxeRect justTouchingCorners2 = new AxeRect(6, 11, 6, 11);

    AxeRect overlapsHorizontalButNotVertical1 = new AxeRect(0, 10, 0, 10);
    AxeRect overlapsHorizontalButNotVertical2 = new AxeRect(0, 10, 11, 20);

    Assert.assertFalse(noOverlap1.overlaps(noOverlap2));
    Assert.assertFalse(noOverlap2.overlaps(noOverlap1));

    Assert.assertFalse(justTouchingRtL1.overlaps(justTouchingRtL2));
    Assert.assertFalse(justTouchingRtL2.overlaps(justTouchingRtL1));

    Assert.assertFalse(justTouchingCorners1.overlaps(justTouchingCorners2));
    Assert.assertFalse(justTouchingCorners2.overlaps(justTouchingCorners1));

    Assert.assertFalse(overlapsHorizontalButNotVertical1.overlaps(overlapsHorizontalButNotVertical2));
    Assert.assertFalse(overlapsHorizontalButNotVertical2.overlaps(overlapsHorizontalButNotVertical1));
  }
}