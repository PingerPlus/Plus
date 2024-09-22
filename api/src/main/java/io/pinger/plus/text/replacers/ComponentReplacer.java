package io.pinger.plus.text.replacers;

import io.pinger.plus.text.Replacer;
import io.pinger.plus.text.ReplacerEntry;
import io.pinger.plus.util.Processor;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class ComponentReplacer implements ReplacerProvider<Component> {
    private static final Set<TextDecoration> DECORATIONS = EnumSet.allOf(TextDecoration.class);

    @Override
    public boolean provides(Class<?> clazz) {
        return Component.class.isAssignableFrom(clazz);
    }

    @Override
    public Component provide(Component object, Replacer replacer) {
        Component replaced = object;

        for (final ReplacerEntry replacerEntry : replacer.entries()) {
            final Component replaceWith = replacerEntry.supplyContent();
            final NamedTextColor color = this.matchColor(replaceWith);
            if (color != null) {
                replaced = this.replaceColor(replaced, replacerEntry.key(), color);
                continue;
            }

            replaced = replaced.replaceText((builder) -> {
                builder.match(Pattern.compile(replacerEntry.key(), Pattern.LITERAL));
                builder.replacement(replaceWith);
            });
        }

        return replaced;
    }

    /**
     * Matches the color of a {@link Component} to the nearest {@link NamedTextColor}.
     *
     * @param component The {@link Component} to check.
     * @return The nearest {@link NamedTextColor} if available, otherwise null.
     */
    private NamedTextColor matchColor(Component component) {
        final TextColor color = component.color();
        if (color == null) {
            return null;
        }
        return NamedTextColor.nearestTo(color);
    }

    /**
     * Creates a {@link Processor} to modify the color of a {@link Component} and its children.
     *
     * @param component The {@link Component} to use as a basis for modifications.
     * @param key       The key to search for.
     * @param color     The {@link NamedTextColor} to apply.
     * @return A {@link Processor} that modifies the component.
     */
    private Processor<Component> modifier(Component component, String key, NamedTextColor color) {
        return (old) -> old
            .colorIfAbsent(color)
            .children(component.children()
                .stream()
                .map((child) -> this.replaceColor(child, key, color).colorIfAbsent(color))
                .collect(Collectors.toList())
        );
    }

    /**
     * Replaces occurrences of a key with a specified color in a {@link Component}.
     *
     * @param component The {@link Component} to process.
     * @param key       The key to search for.
     * @param color     The {@link NamedTextColor} to apply.
     * @return The modified {@link Component}.
     */
    private Component replaceColor(Component component, String key, NamedTextColor color) {
        return this.replaceColor(component, key, color, false);
    }

    /**
     * Replaces occurrences of a key with a specified color in a {@link Component}, optionally resetting decorations.
     *
     * @param component The {@link Component} to process.
     * @param key       The key to search for.
     * @param color     The {@link NamedTextColor} to apply.
     * @param replaced  Indicates whether a replacement has occurred.
     * @return The modified {@link Component}.
     */
    private Component replaceColor(Component component, String key, NamedTextColor color, boolean replaced) {
        if (!(component instanceof TextComponent)) {
            return component;
        }

        final TextComponent text = (TextComponent) component;
        final Processor<Component> modifier = this.modifier(component, key, color);

        boolean replacedHere = replaced;

        final String content = text.content();
        if (content.contains(key)) {
            replacedHere = true;
            return this.replace(text, content, key, modifier);
        } else if (replacedHere) {
            component = component.decorations(new IdentityHashMap<>());
        }

        return component.children(this.applyToChildren(text, key, color, replacedHere));
    }

    /**
     * Applies color replacements to the children of a {@link TextComponent}.
     *
     * @param textComponent The {@link TextComponent} whose children will be processed.
     * @param match         The key to search for.
     * @param color         The {@link NamedTextColor} to apply.
     * @param colorReplaced Indicates whether color was replaced.
     * @return A list of modified child components.
     */
    private List<Component> applyToChildren(@NotNull TextComponent textComponent, @NotNull String match, @NotNull NamedTextColor color, boolean colorReplaced) {
        return textComponent.children()
            .stream()
            .map(child -> this.replaceColor(child, match, color, colorReplaced))
            .collect(Collectors.toList());
    }

    /**
     * Replaces text within a {@link TextComponent} based on a key and a {@link Processor} for modification.
     *
     * @param component The {@link TextComponent} to modify.
     * @param content   The original content of the component.
     * @param match     The key to search for.
     * @param modifier  A {@link Processor} for modifying the component.
     * @return The modified {@link Component}.
     */
    private Component replace(@NotNull TextComponent component, @NotNull String content, @NotNull String match, @NotNull Processor<Component> modifier) {
        final String[] splitParts = content.split(Pattern.quote(match));
        final List<Component> components = new ArrayList<>();

        for (int i = 0; i < splitParts.length; i++) {
            if (!splitParts[i].isEmpty()) {
                components.add(Component.text(splitParts[i]).color(component.color()));
            }
            if (i < splitParts.length - 1) {
                components.add(Component.text("").decorations(DECORATIONS, false));
            }
        }

        return modifier.apply(Component.empty().mergeStyle(component).children(components));
    }
}
