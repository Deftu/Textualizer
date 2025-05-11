package dev.deftu.textualizer.text

import dev.deftu.textile.TextFormat
import dev.deftu.textile.ValueBackedTextHolder
import dev.deftu.textualizer.LocalizationContext

public class LocalizedTextHolder(
    public val context: LocalizationContext,
    public val key: String,
    public val replacements: Array<out Any>
) : ValueBackedTextHolder<LocalizedTextHolder, TextFormat>(context.get(key, *replacements)) {

    override fun copy(): LocalizedTextHolder {
        return LocalizedTextHolder(context, key, replacements).apply {
            _children.addAll(this@LocalizedTextHolder.children)
            _formatting.addAll(this@LocalizedTextHolder.formatting)
        }
    }

    override fun toString(): String {
        return "LocalizedTextHolder(content='$content', _formatting=$_formatting, _children=$_children, context=$context, key='$key', replacements=${replacements.contentToString()})"
    }

}
