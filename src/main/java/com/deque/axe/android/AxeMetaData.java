package com.deque.axe.android;

import com.deque.axe.android.utils.JsonSerializable;

@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess"})
public class AxeMetaData implements JsonSerializable {

  public final String axeVersion = getClass().getPackage().getImplementationVersion();

  public final String appIdentifier;

  public final long analysisTimestamp;

  public AxeMetaData(final String appIdentifier, final long timestamp) {
    this.appIdentifier = appIdentifier;
    this.analysisTimestamp = timestamp;
  }
}
