package dev.deftu.textualizer

public interface Localization<T : Language<T>> {

    public fun refresh()

    public fun set(code: String)

    public fun set(language: T)

    public fun get(key: String, vararg replacements: Any): String

    public fun get(key: String, replacements: List<Any>): String

    public fun isTranslated(key: String): Boolean

}
