package com.deque.axe.android.rules.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EditTextNameTest {

  @Mock
  private AxeProps axeProps;

  private EditTextName subject;

  /**
   * setup to initialize test subject.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    subject = new EditTextName();
  }

  @Test
  public void labelIsNotNullOrEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.LABELED_BY, String.class)).thenReturn("label");

    assertEquals(subject.runRule(axeProps), AxeStatus.PASS);
  }

  @Test
  public void labelIsEmpty_runRule() {
    when(axeProps.get(AxeProps.Name.LABELED_BY, String.class)).thenReturn("");

    assertEquals(subject.runRule(axeProps), AxeStatus.FAIL);
  }
}