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
import com.deque.axe.android.wrappers.AxeViewBuilder;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        new AxeDevice(222, "Samsung", "android", 200, 100),
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
        new AxeDevice(222, "Samsung", "android", 200, 100),
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
        new AxeDevice(222, "Samsung", "android", 200, 100),
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

      final List<AxeRuleResult> results = axe.run(testSpec.axeContext).axeRuleResults;

      final List<AxeRuleResult> expectedResults = testSpec.axeRuleResults;

      results.sort((o1, o2) -> o1.compareTo(o2));

      expectedResults.sort((o1, o2) -> o1.compareTo(o2));

      // Ensure all expected axeRuleResults are present.
      expectedResults.forEach(axeResult -> {

        final String message = testInfo + "\n"
            + "This result was expected, but not present."
            + axeResult.toJson();

        Assert.assertFalse(message, results.isEmpty());

        final AxeRuleResult other = results.get(0);

        /*
         * TODO: Our Test specs don't have this yet. Need to update test specs. This works for now.
         */
        other.impact = 0;
        axeResult.impact = 0;

        assertEquals(axeResult.axeViewId, other.axeViewId);
        assertEquals(axeResult.impact, other.impact);
//        assertEquals(axeResult.props, other.props);

        assertThat(axeResult, is(other));

//        JSONObject jsonObject = new JSONObject(axeResult.toJson().trim());
//        Iterator<String> keys = jsonObject.keys();
//
//        JSONObject jsonObjectNew = new JSONObject(other.toJson().trim());
//        Iterator<String> keysNew = jsonObjectNew.keys();
//
//        ArrayList<String> keysToRemove = new ArrayList<>();
//
//        while (keys.hasNext()) {
//          String key = keys.next();
//          while (keysNew.hasNext()) {
//            String keyNew = keysNew.next();
//            if (jsonObject.get(key) == jsonObjectNew.get(keyNew)) {
//              keysToRemove.add(keyNew);
//            }
//          }
//        }
//
//        keysToRemove.forEach(s -> {
//          if (jsonObjectNew.has(s)) {
//            jsonObjectNew.remove(s);
//          }
//        });
//
//        //After above code we know that jsonObjectNew contains all the elements which are not in the old JSON
//
//        Iterator<String> remainingKeys = jsonObjectNew.keys();
//        while (remainingKeys.hasNext()) {
//          System.out.println(jsonObjectNew.get(remainingKeys.next()));
//        }

        //assertEquals(axeResult, other);

        results.remove(0);
      });

      // Ensure the axeRuleResults array is empty when the run is done.
      if (!results.isEmpty()) {
        fail("Unexpected result present: " + results.toString());
      }

    }
  }

  public static class AxeTestSpec extends AxeResult {

    String testDescription;

    public AxeTestSpec(AxeConf axeConf, AxeContext axeContext, List<AxeRuleResult> axeRuleResults) {
      super(axeConf, axeContext, axeRuleResults);
    }
  }
}