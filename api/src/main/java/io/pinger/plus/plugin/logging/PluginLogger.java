package io.pinger.plus.plugin.logging;

/**
 * Interface for handling plugin logging operations. Provides methods
 * for logging messages at different levels of severity such as info, warn,
 * and error. Supports formatted messages and throwable logging.
 */
public interface PluginLogger {

    /**
     * Logs an informational message.
     *
     * @param message the message to log
     */
    void info(String message);

    /**
     * Logs a warning message.
     *
     * @param message the message to log
     */
    void warn(String message);

    /**
     * Logs a warning message with an associated {@link Throwable}.
     *
     * @param message the warning message to log
     * @param throwable the throwable to log with the message
     */
    void warn(String message, Throwable throwable);

    /**
     * Logs an error message.
     *
     * @param message the message to log
     */
    void error(String message);

    /**
     * Logs an error message with an associated {@link Throwable}.
     *
     * @param message the error message to log
     * @param throwable the throwable to log with the message
     */
    void error(String message, Throwable throwable);

    /**
     * Logs an informational message with formatting arguments.
     *
     * @param message the message format string
     * @param args the arguments referenced by the format specifiers in the message
     */
    default void info(String message, Object... args) {
        this.info(String.format(message, args));
    }

    /**
     * Logs a warning message with formatting arguments.
     *
     * @param message the message format string
     * @param args the arguments referenced by the format specifiers in the message
     */
    default void warn(String message, Object... args) {
        this.warn(String.format(message, args));
    }

    /**
     * Logs a warning message with a {@link Throwable} and formatting arguments.
     *
     * @param message the message format string
     * @param throwable the throwable to log with the message
     * @param args the arguments referenced by the format specifiers in the message
     */
    default void warn(String message, Throwable throwable, Object... args) {
        this.warn(String.format(message, args), throwable);
    }

    /**
     * Logs an error message with formatting arguments.
     *
     * @param message the message format string
     * @param args the arguments referenced by the format specifiers in the message
     */
    default void error(String message, Object... args) {
        this.error(String.format(message, args));
    }

    /**
     * Logs an error message with a {@link Throwable} and formatting arguments.
     *
     * @param message the message format string
     * @param throwable the throwable to log with the message
     * @param args the arguments referenced by the format specifiers in the message
     */
    default void error(String message, Throwable throwable, Object... args) {
        this.error(String.format(message, args), throwable);
    }
}
