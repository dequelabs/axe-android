package com.deque.axe.android;

import com.deque.axe.android.constants.AndroidClassNames;
import com.deque.axe.android.utils.AxeTextUtils;

import java.util.HashMap;
import java.util.Map;

class AxePropCalculator {

  private static final String SPACE = " ";
  private static Map<String, String> propMap = new HashMap<>();
  private final String text;
  private final String contentDescription;
  private final String labelText;
  private final String value;
  private final boolean isEnabled;
  private final String className;
  private final String hintText;

  AxePropCalculator(String text,
                    String contentDescription,
                    String labelText,
                    String value,
                    boolean isEnabled,
                    String className,
                    String hintText) {

    this.text = text;
    this.contentDescription = contentDescription;
    this.labelText = labelText;
    this.value = value;
    this.isEnabled = isEnabled;
    this.className = className;
    this.hintText = hintText;

    AxePropInterface axePropInterface = new AxePropInterface() {};
    propMap.put(Props.NAME.getProp(), axePropInterface.getNameProp(
            text,
            contentDescription,
            labelText,
            hintText));
    propMap.put(Props.ROLE.getProp(),
            axePropInterface.getRoleProp(className));
    propMap.put(Props.VALUE.getProp(),
            axePropInterface.getValueProp(value));
    propMap.put(Props.STATE.getProp(), null);
  }

  Map<String, String> getCalculatedProps() {
    switch (className) {
      case AndroidClassNames.SWITCH:
      case AndroidClassNames.CHECKBOX:
        propMap.put(Props.NAME.getProp(), new AxePropInterface() {
          @Override
          public String getNameProp(String text,
                                    String contentDescription,
                                    String labelText,
                                    String hint) {

            return getPropAfterNullCheck(text) + SPACE
                     + getPropAfterNullCheck(contentDescription) + SPACE
                     + getPropAfterNullCheck(labelText);
          }
        }.getNameProp(text, contentDescription, labelText, hintText));

        propMap.put(Props.STATE.getProp(), new AxePropInterface() {
          @Override
          public String getStateProp(String state) {
            return isEnabled ? "enabled" : "disabled";
          }
        }.getStateProp(""));

        break;
      case AndroidClassNames.EDIT_TEXT:
        propMap.put(Props.NAME.getProp(), new AxePropInterface() {
          @Override
          public String getNameProp(String text,
                                    String contentDescription,
                                    String labelText,
                                    String hint) {
            if (!AxeTextUtils.isNullOrEmpty(contentDescription)
                    && !AxeTextUtils.isNullOrEmpty(hint)) {
                return getPropAfterNullCheck(hint) + SPACE
                        + getPropAfterNullCheck(labelText);
            } else {
                return getPropAfterNullCheck(contentDescription) + SPACE
                        + getPropAfterNullCheck(hint) + SPACE
                        + getPropAfterNullCheck(labelText);
                }
            }
          }.getNameProp(text, contentDescription, labelText, hintText));


        propMap.put(Props.VALUE.getProp(), new AxePropInterface() {
          @Override
          public String getValueProp(String value) {
            return text;
          }
        }.getValueProp(text));

        break;
      case AndroidClassNames.TEXT_VIEW:
        propMap.put(Props.VALUE.getProp(), new AxePropInterface() {
          @Override
          public String getValueProp(String value) {
            return text;
          }
        }.getValueProp(text));

        break;
      case AndroidClassNames.IMAGE_VIEW:
        propMap.put(Props.NAME.getProp(), new AxePropInterface() {
          @Override
          public String getNameProp(String text,
                                    String contentDescription,
                                    String labelText,
                                    String hint) {
            return getPropAfterNullCheck(text) + SPACE
                    + getPropAfterNullCheck(contentDescription) + SPACE
                    + getPropAfterNullCheck(labelText);
            }
          }.getNameProp(text, contentDescription, labelText, hintText));

        break;

      default:
        break;
    }
    return propMap;
  }

  enum Props {
    NAME("name"),
    ROLE("role"),
    VALUE("value"),
    STATE("state");

    private String prop;

    Props(String prop) {
      this.prop = prop;
    }

    public String getProp() {
      return prop;
    }
  }

  interface AxePropInterface {
    default String getNameProp(String text,
                               String contentDescription,
                               String labelText,
                               String hint) {
      return getPropAfterNullCheck(text) + SPACE + getPropAfterNullCheck(contentDescription);
    }

    default String getRoleProp(String className) {
      return className;
    }

    default String getValueProp(String value) {
      return value;
    }

    default String getStateProp(String state) {
      return state;
    }
  }

  private static String getPropAfterNullCheck(String propText) {
    return AxeTextUtils.isNullOrEmpty(propText) ? "" : propText;
  }
}
