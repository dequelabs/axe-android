package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SwitchNameTest {

  @Mock
  private AxeProps axeProps;

  private SwitchName subject;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new SwitchName();
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
}