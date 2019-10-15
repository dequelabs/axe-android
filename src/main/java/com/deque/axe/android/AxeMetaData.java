package com.deque.axe.android;

import com.deque.axe.android.constants.Constants;
import com.deque.axe.android.utils.JsonSerializable;

@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess"})
public class AxeMetaData implements JsonSerializable {

  public final String axeVersion = getClass().getPackage().getImplementationVersion();

  public final String appIdentifier;

  public final long analysisTimestamp;

  public final String screenTitle;

  /**
   * Build an AxeMetaData object.
   * @param appIdentifier The identifier of the application. (PackageName for Android)
   * @param timestamp The date the analysis was performed.
   */
  public AxeMetaData(final String appIdentifier, final long timestamp) {
    this(appIdentifier, Constants.DEFAULT_SCREEN_TITLE, timestamp);
  }

  /**
   * Build an AxeMetaData object.
   * @param appIdentifier The identifier of the application. (PackageName for Android)
   * @param screenTitle A reasonably unique identifier for the current screen.
   * @param timestamp The date the analysis was performed.
   */
  public AxeMetaData(final String appIdentifier, final String screenTitle, final long timestamp) {
    this.appIdentifier = appIdentifier;
    this.analysisTimestamp = timestamp;
    this.screenTitle = screenTitle;
  }
}
