package dev.deftu.textualizer.text

import dev.deftu.textile.TextFormat
import dev.deftu.textile.ValueBackedTextHolder
import dev.deftu.textualizer.Localization

public class LocalizedTextHolder(
    public val localization: Localization<*>,
    public val key: String,
    public val replacements: Array<out Any>
) : ValueBackedTextHolder<LocalizedTextHolder, TextFormat>(localization.get(key, *replacements)) {

    override fun copy(): LocalizedTextHolder {
        return LocalizedTextHolder(localization, key, replacements).apply {
            _children.addAll(this@LocalizedTextHolder.children)
            _formatting.addAll(this@LocalizedTextHolder.formatting)
        }
    }

    override fun toString(): String {
        return "LocalizedTextHolder(content='$content', _formatting=$_formatting, _children=$_children, localization=$localization, key='$key', replacements=${replacements.contentToString()})"
    }

}
