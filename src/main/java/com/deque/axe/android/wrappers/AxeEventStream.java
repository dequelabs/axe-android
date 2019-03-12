package com.deque.axe.android.wrappers;

import com.deque.axe.android.AxeEvent;
import com.deque.axe.android.constants.AxeEventType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AxeEventStream extends LinkedList<AxeEvent> {

  private static final int MAX_SIZE = 100;

  private final @AxeEventType Set<Integer> applicableEvents = new HashSet<>();

  /**
   * Add a new event to the stream.
   * @param axeEvent An event.
   * @return true if the event was added successfully.
   */
  public final boolean addEvent(final AxeEvent axeEvent) {

    if (axeEvent.isViewChangeEvent()) {
      clear();
    }

    if (!applicableEvents.isEmpty()
        && !applicableEvents.contains(axeEvent.eventType)) {
      return false;
    }

    if (this.size() == MAX_SIZE) {
      this.remove(0);
    }

    this.add(axeEvent);

    return true;
  }

  public final AxeEventStream addApplicableEventType(final @AxeEventType Integer axeEventType) {
    applicableEvents.add(axeEventType);
    return this;
  }
}
