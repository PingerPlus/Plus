package io.pinger.plus.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.pinger.plus.platform.Platform;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a class for automatic binding and initialization of methods with specific characteristics.
 * <p>
 * Classes annotated with {@code @AutoBind} will be automatically registered by the dependency
 * injection framework, and any zero-parameter methods returning {@code Subscribeable} will be
 * invoked on initialization. This annotation is particularly useful for classes designed to manage
 * subscriptions or event listeners that require automatic registration at runtime.
 * </p>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>{@code
 * @AutoBind
 * public class EventSubscriber {
 *
 *     public Subscribable onSomeEvent() {
 *         // Method body
 *     }
 *
 *     public Subscribable onAnotherEvent() {
 *         // Method body
 *     }
 * }
 * }</pre>
 *
 * <p>
 * In this example, all methods within {@code EventSubscriber} that return {@code Subscribeable}
 * and have no parameters will be automatically invoked upon binding.
 * </p>
 */
@Retention(RUNTIME)
@Target(TYPE)
@RuntimeInitialized(platform = Platform.ANY)
public @interface AutoBind {
}
