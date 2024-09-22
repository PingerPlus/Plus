package io.pinger.plus.message;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class ComponentHelper {

    public static boolean isEmpty(@NotNull Component component) {
        return component.equals(Component.empty());
    }

}
