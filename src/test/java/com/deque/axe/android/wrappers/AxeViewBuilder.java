package com.deque.axe.android.wrappers;

import com.deque.axe.android.AxeView;
import com.deque.axe.android.AxeView.Builder;

import java.util.ArrayList;
import java.util.List;

public class AxeViewBuilder implements Builder {

  private List<Builder> children = new ArrayList<>();

  private AxeRect boundsInScreen = new AxeRect(0, 0, 0, 0);

  private String className = Object.class.getSimpleName();

  private String contentDescription = null;

  private boolean isAccessibilityFocusable = false;

  private boolean isClickable = false;

  private boolean isEnabled = true;

  private boolean isImportantForAccessibility = false;

  private AxeView labeledBy = null;

  private String paneTitle = null;

  private String text = null;

  private String hintText = null;

  private String value = null;

  private String viewIdResourceName = "";

  private boolean isVisibleToUser = true;

  private int measuredHeight = 0;

  private int measuredWidth = 0;

  public AxeViewBuilder() { }

  private AxeViewBuilder(AxeViewBuilder deepCopyThis) {
    children = deepCopyThis.children;
    boundsInScreen = deepCopyThis.boundsInScreen;
    className = deepCopyThis.className;
    contentDescription = deepCopyThis.contentDescription;
    isAccessibilityFocusable = deepCopyThis.isAccessibilityFocusable;
    isClickable = deepCopyThis.isClickable;
    isEnabled = deepCopyThis.isEnabled;
    isImportantForAccessibility = deepCopyThis.isImportantForAccessibility;
    labeledBy = deepCopyThis.labeledBy;
    viewIdResourceName = deepCopyThis.viewIdResourceName;
    text = deepCopyThis.text;
    hintText = deepCopyThis.hintText;
    value = deepCopyThis.value;
    paneTitle = deepCopyThis.paneTitle;
    isVisibleToUser = deepCopyThis.isVisibleToUser;
    measuredHeight = deepCopyThis.measuredHeight;
    measuredWidth = deepCopyThis.measuredWidth;
  }

  /**
   * Add a child builder to this hierarchy. This child is deep copied,
   * so the same builder can be used to create another child. In fact,
   * you could continually reference the same builder, but that would
   * be hard to do correctly. Don't do this.
   * @param child The child builder to add.
   */
  public void addChild(AxeViewBuilder child) {

    AxeViewBuilder copy = new AxeViewBuilder(child);

    children.add(copy);
  }

  public AxeViewBuilder boundsInScreen(final AxeRect boundsInScreen) {
    this.boundsInScreen = boundsInScreen;
    return this;
  }

  public AxeRect boundsInScreen() {
    return boundsInScreen;
  }

  public AxeViewBuilder className(final String newValue) {
    className = newValue;
    return this;
  }

  public String className() {
    return className;
  }

  public AxeViewBuilder contentDescription(final String newValue) {
    this.contentDescription = newValue;
    return this;
  }

  public String contentDescription() {
    return contentDescription;
  }

  public AxeViewBuilder isAccessibilityFocusable(final boolean newValue) {
    isAccessibilityFocusable = newValue;
    return this;
  }

  public boolean isAccessibilityFocusable() {
    return isAccessibilityFocusable;
  }

  public AxeViewBuilder isClickable(final boolean isClickable) {
    this.isClickable = isClickable;
    return this;
  }

  @Override
  public boolean isClickable() {
    return isClickable;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  public AxeViewBuilder isEnabled(final boolean newValue) {
    isEnabled = newValue;
    return this;
  }

  @Override
  public boolean isImportantForAccessibility() {
    return isImportantForAccessibility;
  }

  public AxeViewBuilder isImportantForAccessibility(final boolean newValue) {
    isImportantForAccessibility = newValue;
    return this;
  }

  @Override
  public AxeView labeledBy() {
    return labeledBy;
  }

  public AxeViewBuilder labeledBy(AxeView labelAxeView) {
    this.labeledBy = labelAxeView;
    return this;
  }

  @Override
  public String packageName() {
    return "com.placeholder";
  }

  @Override
  public String paneTitle() {
    return paneTitle;
  }

  public AxeViewBuilder painTitle(final String paneTitle) {
    this.paneTitle = paneTitle;
    return this;
  }

  public AxeViewBuilder text(final String newValue) {
    this.text = newValue;
    return this;
  }

  public String text() {
    return text;
  }

  public AxeViewBuilder hintText(final String newValue) {
    this.hintText = newValue;
    return this;
  }

  public String hintText() {
    return hintText;
  }

  public String value() {
    return value;
  }

  @Override
  public String viewIdResourceName() {
    return viewIdResourceName;
  }

  public AxeViewBuilder viewIdResourceName(final String viewIdResourceName) {
    this.viewIdResourceName = viewIdResourceName;
    return this;
  }

  @Override
  public List<AxeView> children() {

    List<AxeView> results = new ArrayList<>();

    for (final AxeView.Builder childBuilder : children) {
      results.add(new AxeView(childBuilder));
    }

    return results;
  }

  @Override
  public boolean overridesAccessibilityDelegate() {
    return false;
  }

  @Override
  public boolean isVisibleToUser() {
    return isVisibleToUser;
  }

  /**
   * Returns AxeViewBuilder with isVisibleToUser.
   * @param isVisibleToUser isVisibleToUser.
   * @return the axeViewBuilder.
   */
  public AxeViewBuilder isVisibleToUser(boolean isVisibleToUser) {
    this.isVisibleToUser = isVisibleToUser;

    return this;
  }

  @Override
  public int measuredHeight() {
    return measuredHeight;
  }

  /**
   * Returns AxeViewBuilder with measuredHeight.
   * @param measuredHeight the measuredHeight.
   * @return the AxeViewBuilder.
   */
  public AxeViewBuilder measuredHeight(int measuredHeight) {
    this.measuredHeight = measuredHeight;

    return this;
  }

  @Override
  public int measuredWidth() {
    return measuredWidth;
  }

  /**
   * Returns AxeViewBuilder with measuredWidth.
   * @param measuredWidth the measuredWidth.
   * @return the AxeViewBuilder.
   */
  public AxeViewBuilder measuredWidth(int measuredWidth) {
    this.measuredWidth = measuredWidth;

    return this;
  }

  public AxeView build() {
    return new AxeView(this);
  }
}
