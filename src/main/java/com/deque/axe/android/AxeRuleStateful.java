package com.deque.axe.android;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.wrappers.AxeEventStream;
import com.deque.axe.android.wrappers.AxeProps;

public abstract class AxeRuleStateful extends AxeRule {

  protected AxeEventStream applicableEventStream = new AxeEventStream();

  protected AxeRuleStateful(String standard, int impact, String summary) {
    super(new AxeConf.RuleConf(impact, standard, summary));
  }

  /**
   * Run the given rule on an Accessibility Event Stream.
   * @param axeEventStream The stream of events.
   * @return The result.
   */
  public AxeRuleResult run(AxeEventStream axeEventStream) {

    final AxeProps axeProps = new AxeProps();

    axeProps.put(AxeProps.Name.EVENT_STREAM, applicableEventStream);

    if (axeEventStream == null) {
      return new AxeRuleResult(AxeStatus.INAPPLICABLE, this, axeProps, null);
    } else {
      return new AxeRuleResult(run(axeEventStream, axeProps), this, axeProps, null);
    }
  }

  public abstract @AxeStatus String run(AxeEventStream axeEventStream, AxeProps axeProps);
}
