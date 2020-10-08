package com.deque.axe.android.rules.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ActiveViewNameTest {

  private ActiveViewName subject;

  @Mock
  private AxeProps axeProps;

  /**
   * setup to initialize test subject.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new ActiveViewName();
  }

  @Test
  public void isApplicable_classNameIsButton() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class)).thenReturn("android.widget.Button");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(false);

    assertTrue(subject.isApplicable(axeProps));
  }

  @Test
  public void isApplicable_overridesAccessibilityDelegate_returnsFalse() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class)).thenReturn("android.widget.Button");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void isApplicable_classNameIsCheckBox() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.CheckBox");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void isApplicable_classNameIsSwitch() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class)).thenReturn("android.widget.Switch");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void viewNotClickable_runRule() {
    when(axeProps.get(AxeProps.Name.SPEAKABLE_TEXT, String.class)).thenReturn("Speakable Text");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.INAPPLICABLE);
  }

  @Test
  public void speakableTextEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.SPEAKABLE_TEXT, String.class)).thenReturn("");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(true);

    assertEquals(AxeStatus.FAIL, subject.runRule(axeProps));
  }

  @Test
  public void runRule() {
    when(axeProps.get(AxeProps.Name.SPEAKABLE_TEXT, String.class)).thenReturn("Speakable Text");
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class)).thenReturn(true);

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }
}