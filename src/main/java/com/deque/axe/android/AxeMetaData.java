package com.deque.axe.android;

import com.deque.axe.android.utils.JsonSerializable;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class AxeMetaData implements JsonSerializable {

  private final String libVersion = getClass().getPackage().getSpecificationVersion();

  private final String appIdentifier;

  private final long timestamp;

  AxeMetaData(final String appIdentifier, final long timestamp) {
    this.appIdentifier = appIdentifier;
    this.timestamp = timestamp;
  }
}
