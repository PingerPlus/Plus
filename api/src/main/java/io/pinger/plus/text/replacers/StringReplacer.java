package io.pinger.plus.text.replacers;

import io.pinger.plus.message.ComponentSerializer;
import io.pinger.plus.text.Replacer;
import io.pinger.plus.text.ReplacerEntry;
import io.pinger.plus.text.Text;

public class StringReplacer implements ReplacerProvider<String> {

    @Override
    public boolean provides(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public String provide(String object, Replacer replacer) {
        String replaced = object;

        for (final ReplacerEntry replacerEntry : replacer.entries()) {
            final String replaceWith = ComponentSerializer.contentFromComponent(replacerEntry.supplyContent());
            replaced = replaced.replace(replacerEntry.key(), Text.colorize(replaceWith));
        }

        return replaced;
    }
}
