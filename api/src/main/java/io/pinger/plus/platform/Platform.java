package io.pinger.plus.platform;

/**
 * Enum representing different server platforms that the plugin can run on.
 * <p>
 * Each enum constant is associated with a specific class that is unique to that platform.
 * The {@link #detectPlatform()} method allows the detection of the current platform
 * based on the presence of these platform-specific classes.
 * </p>
 */

public enum Platform {

    SPIGOT("org.bukkit.Bukkit"),
    BUNGEE("net.md_5.bungee.api.ProxyServer"),
    VELOCITY("com.velocitypowered.api.proxy.ProxyServer"),
    UNKNOWN(null);

    private final String clazz;

    Platform(String clazz) {
        this.clazz = clazz;
    }

    public static Platform detectPlatform() {
        for (final Platform platform : values()) {
            if (platform.isAvailable()) {
                return platform;
            }
        }
        throw new IllegalStateException("Failed to find a platform matching this server");
    }

    private boolean isAvailable() {
        if (this.clazz == null) {
            return false;
        }
        try {
            this.instantiate();
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Class<?> instantiate() throws ClassNotFoundException {
        return Class.forName(this.clazz);
    }
}
