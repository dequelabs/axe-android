package com.deque.axe.android;

import com.deque.axe.android.constants.AxeEventType;
import com.deque.axe.android.constants.Constants;
import com.deque.axe.android.utils.JsonSerializable;

import java.util.HashSet;
import java.util.Set;

public class AxeEvent implements JsonSerializable {

  private static int idCounter = 0;

  private static final Set<Integer> viewChangeEventTypes = new HashSet<>();

  static {
    viewChangeEventTypes.add(AxeEventType.WINDOW_STATE_CHANGED);
  }

  public final String eventTypeName;

  public final String packageName;

  public final int id = idCounter++;

  public final transient AxeView axeView;

  public final transient @AxeEventType Integer eventType;

  private AxeEvent(@AxeEventType Integer type, final String packageName, final AxeView axeView) {
    this.eventType = type;
    eventTypeName = Constants.getEventTypeName(type);
    this.packageName = packageName;
    this.axeView = axeView;
  }

  /**
   * Construct an AxeEvent using a builder.
   * @param builder An AxeEvent builder.
   */
  public AxeEvent(final Builder builder) {
    this(
        builder.eventType(),
        builder.packageName(),
        builder.axeView()
    );
  }

  public boolean isEventType(@AxeEventType Integer eventType) {
    return eventType.equals(this.eventType);
  }

  public boolean isViewChangeEvent() {
    return viewChangeEventTypes.contains(eventType);
  }

  @Override
  public String toString() {
    return toJson();
  }

  public interface Builder {

    @AxeEventType Integer eventType();

    String packageName();

    AxeView axeView();
  }
}
