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

public class ImageViewNameTest {

  @Mock
  private AxeProps axeProps;

  private ImageViewName subject;

  /**
   * setup to initialize test subject.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new ImageViewName();
  }

  @Test
  public void imageViewClass_isApplicable() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.ImageView");
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_COMPOSE_VIEW, Boolean.class))
            .thenReturn(false);

    assertTrue(subject.isApplicable(axeProps));
  }

  @Test
  public void imageViewClass_overridesAccessibilityDelegate_notApplicable() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.ImageView");
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void switchClass_isApplicable() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.Switch");

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void isNotImportantForAccessibility_runRule() {
    when(axeProps.get(AxeProps.Name.IMPORTANT, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }

  @Test
  public void isImportantForAccessibilityEmptyContentDesc_runRule() {
    when(axeProps.get(AxeProps.Name.IMPORTANT, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.CONTENT_DESCRIPTION, String.class)).thenReturn("");

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }

  @Test
  public void isImportantForAccessibilityNonEmptyContentDesc_runRule() {
    when(axeProps.get(AxeProps.Name.IMPORTANT, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.CONTENT_DESCRIPTION, String.class)).thenReturn("Cont Desc");

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }
}