package io.pinger.plus.message;

import io.pinger.plus.text.Text;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentSerializer {
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
        .character(LegacyComponentSerializer.SECTION_CHAR)
        .build();

    /**
     * Converts an array of strings into a single {@link Component}, joining the lines with newlines.
     *
     * @param lines The array of strings to convert.
     * @return A {@link Component} created from the provided lines.
     */
    public static Component linesToComponent(@Nonnull String... lines) {
        return ComponentSerializer.linesToComponent(Arrays.asList(lines));
    }

    /**
     * Converts a list of strings into a single {@link Component}, joining the lines with newlines.
     *
     * @param lines The list of strings to convert.
     * @return A {@link Component} created from the provided lines.
     */
    public static Component linesToComponent(List<String> lines) {
        return lines.stream()
            .map(ComponentSerializer::componentFromContent)
            .collect(Collectors.collectingAndThen(Collectors.toList(), ComponentSerializer::mergeComponents));
    }

    /**
     * Merges an array of {@link Component} objects into a single {@link Component}, joining them with newlines.
     *
     * @param components The array of components to merge.
     * @return A single {@link Component} created by merging the provided components.
     */
    public static Component mergeComponents(@Nonnull Component... components) {
        return ComponentSerializer.mergeComponents(Arrays.asList(components));
    }

    /**
     * Merges a list of {@link Component} objects into a single {@link Component}, joining them with newlines.
     * The joining is done using {@link JoinConfiguration#newlines()}.
     *
     * @param components The list of components to merge.
     * @return A single {@link Component} created by merging the provided components.
     */
    public static Component mergeComponents(List<Component> components) {
        return Component.join(JoinConfiguration.newlines(), components);
    }

    /**
     * Converts a string into a {@link Component} using legacy serialization.
     *
     * @param content The string to convert.
     * @return A {@link Component} created from the provided string.
     */
    @Nonnull
    public static Component componentFromContent(@Nonnull String content) {
        return LEGACY_SERIALIZER.deserialize(Text.colorize(content));
    }

    /**
     * Serializes a {@link Component} into a string using legacy serialization.
     *
     * @param component The {@link Component} to serialize.
     * @return The serialized string representation of the component.
     */
    @Nonnull
    public static String contentFromComponent(@Nonnull Component component) {
        return LEGACY_SERIALIZER.serialize(component);
    }

    /**
     * Serializes a list of {@link Component} objects into a list of strings using legacy serialization.
     *
     * @param components The list of {@link Component} objects to serialize.
     * @return A list of string representations of the components.
     */
    @Nonnull
    public static List<String> contentFromComponent(@Nonnull List<Component> components) {
        return components.stream().map(ComponentSerializer::contentFromComponent).collect(Collectors.toList());
    }
}
