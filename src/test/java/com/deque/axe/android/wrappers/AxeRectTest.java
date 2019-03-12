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
}