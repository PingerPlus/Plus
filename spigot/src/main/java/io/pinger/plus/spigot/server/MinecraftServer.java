package io.pinger.plus.spigot.server;

import io.pinger.plus.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;

public final class MinecraftServer implements Comparable<MinecraftServer> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.?\\d{0,3}", Pattern.MULTILINE);

    public static final MinecraftServer CURRENT = MinecraftServer.fromRaw();

    private final String version;
    private final int[] splitter;

    public MinecraftServer(String version) {
        this.version = version;
        this.splitter = MinecraftServer.parseVersions(version);
    }

    private static int[] parseVersions(String version) {
        return Stream.of(version.split("\\."))
            .filter(o -> !o.isEmpty())
            .mapToInt(Integer::parseInt)
            .toArray();
    }

    /**
     * This method creates a new instance of this class
     * by using the rawVersion from the BukkitAPI.
     *
     * @param rawVersion the rawVersion
     * @return the new instance
     */

    public static MinecraftServer fromRaw(String rawVersion) {
        final Matcher matcher = VERSION_PATTERN.matcher(rawVersion);
        if (matcher.find()) {
            return new MinecraftServer(matcher.group(0));
        }

        throw new IllegalArgumentException("Failed to parse Spigot version: " + rawVersion);
    }

    /**
     * This method creates a new instance of this class
     * by using the {@link Bukkit#getVersion()} version.
     *
     * @return the new instance
     */

    public static MinecraftServer fromRaw() {
        return fromRaw(Bukkit.getBukkitVersion());
    }

    /**
     * This method checks if the current server version
     * is equal to the specified version.
     * <p>
     * Note that the specified string should be split,
     * not given by the raw value. For example: 1.7.10, 1.8.8
     *
     * @param otherVersion the version to compare to
     * @return if the server version is equal to the specified one
     */

    public static boolean isVersion(String otherVersion) {
        return CURRENT.compareTo(new MinecraftServer(otherVersion)) == 0;
    }

    /**
     * This method checks if the current server version
     * is greater or equal to the specified version by
     * comparing the two {@link MinecraftServer} references.
     *
     * @param otherServer the specified server version
     * @return the compared value
     */

    public static boolean atLeast(MinecraftServer otherServer) {
        return CURRENT.compareTo(otherServer) >= 0;
    }

    /**
     * This method checks if the current server version
     * is greater or equal to the specified version.
     *
     * @param otherVersion the specified version
     * @return the compared value
     */

    public static boolean atLeast(String otherVersion) {
        return CURRENT.compareTo(new MinecraftServer(otherVersion)) >= 0;
    }

    /**
     * This method returns the version of this
     * minecraft server.
     *
     * @return the version
     */

    public String getVersion() {
        return this.version;
    }

    @Override
    public int compareTo(@Nonnull MinecraftServer o) {
        return Arrays.compare(this.splitter, o.splitter);
    }
}
