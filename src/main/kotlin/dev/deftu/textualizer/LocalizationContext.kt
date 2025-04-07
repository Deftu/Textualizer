package dev.deftu.textualizer

public interface LocalizationContext {

    public fun get(key: String, vararg replacements: Any): String

    public fun get(key: String, replacements: List<Any>): String

    public fun isTranslated(key: String): Boolean

}
