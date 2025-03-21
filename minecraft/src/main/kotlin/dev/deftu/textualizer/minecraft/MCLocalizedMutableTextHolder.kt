package dev.deftu.textualizer.minecraft

import dev.deftu.textile.minecraft.MCSimpleMutableTextHolder

public class MCLocalizedMutableTextHolder(
    public val key: String,
    public val replacements: Array<out Any>
) : MCSimpleMutableTextHolder(MCLocalization.get(key, *replacements)) {

    public constructor(key: String): this(key, emptyArray())

    override fun copy(): MCLocalizedMutableTextHolder {
        return MCLocalizedMutableTextHolder(key, replacements).apply {
            clickEvent = this@MCLocalizedMutableTextHolder.clickEvent
            hoverEvent = this@MCLocalizedMutableTextHolder.hoverEvent
        }
    }

    override fun toString(): String {
        return "MCLocalizedMutableTextHolder(content='$content', formatting=$_formatting, children=$_children, clickEvent=$clickEvent, hoverEvent=$hoverEvent, key='$key', replacements=${replacements.contentToString()})"
    }

}
