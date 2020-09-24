package com.deque.axe.android;

import static com.deque.axe.android.constants.AxeImpact.BLOCKER;
import static com.deque.axe.android.constants.AxeImpact.MINOR;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import com.deque.axe.android.colorcontrast.AxeImage;
import com.deque.axe.android.constants.AxeStandard;
import com.deque.axe.android.constants.AxeStatus;
import com.deque.axe.android.constants.Constants;
import com.deque.axe.android.rules.hierarchy.EditTextName;
import com.deque.axe.android.rules.hierarchy.SwitchName;
import com.deque.axe.android.utils.AxeFile;
import com.deque.axe.android.utils.AxeJankyPng;
import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxeProps;
import com.deque.axe.android.wrappers.AxeProps.Name;
import com.deque.axe.android.wrappers.AxeViewBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
      super(AxeStandard.BEST_PRACTICE, MINOR.getValue(), "A Test Rule");
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
      super(AxeStandard.BEST_PRACTICE, BLOCKER.getValue(), "Blah");
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

    Assert.assertFalse(axeResult.axeRuleResults.isEmpty());
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
  public void testRuleSize() {

    AxeConf axeConf = new AxeConf();

    Axe axe = new Axe(axeConf);
    axe.axeConf.ruleInstances();

    assertEquals(axe.axeConf.ruleIds.size(), 9);
    assertEquals(axe.axeConf.ruleInstances().size(), 9);
  }

  @Test
  public void tesAxeConf() {
    AxeConf axeConf = new AxeConf();

    assertEquals(axeConf.rules.size(), 9);
  }

  @Test
  public void testIgnoreRule() {

    AxeConf axeConf = new AxeConf();

    Axe axe = new Axe(axeConf);
    axe.axeConf.ruleInstances();
    axe.axeConf.ignore(EditTextName.class.getSimpleName(), true);

    Assert.assertEquals(axe.axeConf.ruleIds.size(), 8);
    assertEquals(axe.axeConf.ruleInstances().size(), 9);
  }

  @Test
  public void testIgnoreMultipleRules() {

    AxeConf axeConf = new AxeConf();

    Axe axe = new Axe(axeConf);
    axe.axeConf.ruleInstances();
    List<String> rulesToIgnore = new LinkedList<>();

    rulesToIgnore.add(EditTextName.class.getSimpleName());
    rulesToIgnore.add(SwitchName.class.getSimpleName());

    axe.axeConf.ignore(rulesToIgnore, true);

    Assert.assertEquals(axe.axeConf.ruleIds.size(), 7);
    assertEquals(axe.axeConf.ruleInstances().size(), 9);
  }

  /**
   * This test is to catch new objects being added to the preexisting apis.
   * If this test fails then it means some new parameter has been added into a preexisting api.
   * But it is not updated in the api_test json file.
   *
   * @throws IOException when json is not found.
   */
  @Test
  public void jsonAxeRuleResultTestSpecs() throws IOException {
    JsonParser jsonParser = new JsonParser();
    File file = new AxeFile("api_test_spec/api_test.json").file;
    Path path = file.toPath();

    final byte[] encoded = Files.readAllBytes(path);

    final String json = new String(encoded).trim();

    final AxeTestSpec testSpec = JsonSerializable.fromJson(json, AxeTestSpec.class);

    final Axe axe = new Axe(testSpec.axeConf);

    final AxeResult axeResult = axe.run(testSpec.axeContext);

    final List<AxeRuleResult> actualResults = axeResult.axeRuleResults;

    final List<AxeRuleResult> expectedResults = testSpec.axeRuleResults;

    actualResults.sort((o1, o2) -> o1.compareTo(o2));

    expectedResults.sort((o1, o2) -> o1.compareTo(o2));

    assertEquals(expectedResults.size(), actualResults.size());
    Assert.assertFalse("Calculated Result should not be empty!", actualResults.isEmpty());

    expectedResults.forEach(expectedAxeRuleResult -> {
      AxeRuleResult actualRuleResult = actualResults.get(0);
      assertEquals(expectedAxeRuleResult.axeViewId, actualRuleResult.axeViewId);
      assertEquals(expectedAxeRuleResult.ruleId, actualRuleResult.ruleId);
      assertEquals(expectedAxeRuleResult.ruleSummary, actualRuleResult.ruleSummary);

      String expectedRuleResultString = expectedAxeRuleResult.toJson();
      Object expectedRuleResultObj = jsonParser.parse(expectedRuleResultString);
      JsonObject expectedRuleResultJsonObject = (JsonObject) expectedRuleResultObj;
      Set<String> expectedRuleResultKeySet = expectedRuleResultJsonObject.keySet();

      String actualRuleResultString = actualRuleResult.toJson();
      Object actualRuleResultObj = jsonParser.parse(actualRuleResultString);
      JsonObject actualRuleResultJsonObject = (JsonObject) actualRuleResultObj;
      Set<String> actualRuleResultKeySet = actualRuleResultJsonObject.keySet();

      assertEquals(expectedRuleResultKeySet, actualRuleResultKeySet);

      for (Map.Entry<String, Object> stringObjectEntry : expectedAxeRuleResult.props.entrySet()) {
        Object key = ((Map.Entry) stringObjectEntry).getKey();
        assertTrue(actualRuleResult.props.containsKey(key));
      }

      actualResults.remove(0);
    });

    assertEquals(actualResults.size(), 0);
  }

  @Test
  public void axeViewJsonTest() throws IOException {
    JsonParser jsonParser = new JsonParser();

    Object obj = jsonParser.parse(
            new FileReader("src/test/resources/api_test_spec/api_test.json"));

    JsonObject jsonObject = (JsonObject) obj;

    JsonObject axeViewJsonObject = jsonObject
            .getAsJsonObject("axeContext")
            .getAsJsonObject("axeView");

    Set<String> keySet = axeViewJsonObject.keySet();

    File file = new AxeFile("api_test_spec/api_test.json").file;
    Path path = file.toPath();

    final byte[] encoded = Files.readAllBytes(path);

    final String json = new String(encoded).trim();

    final AxeTestSpec testSpec = JsonSerializable.fromJson(json, AxeTestSpec.class);

    String actualAxeViewJson = testSpec.axeContext.axeView.toJson();

    Object actualAxeObj = jsonParser.parse(actualAxeViewJson);

    JsonObject actualAxeViewJsonObject = (JsonObject) actualAxeObj;

    Set<String> actualAxeViewKeySet = actualAxeViewJsonObject.keySet();

    assertEquals(actualAxeViewKeySet, keySet);

    for (String key: keySet) {
      if (actualAxeViewKeySet.contains(key) && !key.equals("children")) {
        assertEquals(key, actualAxeViewJsonObject.get(key), axeViewJsonObject.get(key));
      }
    }
  }

  @Test
  public void axeMetaDataJsonTest() throws IOException {
    JsonParser jsonParser = new JsonParser();

    Object obj = jsonParser.parse(
            new FileReader("src/test/resources/api_test_spec/api_test.json"));

    JsonObject jsonObject = (JsonObject) obj;

    JsonObject axeMetaDataJsonObject = jsonObject
            .getAsJsonObject("axeContext")
            .getAsJsonObject("axeMetaData");

    Set<String> keySet = axeMetaDataJsonObject.keySet();

    File file = new AxeFile("api_test_spec/api_test.json").file;
    Path path = file.toPath();

    final byte[] encoded = Files.readAllBytes(path);

    final String json = new String(encoded).trim();

    final AxeTestSpec testSpec = JsonSerializable.fromJson(json, AxeTestSpec.class);

    String actualAxeMetaDataJson = testSpec.axeContext.axeMetaData.toJson();

    JsonObject actualAxeMetaDataJsonObject = jsonParser
            .parse(actualAxeMetaDataJson)
            .getAsJsonObject();

    Set<String> actualAxeViewKeySet = actualAxeMetaDataJsonObject.keySet();

    assertEquals(actualAxeViewKeySet, keySet);

    for (String key: keySet) {
      if (actualAxeViewKeySet.contains(key)) {
        assertEquals(key, actualAxeMetaDataJsonObject.get(key), axeMetaDataJsonObject.get(key));
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