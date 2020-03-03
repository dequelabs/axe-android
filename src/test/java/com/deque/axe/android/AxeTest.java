package com.deque.axe.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.deque.axe.android.colorcontrast.AxeImage;
import com.deque.axe.android.constants.AxeImpact;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.constants.Constants;
import com.deque.axe.android.utils.AxeFile;
import com.deque.axe.android.utils.AxeJankyPng;
import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeProps.Name;
import com.deque.axe.android.wrappers.AxeRect;
import com.deque.axe.android.wrappers.AxeViewBuilder;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the base Axe class.
 */
public class AxeTest {

  /**
   * Register type adapters so we can read Images in from base 64 strings.
   */
  @Before
  public void setup() {
    JsonSerializable.DEFAULT_BUILDER
        .registerTypeAdapter(AxeImage.class, new TypeAdapter<AxeImage>() {
          @Override
          public void write(JsonWriter out, AxeImage value) {
            throw new RuntimeException("Writing out is not supported for testing.");
          }

          @Override
          public AxeImage read(JsonReader in) throws IOException {

            if (in.peek() == JsonToken.NULL) {
              in.nextNull();
              return null;
            } else {
              return new AxeJankyPng(in.nextString());
            }
          }
        });
  }

  private static class AxeTestRule extends AxeRuleViewHierarchy {

    private boolean hasCalledSetup = false;
    int numCollectPropsCalls = 0;
    int numIsApplicableCalls = 0;
    int numRunRuleCalls = 0;
    private boolean hasCalledTearDown = false;

    protected AxeTestRule() {
      super(AxeStandard.BEST_PRACTICE, AxeImpact.MINOR, "A Test Rule");
    }

    @Override
    public void setup(final AxeContext axeContext, final AxeProps axeProps) {

      Assert.assertFalse(hasCalledSetup);

      hasCalledSetup = true;
    }

    @Override
    public void collectProps(AxeView axeView, AxeProps axeProps) {
      numCollectPropsCalls++;
    }

    @Override
    public boolean isApplicable(AxeProps axeProps) {
      return (numIsApplicableCalls++ % 2) == 0;
    }

    @Override
    public String runRule(AxeProps axeProps) {

      numRunRuleCalls++;

      return AxeStatus.FAIL;
    }

    @Override
    public void tearDown() {

      Assert.assertFalse(hasCalledTearDown);

      hasCalledTearDown = true;

      Assert.assertTrue(hasCalledSetup);
      Assert.assertEquals(numCollectPropsCalls, numIsApplicableCalls);
      Assert.assertEquals(numIsApplicableCalls / 2 + 1, numRunRuleCalls);
      Assert.assertTrue(numCollectPropsCalls > 0);
      Assert.assertTrue(numIsApplicableCalls > 0);
      Assert.assertTrue(numRunRuleCalls > 0);
    }
  }

  @Test
  public void testRun() {

    final AxeConf axeConf = new AxeConf();
    axeConf.customRules.add(AxeTestRule.class);

    Axe axe = new Axe(axeConf);

    AxeResult axeResult = axe.run(new AxeContext(
        new AxeViewBuilder().build(),
        null,
        null,
        null)
    );

    Assert.assertEquals(1, axeResult.axeRuleResults.size());
    Assert.assertTrue(
        axeResult.axeRuleResults.get(0).ruleId.contentEquals(AxeTestRule.class.getSimpleName()));
  }

  public static class AxeRuleThrows extends AxeRuleViewHierarchy {

    protected AxeRuleThrows() {
      super(AxeStandard.BEST_PRACTICE, AxeImpact.BLOCKER, "Blah");
    }

    @Override
    public void collectProps(AxeView axeView, AxeProps axeProps) {

    }

    @Override
    public boolean isApplicable(AxeProps axeProps) {
      return true;
    }

    @Override
    public String runRule(AxeProps axeProps) {
      throw new RuntimeException("blah");
    }
  }

  @Test
  public void testThrownException() {

    Axe axe = new Axe(new AxeConf());

    axe.axeConf.customRules.add(AxeRuleThrows.class);

    AxeResult axeResult = axe.run(new AxeContext(
        new AxeViewBuilder().build(),
        null,
        null,
        null
        )
    );

    Assert.assertTrue(!axeResult.axeRuleResults.isEmpty());
    Assert.assertEquals("blah", axeResult.axeRuleResults.get(0).props.get(Name.EXCEPTION));
  }

  @Test
  public void testDefaultConfig() {

    Axe axe = new Axe(new AxeConf());

    axe.run(new AxeContext(new AxeViewBuilder().build(),
        null,
        null,
        null
        )
    );

    Assert.assertEquals("Default run should run all rules.",
        Constants.AXE_RULE_CLASSES.size(), axe.axeConf.ruleInstances().size());
  }

  @Test
  public void testOneStandardAndOneRule() {

    AxeConf axeConf = new AxeConf();
    axeConf.standards.clear();
    axeConf.standards.add(AxeStandard.WCAG_21);

    Axe axe = new Axe(axeConf);

    final int numRulesBeforeAddingRule = axe.axeConf.ruleInstances().size();

    axeConf.customRules.add(AxeTestRule.class);

    Assert.assertEquals(numRulesBeforeAddingRule + 1,
        new Axe(axeConf).axeConf.ruleInstances().size());
  }

  @Test
  public void jsonTestSpecs() throws IOException {

    ClassLoader classLoader = getClass().getClassLoader();

    assert classLoader != null;

    File file = new AxeFile("test_specs").file;

    File[] fileList = file.listFiles();

    Assert.assertNotNull(fileList);

    for (final File fileEntry : fileList) {

      Path path = fileEntry.toPath();

      final byte[] encoded = Files.readAllBytes(path);

      final String json = new String(encoded);

      final AxeTestSpec testSpec = JsonSerializable.fromJson(json, AxeTestSpec.class);

      final String testInfo = testSpec.testDescription + ": " + path.toString();

      final Axe axe = new Axe(testSpec.axeConf);

      final List<AxeRuleResult> actualResults = axe.run(testSpec.axeContext).axeRuleResults;

      final List<AxeRuleResult> expectedResults = testSpec.axeRuleResults;

      actualResults.sort((o1, o2) -> o1.compareTo(o2));

      expectedResults.sort((o1, o2) -> o1.compareTo(o2));

      // Ensure all expected axeRuleResults are present.
      expectedResults.forEach(axeResult -> {

        final String message = testInfo + "\n"
            + "This result was expected, but not present."
            + axeResult.toJson();

        Assert.assertFalse(message, actualResults.isEmpty());

        final AxeRuleResult other = actualResults.get(0);

        /*
         * TODO: Our Test specs don't have this yet. Need to update test specs. This works for now.
         */
        other.impact = 0;
        axeResult.impact = 0;

        AxeComparator axeComparator = new AxeComparator("File Name: " + fileEntry.getName()
                + ", axeViewId : " + axeResult.axeViewId
                + "\n Expected: " + axeResult
                + "\n Actual: " + other);

        if (axeResult.axeViewId != null && other.axeViewId != null) {
          axeComparator.compareAxeViewId(axeResult.axeViewId, other.axeViewId);
        }

        axeComparator.compareAxeRuleId(axeResult.ruleId, other.ruleId);

        if (axeResult.props != other.props) {
          for (Map.Entry<String, Object> stringObjectEntry : axeResult.props.entrySet()) {
            Object key = ((Map.Entry) stringObjectEntry).getKey();
            if (other.props.containsKey(key)) {
              if (stringObjectEntry.getValue() != null && other.props.get(key) != null) {
                Object expected = stringObjectEntry.getValue();
                Object actual = other.props.get(key);

                switch (key.toString()) {
                  case "className":
                    axeComparator.compareAxePropClassName(expected, actual);
                    break;
                  case "contentDescription":
                    axeComparator.compareAxePropContentDescription(expected, actual);
                    break;
                  case "Screen Dots Per Inch":
                    axeComparator.compareAxePropDpi(expected, actual);
                    break;
                  case "boundsInScreen":
                    axeComparator.compareAxePropFrame(expected, actual);
                    break;
                  case "height":
                    axeComparator.compareAxePropHeight(expected, actual);
                    break;
                  case "isImportantForAccessibility":
                    axeComparator.compareAxePropImportant(expected, actual);
                    break;
                  case "isActive":
                    axeComparator.compareAxePropIsClickable(expected, actual);
                    break;
                  case "isEnabled":
                    axeComparator.compareAxePropIsEnabled(expected, actual);
                    break;
                  case "labeledBy":
                    axeComparator.compareAxePropLabeledBy(expected, actual);
                    break;
                  case "Speakable Text":
                    axeComparator.compareAxePropSpeakableText(expected, actual);
                    break;
                  case "width":
                    axeComparator.compareAxePropWidth(expected, actual);
                    break;
                  case "Exception":
                    axeComparator.compareAxePropException(expected, actual);
                    break;
                  case "Stack Trace":
                    axeComparator.compareAxePropStackTrace(expected, actual);
                    break;
                  case "Applicable Event Stream":
                    axeComparator.compareAxePropEventStream(expected, actual);
                    break;
                  case "AccessibilityEvent":
                    axeComparator.compareAxePropAccessibilityEvent(expected, actual);
                    break;
                  case "Touch Interaction Started":
                    axeComparator.compareAxePropIsTouchStarted(expected, actual);
                    break;
                  case "Is Focus Change Acceptable":
                    axeComparator.compareAxePropIsFocusChangeOk(expected, actual);
                    break;
                  case "Touch Exploration Started":
                    axeComparator.compareAxePropIsTouchExplorationGesture(expected, actual);
                    break;
                  case "Visible Text":
                    axeComparator.compareAxePropVisibleText(expected, actual);
                    break;
                  case "Foreground Color":
                    axeComparator.compareAxePropColorForeGround(expected, actual);
                    break;
                  case "Background Color":
                    axeComparator.compareAxePropColorBackGround(expected, actual);
                    break;
                  case "Color Contrast Ratio":
                    axeComparator.compareAxePropColorContrast(expected, actual);
                    break;
                  case "Confidence in Color Detection":
                    axeComparator.compareAxePropConfidence(expected, actual);
                    break;
                  case "Screen Height":
                    axeComparator.compareAxePropScreenHeight(expected, actual);
                    break;
                  case "Screen Width":
                    axeComparator.compareAxePropScreenWidth(expected, actual);
                    break;
                  case "isRendered":
                    axeComparator.compareAxePropIsRendered(expected, actual);
                    break;
                  case "isOffScreen":
                    axeComparator.compareAxePropIsOffScreen(expected, actual);
                    break;
                  case "isPartiallyVisible":
                    axeComparator.compareAxePropIsPartiallyVisible(expected, actual);
                    break;
                  default:
                    break;

                }
              }
            }
          }
        }

        axeComparator.compareRuleSummary(axeResult.ruleSummary, other.ruleSummary);
        axeComparator.compareImpact(axeResult.impact, other.impact);
        axeComparator.compareStatus(axeResult.status, other.status);

        actualResults.remove(0);
      });

      // Ensure the axeRuleResults array is empty when the run is done.
      if (!actualResults.isEmpty()) {
        fail("Unexpected result present: " + actualResults.toString());
      }

    }
  }

  public static class AxeTestSpec extends AxeResult {

    String testDescription;

    public AxeTestSpec(AxeConf axeConf, AxeContext axeContext, List<AxeRuleResult> axeRuleResults) {
      super(axeConf, axeContext, axeRuleResults);
    }
  }

  static class AxeComparator {

    private final String message;

    AxeComparator(String message) {
      this.message = message;
    }

    void compareAxeViewId(String expectedAxeViewId, String actualAxeViewId) {
      assertEquals("AxeViewId:\n" + message, expectedAxeViewId, actualAxeViewId);
    }

    void compareAxeRuleId(String expectedRuleId, String actualRuleId) {
      assertEquals("AxeRuleId:\n" + message, expectedRuleId, actualRuleId);
    }

    void compareRuleSummary(String expectedRuleSummary, String actualRuleSummary) {
      assertEquals("RuleSummary:\n" + message, expectedRuleSummary, actualRuleSummary);
    }

    void compareImpact(int expectedImpact, int actualImpact) {
      assertEquals("Impact:\n" + message, expectedImpact, actualImpact);
    }

    void compareStatus(String expectedResult, String actualResult) {
      assertEquals("Result:\n" + message, expectedResult, actualResult);
    }

    void compareAxePropClassName(Object expected, Object actual) {
      assertEquals("PropClassName:\n" + message, expected, actual);
    }

    void compareAxePropContentDescription(Object expected, Object actual) {
      assertEquals("ContentDescription:\n" + message, expected, actual);
    }

    void compareAxePropDpi(Object expected, Object actual) {
      assertEquals("DPI:\n" + message, expected.toString(), actual.toString());
    }

    void compareAxePropFrame(Object expected, Object actual) {
      assertEquals("Frame Bottom:\n" + message, ((LinkedTreeMap) expected).get("bottom"),
              (double) ((AxeRect) actual).bottom);
      assertEquals("Frame Left:\n" + message, ((LinkedTreeMap) expected).get("left"),
              (double) ((AxeRect) actual).left);
      assertEquals("Frame Top:\n" + message, ((LinkedTreeMap) expected).get("top"),
              (double) ((AxeRect) actual).top);
      assertEquals("Frame Right:\n" + message, ((LinkedTreeMap) expected).get("right"),
              (double) ((AxeRect) actual).right);
    }

    void compareAxePropHeight(Object expected, Object actual) {
      assertEquals("Height:\n" + message, expected, actual);
    }

    void compareAxePropImportant(Object expected, Object actual) {
      assertEquals("Important:\n" + message, expected, actual);
    }

    void compareAxePropIsClickable(Object expected, Object actual) {
      assertEquals("isClickable:\n" + message, expected, actual);
    }

    void compareAxePropIsEnabled(Object expected, Object actual) {
      assertEquals("isEnabled:\n" + message, expected, actual);
    }

    void compareAxePropLabeledBy(Object expected, Object actual) {
      assertEquals("LabeledBy:\n" + message, expected, actual);
    }

    void compareAxePropSpeakableText(Object expected, Object actual) {
      assertEquals("Speakable Text:\n" + message, expected.toString().trim(),
              actual.toString().replaceAll("\n", " ").trim());
    }

    void compareAxePropWidth(Object expected, Object actual) {
      assertEquals("Width:\n" + message, expected, actual);
    }

    void compareAxePropException(Object expected, Object actual) {
      assertEquals("Exception:\n" + message, expected, actual);
    }

    void compareAxePropStackTrace(Object expected, Object actual) {
      assertEquals("Stack Trace:\n" + message, expected, actual);
    }

    void compareAxePropEventStream(Object expected, Object actual) {
      assertEquals("Event Stream:\n" + message, expected, actual);
    }

    void compareAxePropAccessibilityEvent(Object expected, Object actual) {
      assertEquals("Accessibility Event:\n" + message, expected, actual);
    }

    void compareAxePropIsTouchStarted(Object expected, Object actual) {
      assertEquals("Touch Started:\n" + message, expected, actual);
    }

    void compareAxePropIsFocusChangeOk(Object expected, Object actual) {
      assertEquals("Is Focus Change OK:\n" + message, expected, actual);
    }

    void compareAxePropIsTouchExplorationGesture(Object expected, Object actual) {
      assertEquals("Touch Exploration Gesture:\n" + message, expected, actual);
    }

    void compareAxePropVisibleText(Object expected, Object actual) {
      assertEquals("Visible Text:\n" + message, expected, actual);
    }

    void compareAxePropColorForeGround(Object expected, Object actual) {
      assertEquals("Color Fore Ground:\n" + message, expected.toString(), actual.toString());
    }

    void compareAxePropColorBackGround(Object expected, Object actual) {
      assertEquals("colorBackGround:\n" + message, expected.toString(), actual.toString());
    }

    void compareAxePropColorContrast(Object expected, Object actual) {
      assertEquals("colorContrast:\n" + message, (double)expected, (double)actual, 0.1);
    }

    void compareAxePropConfidence(Object expected, Object actual) {
      assertEquals("Confidence:\n" + message, expected, actual);
    }

    void compareAxePropScreenHeight(Object expected, Object actual) {
      assertEquals("Screen Height:\n" + message, expected, actual);
    }

    void compareAxePropScreenWidth(Object expected, Object actual) {
      assertEquals("Screen Width:\n" + message, expected, actual);
    }

    void compareAxePropIsRendered(Object expected, Object actual) {
      assertEquals("Is Rendered:\n" + message, expected, actual);
    }

    void compareAxePropIsOffScreen(Object expected, Object actual) {
      assertEquals("is Off Screen:\n" + message, expected, actual);
    }

    void compareAxePropIsPartiallyVisible(Object expected, Object actual) {
      assertEquals("Is Partially Visible:\n" + message, expected, actual);
    }
  }
}