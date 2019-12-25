package com.deque.axe.android.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AxeTree<T extends AxeTree<T>> {

  Iterable<T> getTreeChildren();

  T getTreeNode();

  String getNodeId();

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({0, 1, 2})
  @interface CallBackResponse {
    int CONTINUE = 0;
    int STOP = 1;
    int SKIP_BRANCH = 2;
  }

  interface Callback<T> {
    @CallBackResponse
    int run(final T instance);
  }

  /**
   * Run the callback on each view in the hierarchy, unless asked to stop.
   * @param callback The function to run.
   * @return Whether or not to keep going deeper into the hierarchy.
   */
  @CallBackResponse
  default int forEachRecursive(final Callback<T> callback) {

    int callbackResponse = callback.run(this.getTreeNode());

    if (callbackResponse == CallBackResponse.STOP) {
      return CallBackResponse.STOP;
    }

    if (callbackResponse == CallBackResponse.SKIP_BRANCH) {
      return CallBackResponse.CONTINUE;
    }

    Iterable<T> children = getTreeChildren();

    if (children != null) {
      for (T child : getTreeChildren()) {
        if (child.forEachRecursive(callback) == CallBackResponse.STOP) {
          return CallBackResponse.STOP;
        }
      }
    }

    return CallBackResponse.CONTINUE;
  }
}
