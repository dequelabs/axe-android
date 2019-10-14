package com.deque.axe.android;

import com.deque.axe.android.constants.Constants;
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

  @Test
  public void screenTitle_basic() {

    AxeViewBuilder parent = new AxeViewBuilder();

    parent.viewIdResourceName("id:content");

    AxeViewBuilder child = new AxeViewBuilder();

    child.viewIdResourceName("My Title");

    parent.addChild(child);

    AxeView axeView = parent.build();

    Assert.assertEquals("My Title", axeView.getScreenTitle());
  }

  @Test
  public void screenTitle_noContentView() {

    AxeViewBuilder parent = new AxeViewBuilder();

    parent.viewIdResourceName("id:not_content_view");

    AxeViewBuilder child = new AxeViewBuilder();

    child.viewIdResourceName("Not a Title");

    parent.addChild(child);

    AxeView axeView = parent.build();

    Assert.assertEquals(Constants.DEFAULT_SCREEN_TITLE, axeView.getScreenTitle());
  }
}