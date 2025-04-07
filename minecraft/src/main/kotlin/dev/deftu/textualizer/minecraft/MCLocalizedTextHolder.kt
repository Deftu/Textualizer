package dev.deftu.textualizer.minecraft

import dev.deftu.textile.minecraft.MCSimpleTextHolder

public class MCLocalizedTextHolder(
    public val key: String,
    public val replacements: Array<out Any>
) : MCSimpleTextHolder(MCLocalization.current().get(key, *replacements)) {

    public constructor(key: String): this(key, emptyArray())

    override fun copy(): MCLocalizedTextHolder {
        return MCLocalizedTextHolder(key, replacements).apply {
            clickEvent = this@MCLocalizedTextHolder.clickEvent
            hoverEvent = this@MCLocalizedTextHolder.hoverEvent
        }
    }

    override fun toString(): String {
        return "MCLocalizedTextHolder(content='$content', formatting=$_formatting, children=$_children, clickEvent=$clickEvent, hoverEvent=$hoverEvent, key='$key', replacements=${replacements.contentToString()})"
    }

}
