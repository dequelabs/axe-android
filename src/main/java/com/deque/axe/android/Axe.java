package com.deque.axe.android;

import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.utils.AxeTree;
import com.deque.axe.android.wrappers.AxeProps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Main class for the Axe library. Is in charge of managing AxeRules and returning AxeResults.
 */
public class Axe {

  final AxeConf axeConf;

  public Axe(final AxeConf axeConf) {

    this.axeConf = axeConf;
  }

  /**
   * Main axe run function.
   * @param axeContext The {@link AxeContext} for the run.
   * @return An {@link AxeResult} object.
   */
  public AxeResult run(final AxeContext axeContext) {

    AxeResult axeResult = new AxeResult(axeConf, axeContext);

    List<AxeRuleViewHierarchy> rules = new LinkedList<>();

    List<AxeRuleStateful> statefulRules = new LinkedList<>();

    for (AxeRule rule : axeConf.ruleInstances()) {
      if (rule instanceof AxeRuleStateful) {
        statefulRules.add((AxeRuleStateful) rule);
      } else if (rule instanceof AxeRuleViewHierarchy) {
        rules.add((AxeRuleViewHierarchy) rule);
      }
    }

    final Map<AxeRuleViewHierarchy, AxeProps> contextProps = new HashMap<>();

    for (AxeRuleViewHierarchy axeRule : rules) {
      final AxeProps axeProps = new AxeProps();
      contextProps.put(axeRule, axeProps);
      axeRule.setup(axeContext, axeProps);
    }

    axeContext.axeView.forEachRecursive(axeView -> {

      for (AxeRuleViewHierarchy axeRule : rules) {

        try {

          final AxeProps viewProps = new AxeProps();

          viewProps.putAll(contextProps.get(axeRule));

          axeRule.collectProps(axeView, viewProps);

          if (axeRule.isApplicable(viewProps)) {
            final String status = axeRule.runRule(viewProps);
            axeResult.add(new AxeRuleResult(status, axeRule, viewProps, axeView));
          }
        } catch (Exception e) {
          handleException(e, axeResult, axeRule, axeView);
        }
      }

      return AxeTree.CallBackResponse.CONTINUE;
    });

    for (AxeRuleViewHierarchy axeRule : rules) {
      axeRule.tearDown();
    }

    for (AxeRuleStateful axeRule : statefulRules) {
      try {
        axeResult.add(axeRule.run(axeContext.axeEventStream));
      } catch (Exception e) {
        handleException(e, axeResult, axeRule, axeContext.axeView);
      }
    }

    return axeResult;
  }

  private void handleException(final Exception e,
      final AxeResult axeResult,
      final AxeRule axeRule,
      final AxeView axeView) {

    e.printStackTrace();

    AxeProps props = new AxeProps();

    props.put(AxeProps.Name.EXCEPTION, e.getLocalizedMessage());
    props.put(AxeProps.Name.STACK_TRACE, e.getStackTrace());

    axeResult.add(new AxeRuleResult(AxeStatus.INCOMPLETE, axeRule, props, axeView));

  }
}
