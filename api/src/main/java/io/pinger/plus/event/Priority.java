package io.pinger.plus.event;

/**
 * Enum representing the priority levels for event listeners.
 *
 * <p>Event listeners are executed in the order of their priority. The execution flow
 * starts with {@code LOWEST} and ends with {@code MONITOR}. The {@code MONITOR} priority
 * is used for observers or final tasks that do not modify the event.</p>
 */
public enum Priority {
    /**
     * Indicates that the event listener has the lowest priority and is executed first.
     * Typically used when the listener does not influence or depends on other listeners.
     */
    LOWEST,

    /**
     * Indicates that the event listener has low priority and is executed after {@code LOWEST}.
     */
    LOW,

    /**
     * Indicates the default priority for event listeners.
     * Listeners with {@code NORMAL} priority are executed after {@code LOW}, but before {@code HIGH}.
     */
    NORMAL,

    /**
     * Indicates that the event listener has high priority and is executed after {@code NORMAL}.
     * Used when the listener must run before most other listeners.
     */
    HIGH,

    /**
     * Indicates the highest priority for event listeners, executed just before {@code MONITOR}.
     * Suitable for listeners that must execute before all other listeners except monitors.
     */
    HIGHEST,

    /**
     * Indicates that the event listener is monitoring and executed last.
     * Listeners with this priority should not modify the event.
     */
    MONITOR
}
