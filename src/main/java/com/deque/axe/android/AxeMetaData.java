package com.deque.axe.android;

import com.deque.axe.android.utils.JsonSerializable;

@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess"})
class AxeMetaData implements JsonSerializable {

  public final String libVersion = getClass().getPackage().getSpecificationVersion();

  public final String appIdentifier;

  public final long timestamp;

  AxeMetaData(final String appIdentifier, final long timestamp) {
    this.appIdentifier = appIdentifier;
    this.timestamp = timestamp;
  }
}
