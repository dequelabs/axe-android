package com.deque.axe.android;

import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.constants.Constants;
import com.deque.axe.android.utils.AxeTextUtils;
import com.deque.axe.android.utils.AxeTree;
import com.deque.axe.android.utils.JsonSerializable;
import com.deque.axe.android.wrappers.AxeRect;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class AxeView implements AxeTree<AxeView>, Comparable<AxeView>, JsonSerializable {

  /**
   * We need to ignore some of the fields below when we compare JSON Strings, otherwise two
   * identical objects won't register as identical.
   */
  private static final List<String> GSON_IGNORED_FIELDS = Arrays.asList("children", "axeViewId");

  /**
   * A direct copy of the associated Android property encapsulated in an Axe wrapper.
   */
  public final AxeRect boundsInScreen;

  /**
   * Direct copy of the associated Java Property.
   */
  public final String className;

  /**
   * Direct copy of the associated Android Property.
   */
  public final String contentDescription;

  /**
   * Whether or not the view would be focused by Assistive Technologies.
   */
  public final boolean isAccessibilityFocusable;

  /**
   * Whether or not the view responds to Click actions.
   */
  public final boolean isClickable;

  /**
   * True if view interaction is enabled.
   */
  public final boolean isEnabled;

  /**
   * Direct copy of the associated Android Property.
   */
  public final boolean isImportantForAccessibility;

  /**
   * The AxeView of the Label that is acting as the Name for this View.
   */
  public final AxeView labeledBy;

  /**
   * The packageName that the View belongs to.
   * FIXME: Make non transient before a 1.0 release.
   */
  public final transient String packageName;

  /**
   * Direct copy of the associated Android property.
   */
  public final transient String paneTitle;

  /**
   * Direct copy of the associated Android Property.
   */
  public final String text;

  /**
   * A unique Identifier for a given view... conflicts possible but unlikely.
   */
  public final String axeViewId;

  /**
   * Direct copy of the associated Android Property.
   */
  public final String viewIdResourceName;

  /**
   * Direct copy of hint text for views where text can be entered.
   */
  public final String hintText;

  /**
   * Direct copy of value.
   */
  public final String value;

  /**
   * True if the view overrides AccessibilityDelegate.
   */
  public final boolean overridesAccessibilityDelegate;

  /**
   * True if the view is visible to the user.
   */
  public final boolean isVisibleToUser;

  /**
   * Direct copy of the associated Android Property.
   */
  public final int measuredHeight;

  /**
   * Direct copy of the associated Android Property.
   */
  public final int measuredWidth;

  /**
   * Maintains a copy of Content View Axe Rect.
   */
  static AxeRect contentViewAxeRect;

  /**
   * Library of calculated props name, role, state, value.
   */
  public final Map<String, Object> calculatedProps;

  /**
   * The Children of this view as AxeView objects.
   */
  public List<AxeView> children;

  public interface Builder {

    AxeRect boundsInScreen();

    String className();

    String contentDescription();

    boolean isAccessibilityFocusable();

    boolean isClickable();

    boolean isEnabled();

    boolean isImportantForAccessibility();

    AxeView labeledBy();

    String packageName();

    String paneTitle();

    String text();

    String viewIdResourceName();

    String hintText();

    String value();

    List<AxeView> children();

    boolean overridesAccessibilityDelegate();

    boolean isVisibleToUser();

    int measuredHeight();

    int measuredWidth();

    default AxeView build() {
      return new AxeView(this);
    }
  }

  private AxeView(
      final AxeRect boundsInScreen,
      final String className,
      final String contentDescription,
      final boolean isAccessibilityFocusable,
      final boolean isClickable,
      final boolean isEnabled,
      final boolean isImportantForAccessibility,
      final AxeView labeledBy,
      final String packageName,
      final String paneTitle,
      final String text,
      final String viewIdResourceName,
      final String hintText,
      final String value,
      final List<AxeView> children,
      final boolean overridesAccessibilityDelegate,
      final boolean isVisibleToUser,
      final int measuredHeight,
      final int measuredWidth
  ) {
    this.boundsInScreen = boundsInScreen;
    this.className = className;
    this.contentDescription = contentDescription;
    this.isAccessibilityFocusable = isAccessibilityFocusable;
    this.isClickable = isClickable;
    this.isEnabled = isEnabled;
    this.isImportantForAccessibility = isImportantForAccessibility;
    this.labeledBy = labeledBy;
    this.packageName = packageName;
    this.paneTitle = paneTitle;
    this.text = text;
    this.viewIdResourceName = viewIdResourceName;
    this.hintText = hintText;
    this.value = value;
    this.children = children;
    this.overridesAccessibilityDelegate = overridesAccessibilityDelegate;
    this.isVisibleToUser = isVisibleToUser;
    this.measuredHeight = measuredHeight;
    this.measuredWidth = measuredWidth;

    setContentView(viewIdResourceName, boundsInScreen);
    this.calculatedProps = calculateProps();

    // This should be the last thing we do in case we decide parent/children relationships
    // contribute to ID calculation.
    this.axeViewId = Integer.toString(this.hashCode());
  }

  /**
   * Construct an AxeView.
   * @param builder An object that implements the AxeView.Builder interface.
   */
  public AxeView(
      Builder builder
  ) {
    this(
        builder.boundsInScreen(),
        builder.className(),
        builder.contentDescription(),
        builder.isAccessibilityFocusable(),
        builder.isClickable(),
        builder.isEnabled(),
        builder.isImportantForAccessibility(),
        builder.labeledBy(),
        builder.packageName(),
        builder.paneTitle(),
        builder.text(),
        builder.viewIdResourceName(),
        builder.hintText(),
        builder.value(),
        builder.children(),
        builder.overridesAccessibilityDelegate(),
        builder.isVisibleToUser(),
        builder.measuredHeight(),
        builder.measuredWidth()
    );
  }

  /**
   * Recurse through the view hierarchy and grab the package name of the first
   * non Android System UI view.
   * @return A non Android System UI packageName.
   */
  @SuppressWarnings("WeakerAccess")
  public String appIdentifier() {

    final StringBuilder result = new StringBuilder();

    forEachRecursive(instance -> {

      result.setLength(0);

      result.append(instance.packageName);

      if (instance.className.endsWith("ContentFrameLayout")) {
        return CallBackResponse.STOP;
      } else {
        return CallBackResponse.CONTINUE;
      }
    });

    return result.toString();
  }

  @Override
  @NotNull
  public String toString() {
    return toJson();
  }

  /**
   * Gets speakable text of the control. Digs down into child views to see what their speakable
   * text is as well.
   * @return The speakable text of the control and its children.
   */
  public String speakableTextRecursive() {

    final StringBuilder result = new StringBuilder();

    final AtomicBoolean allAreNull = new AtomicBoolean(true);

    forEachRecursive(instance -> {

      if (isChildSpeakableTextIgnoredByTalkback()) {
        result.append(instance.speakableText());
        allAreNull.set(false);

        return CallBackResponse.SKIP_BRANCH;
      }

      final String speakableText = instance.speakableText();

      if (!AxeTextUtils.isNullOrEmpty(speakableText)) {
        result.append(instance.speakableText()).append(" ");
      }

      if (speakableText != null) {
        allAreNull.set(false);
      }

      return CallBackResponse.CONTINUE;
    });

    return allAreNull.get() ? null : result.toString();
  }

  @SuppressWarnings("WeakerAccess")
  public String speakableText() {
    return text == null ? contentDescription : text;
  }

  public String speakableTextOfLabeledBy() {
    return labeledBy == null ? null : labeledBy.speakableText();
  }

  /**
   * Checks if the text has Operating System Text or is Null.
   * @param text to check.
   * @return true if text is Operating System Text or is Null.
   */
  public boolean hasOperatingSystemTextOnlyOrIsNull(String text) {
    if (text == null) {
      return true;
    } else {
      return text.equalsIgnoreCase("on") || text.equalsIgnoreCase("off");
    }
  }

  @Override
  public boolean equals(Object object) {

    if (object == this) {
      return true;
    }

    if (!(object instanceof AxeView)) {
      return false;
    }

    AxeView view = (AxeView) object;

    return view.compareTo(this) == 0;
  }

  @Override
  public List<AxeView> getTreeChildren() {
    return children;
  }

  @Override
  public AxeView getTreeNode() {
    return this;
  }

  @Override
  public String getNodeId() {
    return axeViewId;
  }

  @Override
  public int compareTo(@NotNull AxeView o) {
    return JsonSerializable.compareTo(this, o);
  }

  @Override
  public int hashCode() {
    return JsonSerializable.hashCode(this);
  }

  @Override
  public Gson getGsonComparison() {
    return new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .addSerializationExclusionStrategy(new ExclusionStrategy() {

          @Override
          public boolean shouldSkipField(FieldAttributes f) {
            return GSON_IGNORED_FIELDS.contains(f.getName());
          }

          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        }).create();
  }

  public static boolean classNameIsOfType(
      @NotNull final String fullClassName,
      @NotNull final @AndroidClassNames String viewClass
  ) {
    return viewClass.equalsIgnoreCase(fullClassName);
  }

  public interface Matcher {
    boolean matches(final AxeView view);
  }

  /**
   * Find all AxeView objects in the hierarchy that match.
   * @param matcher A matcher function.
   * @return The list of views that match.
   */
  @SuppressWarnings("WeakerAccess")
  public List<AxeView> query(final Matcher matcher) {

    final ArrayList<AxeView> results = new ArrayList<>();

    forEachRecursive(instance -> {

      try {

        if (matcher.matches(instance)) {
          results.add(instance);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      return CallBackResponse.CONTINUE;
    });

    return results;
  }

  private Map<String, Object> calculateProps() {

    String labelText = labeledBy == null ? "" : labeledBy.text;
    int visibleAxeViewHeight = boundsInScreen.bottom - boundsInScreen.top;
    int visibleAxeViewWidth = boundsInScreen.right - boundsInScreen.left;

    AxePropCalculator axePropCalculator = new AxePropCalculator(
            text,
            contentDescription,
            labelText,
            value,
            isEnabled,
            className,
            hintText,
            isVisibleToUser,
            measuredHeight,
            measuredWidth,
            visibleAxeViewHeight,
            visibleAxeViewWidth
    );

    return axePropCalculator.getCalculatedProps();
  }

  private static void setContentView(String viewIdResourceName, AxeRect boundsInScreen) {
    if (viewIdResourceName != null && viewIdResourceName.equals("android:id/content")) {
      AxeView.contentViewAxeRect = boundsInScreen;
    }
  }

  private boolean isContentView() {
    return viewIdResourceName != null
            && (viewIdResourceName.endsWith("android:id/content") && !children.isEmpty());
  }

  private AxeView getContentView() {

    List<AxeView> axeViewList = query(view ->
            view.viewIdResourceName != null
              && view.viewIdResourceName.endsWith("android:id/content")
    );

    if (axeViewList.size() == 0) {
      return null;
    } else {
      return axeViewList.get(0);
    }
  }

  private boolean isChildSpeakableTextIgnoredByTalkback() {
    return (!AxeTextUtils.isNullOrEmpty(contentDescription)) && isAccessibilityFocusable;
  }

  /**
   * Calculate the screen title for a hierarchy.
   * @return The screen title.
   */
  public String getScreenTitle() {
    
    final AxeView contentView = getContentView();

    if (contentView != null && contentView.isContentView()) {

      final StringBuilder result = new StringBuilder();

      for (AxeView axeView : contentView.children) {
        if (result.length() == 0
                && axeView.viewIdResourceName != null
                && !"null".equals(axeView.viewIdResourceName)) {
          result.append(axeView.viewIdResourceName);
        }
      }

      return result.toString();
    }

    return Constants.DEFAULT_SCREEN_TITLE;
  }

  /**
   * Returns true if the view is Rendered on screen.
   * @param dpi device dots per inch.
   * @param height device height.
   * @param width device width.
   */
  public boolean isRendered(float dpi, long height, long width) {
    return dpi <= 0 || height < 0 || width < 0;
  }

  /**
   * Returns true if the view is created in hierarchy but not visible on the screen.
   * @param frame rectangle containing the view.
   * @param screenHeight device height.
   * @param screenWidth device width.
   */
  public boolean isOffScreen(AxeRect frame, int screenHeight, int screenWidth) {
    if (screenHeight > 0 && screenWidth > 0) {
      return frame.top < 0
              || frame.left < 0
              || frame.bottom > screenHeight
              || frame.right > screenWidth;
    }
    return false;
  }

  /**
   * Returns true if only part of the view is visible on screen.
   * @param frame rectangle containing the view.
   * @param screenHeight device height.
   * @param screenWidth device width.
   */
  public boolean isPartiallyVisible(AxeRect frame, int screenHeight, int screenWidth) {
    if (screenHeight > 0 && screenWidth > 0 && contentViewAxeRect != null) {
      return frame.top <= contentViewAxeRect.top
              || frame.left <= 0
              || frame.bottom >= contentViewAxeRect.bottom
              || frame.right >= screenWidth;
    }
    return false;
  }
}
