package com.deque.axe.android;

import static com.deque.axe.android.constants.AxeImpact.BLOCKER;
import static com.deque.axe.android.constants.AxeImpact.CRITICAL;
import static com.deque.axe.android.constants.AxeImpact.MINOR;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import com.deque.axe.android.wrappers.AxeRect;
import com.deque.axe.android.wrappers.AxeViewBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
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

  @Test
  public void testAddRule() {

    AxeConf axeConf = new AxeConf();
    axeConf.addRule(SampleRuleClass.class);

    Axe axe = new Axe(axeConf);
    axe.axeConf.ruleInstances();

    assertEquals(axe.axeConf.rules.size(), 10);
    assertEquals(axe.axeConf.ruleIds.size(), 10);
    assertEquals(axe.axeConf.ruleInstances().size(), 10);
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

  @Test
  public void backwardCompatibilityTest() throws IOException {

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

        AxeComparatorInterface comparatorInterface;

        if (axeResult.axeViewId != null && other.axeViewId != null) {
          if (!axeResult.axeViewId.equals(other.axeViewId)) {
            System.out.println(axeResult + "\n" + other);
          }
          assertEquals(axeResult.axeViewId, other.axeViewId);
        }

        assertEquals(axeResult.ruleId, other.ruleId);
        List<Object> missingKeys = new ArrayList<>();
        List<String> unknownMissingKeys = new ArrayList<>();

        if (axeResult.props != other.props) {
          for (Map.Entry<String, Object> stringObjectEntry : axeResult.props.entrySet()) {
            Object key = ((Map.Entry) stringObjectEntry).getKey();
            if (other.props.containsKey(key)) {
              if (stringObjectEntry.getValue() != null && other.props.get(key) != null) {
                Object expected = stringObjectEntry.getValue();
                Object actual = other.props.get(key);
                comparatorInterface = comparators.getOrDefault(
                        key,
                        new AxeComparatorInterface() {});
                comparatorInterface.compare(key.toString(), expected, actual);
              }
            } else {
              missingKeys.add(key);
            }
          }
          for (Object missingKey: missingKeys) {
            if (!ignorePropsList.contains(missingKey.toString())) {
              unknownMissingKeys.add(missingKey.toString());
            }
          }
        }

        assertEquals("It looks like a prop has been removed or renamed \n",
                unknownMissingKeys.size(), 0);
        assertEquals(axeResult.ruleSummary, other.ruleSummary);
        assertEquals(axeResult.impact, other.impact);

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

  interface AxeComparatorInterface {
    default void compare(final String key, final Object expected, final Object actual) {
      assertEquals(key + "\n", expected, actual);
    }

    default String message(final String key, final Object expected, final Object actual) {
      return expected.toString() + "\n" + actual.toString();
    }
  }

  private static final Map<String, AxeComparatorInterface> comparators = new HashMap<>();
  private static final List<String> ignorePropsList = new ArrayList<>();

  static {
    //Add all props that we know are present in old result but not anymore in new result
    ignorePropsList.add("height");
    ignorePropsList.add("width");

    comparators.put("boundsInScreen", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {

        assertEquals(
                "Frame Bottom:\n" + message(key, expected, actual),
                ((LinkedTreeMap) expected).get("bottom"),
                (double) ((AxeRect) actual).bottom
        );
        assertEquals(
                "Frame Left:\n" + message(key, expected, actual),
                ((LinkedTreeMap) expected).get("left"),
                (double) ((AxeRect) actual).left
        );
        assertEquals(
                "Frame Top:\n" + message(key, expected, actual),
                ((LinkedTreeMap) expected).get("top"),
                (double) ((AxeRect) actual).top
        );
        assertEquals(
                "Frame Right:\n" + message(key, expected, actual),
                ((LinkedTreeMap) expected).get("right"),
                (double) ((AxeRect) actual).right
        );
      }
    });

    comparators.put("Speakable Text", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {

        assertEquals(
                "Speakable Text:\n" + message(key, expected, actual),
                expected.toString().trim(),
                actual.toString().replaceAll("\n", " ").trim());
      }
    });

    comparators.put("Color Contrast Ratio", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {

        assertEquals(
                "colorContrast:\n" + message(key, expected, actual),
                (double)expected,
                (double)actual, 0.1);
      }
    });

    comparators.put("Foreground Color", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {

        assertEquals(
                "Color Fore Ground:\n" + message(key, expected, actual),
                expected.toString(),
                actual.toString());
      }
    });

    comparators.put("Background Color", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {

        assertEquals(
                "colorBackGround:\n" + message(key, expected, actual),
                expected.toString(),
                actual.toString());
      }
    });

    comparators.put("Screen Dots Per Inch", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {

        assertEquals(
                "DPI:\n" + message(key, expected, actual),
                expected.toString(),
                actual.toString());
      }
    });

    comparators.put("Screen Width", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {
        Double expectedWidth = (Double) expected;
        assertEquals(
                "screen width:\n" + message(key, expected, actual),
                expectedWidth.intValue(),
                (int) actual);
      }
    });

    comparators.put("Screen Height", new AxeComparatorInterface() {
      @Override
      public void compare(String key, Object expected, Object actual) {
        Double expectedWidth = (Double) expected;
        assertEquals(
                "screen height:\n" + message(key, expected, actual),
                expectedWidth.intValue(),
                (int) actual);
      }
    });
  }

  static class SampleRuleClass extends AxeRuleViewHierarchy {

    protected SampleRuleClass() {
      super(AxeStandard.WCAG_20, CRITICAL.getValue(),
              "Sample Rule Violation Description");
    }

    @Override
    public void collectProps(AxeView axeView, AxeProps axeProps) {

    }

    @Override
    public boolean isApplicable(AxeProps axeProps) {
      return false;
    }

    @Override
    public String runRule(AxeProps axeProps) {
      return null;
    }
  }
}