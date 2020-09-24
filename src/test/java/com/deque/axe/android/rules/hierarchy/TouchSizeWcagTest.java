package com.deque.axe.android.rules.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeRect;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TouchSizeWcagTest {

  @Mock
  private AxeProps axeProps;

  @Mock
  private AxeRect axeRect;

  private TouchSizeWcag subject;

  /**
   * setup to initialize test subject.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(axeProps.get(AxeProps.Name.DPI, Float.class)).thenReturn(2.625f);
    when(axeProps.get(AxeProps.Name.FRAME, AxeRect.class)).thenReturn(axeRect);
    when(axeRect.height()).thenReturn(147);
    when(axeRect.width()).thenReturn(147);

    subject = new TouchSizeWcag();
  }

  @Test
  public void runRule_isRendered() {
    when(axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.INCOMPLETE);
  }

  @Test
  public void runRule_isOffScreen() {
    when(axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class)).thenReturn(true);
    when(axeProps.get(AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.INCOMPLETE);
  }

  @Test
  public void runRule_isPartiallyVisible() {
    when(axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class)).thenReturn(true);

    assertEquals(subject.runRule(axeProps), AxeStatus.INCOMPLETE);
  }

  @Test
  public void runRule_expectedSizeLessThanAdjustedHeightAndExpectedSizeLessThanAdjustedWidth() {
    when(axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }

  @Test
  public void runRule_expectedSizeGreaterThanAdjustedHeight() {
    when(axeRect.height()).thenReturn(90);
    when(axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }

  @Test
  public void runRule_expectedSizeGreaterThanAdjustedWidth() {
    when(axeRect.width()).thenReturn(90);
    when(axeProps.get(AxeProps.Name.IS_RENDERED, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_OFF_SCREEN, Boolean.class)).thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_PARTIALLY_VISIBLE, Boolean.class)).thenReturn(false);

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }

  @Test
  public void notVisibleToUser_returnsFalse() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.Switch");
    when(axeProps.get(AxeProps.Name.IS_VISIBLE_TO_USER, Boolean.class))
            .thenReturn(false);
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(false);
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class))
            .thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }

  @Test
  public void overRidesAccessibilityDelegate_returnsFalse() {
    when(axeProps.get(AxeProps.Name.CLASS_NAME, String.class))
            .thenReturn("android.widget.Switch");
    when(axeProps.get(AxeProps.Name.IS_VISIBLE_TO_USER, Boolean.class))
            .thenReturn(true);
    when(axeProps.get(AxeProps.Name.OVERRIDES_ACCESSIBILITY_DELEGATE, Boolean.class))
            .thenReturn(true);
    when(axeProps.get(AxeProps.Name.IS_CLICKABLE, Boolean.class))
            .thenReturn(true);

    assertFalse(subject.isApplicable(axeProps));
  }
}