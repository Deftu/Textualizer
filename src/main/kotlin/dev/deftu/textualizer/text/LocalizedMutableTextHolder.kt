package dev.deftu.textualizer.text

import dev.deftu.textile.TextFormat
import dev.deftu.textile.ValueBackedMutableTextHolder
import dev.deftu.textualizer.Localization

public class LocalizedMutableTextHolder(
    public val localization: Localization<*>,
    public val key: String,
    public val replacements: Array<out Any>
) : ValueBackedMutableTextHolder<LocalizedMutableTextHolder, TextFormat>(localization.get(key, *replacements)) {

    override fun copy(): LocalizedMutableTextHolder {
        return LocalizedMutableTextHolder(localization, key, replacements).apply {
            _children.addAll(this@LocalizedMutableTextHolder.children)
            _formatting.addAll(this@LocalizedMutableTextHolder.formatting)
        }
    }

    override fun toString(): String {
        return "LocalizedMutableTextHolder(content='$content', _formatting=$_formatting, _children=$_children, localization=$localization, key='$key', replacements=${replacements.contentToString()})"
    }

}
