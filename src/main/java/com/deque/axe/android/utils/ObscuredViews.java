package com.deque.axe.android.utils;

import com.deque.axe.android.AxeView;
import com.deque.axe.android.utils.AxeTree.CallBackResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObscuredViews {

  private final Map<String, AxeView> notObscuredViews = new HashMap<>();

  private final Map<String, AxeView> obscuredViews = new HashMap<>();

  public static void calculateIsObscured(final AxeView axeView) {
    new ObscuredViews(axeView);
  }

  private ObscuredViews(AxeView axeView) {

    axeView.forEachRecursive(instance -> {
      //TODO: We probably want this to be "isVisibleToUser"
      if (!axeView.isAccessibilityFocusable) {
        return CallBackResponse.CONTINUE;
      }

      if (obscuredViews.containsKey(axeView.axeViewId)) {
        return CallBackResponse.CONTINUE;
      }

      if (notObscuredViews.containsKey(axeView.axeViewId)) {
        return CallBackResponse.CONTINUE;
      }

      for(AxeView checkAxeView : allViews()) {
        if (axeView.isObscuredBy(checkAxeView)) {
          addObscuredView(axeView);
          addObscuredView(checkAxeView);
          return CallBackResponse.CONTINUE;
        }
      }

      addNotObscuredView(axeView);

      return CallBackResponse.CONTINUE;
    });
  }

  public boolean isObscured(final AxeView axeView) {
    if (notObscuredViews.containsKey(axeView.axeViewId)) {
      return false;
    } else {
      return obscuredViews.containsKey(axeView.axeViewId);
    }
  }

  private AxeView addObscuredView(final AxeView axeView) {

    axeView.axe_is_obscured = true;

    notObscuredViews.remove(axeView.axeViewId);

    return obscuredViews.put(axeView.axeViewId, axeView);
  }

  private AxeView addNotObscuredView(final AxeView axeView) {

    axeView.axe_is_obscured = false;

    obscuredViews.remove(axeView.axeViewId);

    return notObscuredViews.put(axeView.axeViewId, axeView);
  }

  private List<AxeView> allViews() {

    final List<AxeView> result = new ArrayList<>();

    result.addAll(notObscuredViews.values());
    result.addAll(obscuredViews.values());

    return result;
  }
}
