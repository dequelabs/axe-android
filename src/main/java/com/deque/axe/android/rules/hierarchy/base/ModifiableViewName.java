package com.deque.axe.android.rules.hierarchy.base;

import static com.deque.axe.android.wrappers.AxeProps.Name;

import android.support.annotation.CallSuper;

import com.deque.axe.android.AxeRuleViewHierarchy;
import com.deque.axe.android.AxeView;
import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.wrappers.AxeProps;


public abstract class ModifiableViewName extends AxeRuleViewHierarchy {

  private final @AndroidClassNames String applicableClass;

  /**
   * Views that have modifiable Values should get their name from a nearby Label.
   * @param className The className of a modifiable view type.
   */
  public ModifiableViewName(final @AndroidClassNames String className) {

    super(AxeStandard.BEST_PRACTICE, AxeImpact.MODERATE,
        "Views that have modifiable Values should get their name from a nearby Label.");

    applicableClass = className;
  }

  @Override
  @CallSuper
  public void collectProps(AxeView axeView, AxeProps axeProps) {
    axeProps.put(Name.LABELED_BY, axeView.speakableTextOfLabeledBy());
    axeProps.put(Name.CLASS_NAME, axeView.className);
  }

  @Override
  @CallSuper
  public boolean isApplicable(AxeProps axeProps) {

    final String className = axeProps.get(Name.CLASS_NAME, String.class);

    return applicableClass.contentEquals(className);
  }
}
