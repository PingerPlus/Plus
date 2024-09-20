package io.pinger.plus.runtime;

/**
 * Enum to define the order of initialization for classes annotated with {@code @RuntimeInitialized}.
 * <p>
 * The {@code InitializeOrder} enum provides five levels of initialization order, which control
 * when annotated classes are initialized relative to other classes. This is useful for managing
 * dependencies between classes and ensuring that certain classes are initialized earlier or later
 * as required by the application.
 * <p>
 * Classes marked with {@code @RuntimeInitialized} can specify their initialization order by
 * setting the {@code order} attribute. The initialization framework will process classes
 * based on this order, allowing fine-grained control over the initialization sequence.
 *
 * <h2>Initialization Levels</h2>
 * <ul>
 *   <li>{@link #VERY_FIRST} – The highest priority level, ensuring the class is initialized first.</li>
 *   <li>{@link #EARLY} – Ensures the class is initialized early, but after those marked as {@code VERY_FIRST}.</li>
 *   <li>{@link #NORMAL} – Standard priority, used when no special initialization order is required.</li>
 *   <li>{@link #LATE} – Ensures the class is initialized later, after most other classes.</li>
 *   <li>{@link #VERY_LAST} – The lowest priority level, ensuring the class is initialized last.</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>
 * &#064;RuntimeInitialized(initializeOrder = InitializeOrder.VERY_FIRST)
 * public class CoreSystem {
 *     // This class will be initialized before all others.
 * }
 * </pre>
 */
public enum InitializeOrder {

    /**
     * Indicates that the class should be initialized as early as possible.
     * <p>
     * Classes with this priority will be initialized before any other classes.
     * This is useful for core systems or infrastructure components that other
     * parts of the application depend on.
     * <p>
     * Example: Initialization of critical services such as logging, configuration
     * systems, or dependency injection frameworks.
     */
    VERY_FIRST,

    /**
     * Indicates that the class should be initialized early in the initialization process.
     * <p>
     * Classes with this priority are initialized after {@link #VERY_FIRST} but still
     * before the majority of other classes. Use this when a class needs to be available
     * early but isn't as critical as those initialized with {@code VERY_FIRST}.
     */
    EARLY,

    /**
     * Indicates that the class should be initialized with standard priority.
     * <p>
     * This is the default initialization order, used when no specific order is
     * required. Classes initialized with {@code DEFAULT} will be processed after
     * those marked with {@code VERY_FIRST} and {@code EARLY}, but before {@code LATE}
     * or {@code VERY_LAST}.
     */
    NORMAL,

    /**
     * Indicates that the class should be initialized later in the process.
     * <p>
     * Classes marked with {@code LATE} are initialized after most other classes,
     * allowing them to depend on components initialized earlier. This is useful for
     * systems that need to observe or interact with already-initialized components.
     */
    LATE,

    /**
     * Indicates that the class should be initialized as late as possible.
     * <p>
     * Classes with this priority will be the last to be initialized. This is useful
     * for systems that depend on almost all other components being available before
     * they can be initialized.
     * <p>
     * Example: Shutdown hooks, monitoring systems, or systems that require a complete
     * and fully initialized environment.
     */
    VERY_LAST;
}
