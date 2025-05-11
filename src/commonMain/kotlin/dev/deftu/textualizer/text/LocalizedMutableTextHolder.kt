package dev.deftu.textualizer.text

import dev.deftu.textile.TextFormat
import dev.deftu.textile.ValueBackedMutableTextHolder
import dev.deftu.textualizer.LocalizationContext

public class LocalizedMutableTextHolder(
    public val context: LocalizationContext,
    public val key: String,
    public val replacements: Array<out Any>
) : ValueBackedMutableTextHolder<LocalizedMutableTextHolder, TextFormat>(context.get(key, *replacements)) {

    override fun copy(): LocalizedMutableTextHolder {
        return LocalizedMutableTextHolder(context, key, replacements).apply {
            _children.addAll(this@LocalizedMutableTextHolder.children)
            _formatting.addAll(this@LocalizedMutableTextHolder.formatting)
        }
    }

    override fun toString(): String {
        return "LocalizedMutableTextHolder(content='$content', _formatting=$_formatting, _children=$_children, context=$context, key='$key', replacements=${replacements.contentToString()})"
    }

}
