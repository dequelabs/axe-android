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

public class EditTextValueTest {

  @Mock
  private AxeProps axeProps;

  private EditTextValue subject;

  /**
   * setup to initialize test subject.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new EditTextValue();
  }

  @Test
  public void switchClass_isApplicable() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.CheckBox");

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void editTextClass_isApplicable() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.EditText");
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(false);

    assertTrue(subject.isApplicable(axeProps));
  }

  @Test
  public void editTextClass_overridesAccessibilityDelegate_notApplicable() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.EditText");
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void emptyContentDescription_runRule() {
    when(axeProps.get(AxeProps.Name.CONTENT_DESCRIPTION, String.class)).thenReturn("");

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }

  @Test
  public void nonEmptyContentDescription_runRule() {
    when(axeProps.get(AxeProps.Name.CONTENT_DESCRIPTION, String.class))
            .thenReturn("Content Description");

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }

  @Test
  public void overRidesAccessibilityDelegate_returnsFalse() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.Switch");
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }
}