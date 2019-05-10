package com.deque.axe.android;

import com.deque.axe.android.utils.JsonSerializable;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class AxeMetaData implements JsonSerializable {

  private final String libVersion = getClass().getPackage().getSpecificationVersion();

  private final String subject;

  private final long timestamp;

  AxeMetaData(final String subject, final long timestamp) {
    this.subject = subject;
    this.timestamp = timestamp;
  }
}
