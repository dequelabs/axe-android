package com.deque.axe.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.Constants;
import com.deque.axe.android.wrappers.AxeRect;
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

    assertEquals(expected, actual);
  }

  @Test
  public void screenTitle_basic() {

    AxeViewBuilder parent = new AxeViewBuilder();

    parent.viewIdResourceName("android:id/content");

    AxeViewBuilder child = new AxeViewBuilder();

    child.viewIdResourceName("My Title");

    parent.addChild(child);

    AxeView axeView = parent.build();

    assertEquals("My Title", axeView.getScreenTitle());
  }

  @Test
  public void screenTitle_noContentView() {

    AxeViewBuilder parent = new AxeViewBuilder();

    parent.viewIdResourceName("id:not_content_view");

    AxeViewBuilder child = new AxeViewBuilder();

    child.viewIdResourceName("Not a Title");

    parent.addChild(child);

    AxeView axeView = parent.build();

    assertEquals(Constants.DEFAULT_SCREEN_TITLE, axeView.getScreenTitle());
  }

  @Test
  public void isPartiallyVisible_buttonIsNotOffScreen_returnsFalse() {
    AxeRect parentRect = new AxeRect(0, 100, 0, 200);
    AxeViewBuilder parent = new AxeViewBuilder();
    parent.viewIdResourceName("android:id/content");
    parent.boundsInScreen(parentRect);

    AxeRect childRect = new AxeRect(1, 45, 1, 45);
    AxeViewBuilder child = new AxeViewBuilder();
    child.viewIdResourceName("id:button1");
    child.boundsInScreen(childRect);

    parent.addChild(child);

    AxeView axeView = child.build();

    Assert.assertFalse(axeView.isPartiallyVisible(axeView.boundsInScreen, 220, 101));
  }

  @Test
  public void isOffScreen_viewOutOfScreen_returnsTrue() {
    AxeRect parentRect = new AxeRect(-1, 100, 0, 200);
    AxeViewBuilder parent = new AxeViewBuilder();
    parent.viewIdResourceName("android:id/content");
    parent.boundsInScreen(parentRect);

    AxeView axeView = parent.build();

    Assert.assertTrue(axeView.isOffScreen(axeView.boundsInScreen, 220, 100));
  }

  @Test
  public void isOffScreen_viewInsideScreen_returnsFalse() {
    AxeRect parentRect = new AxeRect(0, 100, 0, 200);
    AxeViewBuilder parent = new AxeViewBuilder();
    parent.viewIdResourceName("android:id/content");
    parent.boundsInScreen(parentRect);

    AxeView axeView = parent.build();

    Assert.assertFalse(axeView.isOffScreen(axeView.boundsInScreen, 220, 100));
  }

  @Test
  public void calculateProps_switch() {
    AxeViewBuilder labelAxeViewBuilder = new AxeViewBuilder();
    labelAxeViewBuilder.text("Control");

    AxeView labelAxeView = labelAxeViewBuilder.build();

    AxeViewBuilder parent = new AxeViewBuilder();
    parent.text("Control Switch");
    parent.contentDescription("Switch Control");
    parent.labeledBy(labelAxeView);
    parent.isEnabled(true);
    parent.className(AndroidClassNames.SWITCH);

    AxeView axeView = parent.build();

    assertEquals(axeView.calculatedProps.size(), 4);
    assertEquals(axeView.calculatedProps.get("name"), "Control Switch Switch Control Control");
    assertEquals(axeView.calculatedProps.get("role"), "android.widget.Switch");
    assertNull(axeView.calculatedProps.get("value"));
    assertEquals(axeView.calculatedProps.get("state"), "enabled");
  }

  @Test
  public void calculateProps_editText() {
    AxeViewBuilder labelAxeViewBuilder = new AxeViewBuilder();
    labelAxeViewBuilder.text("Enter Name");

    AxeView labelAxeView = labelAxeViewBuilder.build();

    AxeViewBuilder parent = new AxeViewBuilder();
    parent.text(null);
    parent.contentDescription("Name");
    parent.labeledBy(labelAxeView);
    parent.hintText("John");
    parent.className(AndroidClassNames.EDIT_TEXT);

    AxeView axeView = parent.build();

    assertEquals(axeView.calculatedProps.size(), 4);
    assertEquals(axeView.calculatedProps.get("name"), "John Enter Name");
    assertEquals(axeView.calculatedProps.get("role"), "android.widget.EditText");
    assertNull(axeView.calculatedProps.get("value"));
    assertNull(axeView.calculatedProps.get("state"));
  }
}