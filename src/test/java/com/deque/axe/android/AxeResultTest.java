package com.deque.axe.android;

import com.deque.axe.android.AxeRuleResult.Matcher;
import com.deque.axe.android.constants.AxeStatus;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class AxeResultTest {

  @Test
  public void test_QueryOnlyViolations() {
    AxeResult axeResult = new AxeResult(null, null);

    axeResult.add(new AxeRuleResult(AxeStatus.INAPPLICABLE, null, null, null));
    axeResult.add(new AxeRuleResult(AxeStatus.FAIL, null, null, null));
    axeResult.add(new AxeRuleResult(AxeStatus.PASS, null, null, null));
    axeResult.add(new AxeRuleResult(AxeStatus.INCOMPLETE, null, null, null));

    List<AxeRuleResult> ruleResultList = axeResult.query(
        ruleResult -> ruleResult.status.contentEquals(AxeStatus.FAIL)
    );

    Assert.assertEquals(1, ruleResultList.size());
    Assert.assertEquals(AxeStatus.FAIL, ruleResultList.get(0).status);
  }

}