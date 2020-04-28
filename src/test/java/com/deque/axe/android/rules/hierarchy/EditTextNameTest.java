package com.deque.axe.android.rules.hierarchy;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeProps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EditTextNameTest {

  @Mock
  private AxeProps axeProps;

  private EditTextName subject;

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