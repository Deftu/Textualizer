package dev.deftu.textualizer.minecraft

import dev.deftu.textualizer.LocalizationContext

public class MCLocalizationContext(
    private val language: MCLanguage
) : LocalizationContext {

    override fun get(key: String, vararg replacements: Any): String {
        val translations = language.translations
        var translation = translations[key]
        if (translation == null) {
            val fallbackTranslations = language.fallback?.translations ?: return key
            translation = fallbackTranslations[key] ?: return key
        }

        return translation.format(*replacements)
    }

    override fun get(key: String, replacements: List<Any>): String {
        return get(key, *replacements.toTypedArray())
    }

    override fun isTranslated(key: String): Boolean {
        val translations = language.translations
        if (translations.containsKey(key)) {
            return true
        }

        val fallbackTranslations = language.fallback?.translations ?: return false
        return fallbackTranslations.containsKey(key)
    }

}
