package io.pinger.plus.annotation;

import io.pinger.plus.platform.Platform;
import io.pinger.plus.runtime.InitializeOrder;

/**
 * Annotation to mark a class for automatic initialization based on the platform.
 * <p>
 * The {@code @RuntimeInitialized} annotation indicates that a class should be
 * automatically initialized when the plugin starts, depending on the server platform.
 * This is useful for managing platform-specific features in a modular way.
 * <p>
 * Classes annotated with this annotation will only be initialized if the platform
 * specified in the {@code platform} attribute matches the current server platform.
 * For example, a class annotated with {@code @RuntimeInitialized(platform = Platform.SPIGOT)}
 * will only be initialized if the plugin is running on Spigot.
 *
 * <h2>Usage</h2>
 * Apply {@code @RuntimeInitialized} to any class that should be automatically initialized
 * at runtime. Specify the target platform using the {@code platform} attribute.
 *
 * <pre>
 * &#064;RuntimeInitialized(platform = Platform.VELOCITY)
 * public class VelocityFeature {
 *     // Velocity-specific implementation
 * }
 * </pre>
 *
 * <h2>Platform Attribute</h2>
 * The {@code platform} attribute defines which platform the class is intended for.
 * The class will only be initialized if the runtime platform matches this value.
 * If not specified, it defaults to {@link Platform#SPIGOT}.
 *
 * @see io.pinger.plus.platform.Platform
 */
public @interface RuntimeInitialized {

    /**
     * Defines the target platform for the annotated class.
     * <p>
     * This specifies which platform the class is meant to be used with. The class will be
     * initialized only if the detected platform at runtime matches this value.
     *
     * @return the target platform for initialization, default is {@link Platform#SPIGOT}.
     */
    Platform platform() default Platform.SPIGOT;

    /**
     * Defines the priority (order) in which this class should be initialized.
     *
     * @see InitializeOrder
     * @return the priority (order) for initializing this class
     */
    InitializeOrder order() default InitializeOrder.NORMAL;
}
