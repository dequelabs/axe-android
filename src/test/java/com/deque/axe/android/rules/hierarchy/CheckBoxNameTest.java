package com.deque.axe.android.rules.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CheckBoxNameTest {

  private CheckBoxName subject;

  @Mock
  private AxeProps axeProps;

  /**
   * setup to initialize test subject.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new CheckBoxName();
  }

  @Test
  public void visibleTextEmpty_labelIsEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.VISIBLE_TEXT, String.class)).thenReturn("");
    when(axeProps.get(AxeProps.Name.LABELED_BY, String.class)).thenReturn("");

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }

  @Test
  public void visibleTextNotEmpty_labelEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.VISIBLE_TEXT, String.class)).thenReturn("VISIBLE TEXT");
    when(axeProps.get(AxeProps.Name.LABELED_BY, String.class)).thenReturn("");

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }

  @Test
  public void visibleTextEmpty_labelNotEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.VISIBLE_TEXT, String.class)).thenReturn("");
    when(axeProps.get(AxeProps.Name.LABELED_BY, String.class)).thenReturn("Label");

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }

  @Test
  public void visibleTextNotEmpty_labelNotEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.VISIBLE_TEXT, String.class)).thenReturn("VISIBLE TEXT");
    when(axeProps.get(AxeProps.Name.LABELED_BY, String.class)).thenReturn("Label");

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }

  @Test
  public void notVisibleToUser_returnsFalse() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class)).thenReturn("android.widget.Switch");
    when(axeProps.get(AxeProps.Name.IS_VISIBLE_TO_USER, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class)).thenReturn(false);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void overRidesAccessibilityDelegate_returnsFalse() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class)).thenReturn("android.widget.Switch");
    when(axeProps.get(AxeProps.Name.IS_VISIBLE_TO_USER, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class)).thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }
}