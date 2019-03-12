package com.deque.axe.android;

import com.deque.axe.android.wrappers.AxeViewBuilder;
import org.junit.Assert;
import org.junit.Test;

public class AxeViewTest {

  @Test
  public void query() {

    AxeViewBuilder parent = new AxeViewBuilder();
    AxeViewBuilder child = new AxeViewBuilder();

    parent.contentDescription("No empty.");
    child.contentDescription("I love puppies.");

    parent.addChild(child);

    AxeView expected = child.build();

    AxeView actual = parent.build().query(
        view -> view.contentDescription.contentEquals("I love puppies.")).get(0);

    Assert.assertEquals(expected, actual);
  }
}