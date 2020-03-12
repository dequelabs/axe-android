package com.deque.axe.android;

import com.deque.axe.android.utils.AxeTextUtils;

class AxeCalculatedPropsImpl {

  public static class ControlsCalculatedProps implements AxeCalculatedProps {

    private final String text;
    private final String contentDescription;
    private final String labelText;
    private final String value;
    private final boolean isEnabled;
    private final String className;

    ControlsCalculatedProps(String text,
                            String contentDescription,
                            String labelText,
                            String value,
                            boolean isEnabled,
                            String className) {
      this.text = text;
      this.contentDescription = contentDescription;
      this.labelText = labelText;
      this.value = value;
      this.isEnabled = isEnabled;
      this.className = className;
    }

    @Override
    public String calculatedNameProp() {
      return getPropAfterNullCheck(text)
              + getPropAfterNullCheck(contentDescription)
              + getPropAfterNullCheck(labelText);
    }

    @Override
    public String calculatedRoleProp() {
      return className;
    }

    @Override
    public String calculatedValueProp() {
      return value;
    }

    @Override
    public String calculatedStateProp() {
      return isEnabled ? "enabled" : "disabled";
    }
  }

  static class EditTextCalculatedProps implements AxeCalculatedProps {

    private final String text;
    private final String contentDescription;
    private final String labelText;
    private final String hint;
    private final String className;

    EditTextCalculatedProps(String text,
                            String contentDescription,
                            String labelText,
                            String hint,
                            String className) {
      this.text = text;
      this.contentDescription = contentDescription;
      this.labelText = labelText;
      this.hint = hint;
      this.className = className;
    }

    @Override
    public String calculatedNameProp() {
      if (!AxeTextUtils.isNullOrEmpty(contentDescription)
              && !AxeTextUtils.isNullOrEmpty(hint)) {
        return getPropAfterNullCheck(hint)
                  + getPropAfterNullCheck(labelText);
      } else {
        return getPropAfterNullCheck(contentDescription)
                  + getPropAfterNullCheck(hint)
                  + getPropAfterNullCheck(labelText);
      }
    }

    @Override
    public String calculatedRoleProp() {
      return className;
    }

    @Override
    public String calculatedValueProp() {
      return text;
    }

    @Override
    public String calculatedStateProp() {
      return null;
    }
  }

  static class ImageViewCalculatedProps implements AxeCalculatedProps {

    private final String text;
    private final String contentDescription;
    private final String labelText;
    private final String className;

    ImageViewCalculatedProps(String text,
                             String contentDescription,
                             String labelText,
                             String className) {
      this.text = text;
      this.contentDescription = contentDescription;
      this.labelText = labelText;
      this.className = className;
    }

    @Override
    public String calculatedNameProp() {
      return getPropAfterNullCheck(text)
              + getPropAfterNullCheck(contentDescription)
              + getPropAfterNullCheck(labelText);
    }

    @Override
    public String calculatedRoleProp() {
      return className;
    }

    @Override
    public String calculatedValueProp() {
      return null;
    }

    @Override
    public String calculatedStateProp() {
      return null;
    }
  }

  static class TextViewCalculatedProps implements AxeCalculatedProps {

    private final String text;
    private final String contentDescription;
    private final String className;

    TextViewCalculatedProps(String text, String contentDescription, String className) {
      this.text = text;
      this.contentDescription = contentDescription;
      this.className = className;
    }

    @Override
    public String calculatedNameProp() {
      return getPropAfterNullCheck(text)
              + getPropAfterNullCheck(contentDescription);
    }

    @Override
    public String calculatedRoleProp() {
      return className;
    }

    @Override
    public String calculatedValueProp() {
      return text;
    }

    @Override
    public String calculatedStateProp() {
      return null;
    }
  }

  static class UnknownViewCalculatedProps implements AxeCalculatedProps {

    private final String text;
    private final String contentDescription;
    private final String className;

    UnknownViewCalculatedProps(String text, String contentDescription, String className) {
      this.text = text;
      this.contentDescription = contentDescription;
      this.className = className;
    }

    @Override
    public String calculatedNameProp() {
      return getPropAfterNullCheck(text)
              + getPropAfterNullCheck(contentDescription);
    }

    @Override
    public String calculatedRoleProp() {
      return className;
    }

    @Override
    public String calculatedValueProp() {
      return null;
    }

    @Override
    public String calculatedStateProp() {
      return null;
    }
  }

  private static String getPropAfterNullCheck(String propText) {
    return AxeTextUtils.isNullOrEmpty(propText) ? "" : propText;
  }
}
