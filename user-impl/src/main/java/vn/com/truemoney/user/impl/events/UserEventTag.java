package vn.com.truemoney.user.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

/**
 * In order to consume events from a read-side, the events need to be tagged.
 * The simplest way to tag events is to give all events for a particular entity the same tag.
 *
 * {@link https://www.lagomframework.com/documentation/1.6.x/java/ReadSide.html#Event-tags}
 */
public class UserEventTag {

  public static final AggregateEventTag<UserEvent> USER_EVENT_TAG = AggregateEventTag.of(UserEvent.class);

}
