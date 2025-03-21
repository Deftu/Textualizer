package dev.deftu.textualizer.minecraft

import dev.deftu.textualizer.Localization

public object MCLocalization : Localization<MCLanguage> {

    private var language: MCLanguage? = null

    override fun refresh() {
        set(MCResourceManager.get())
    }

    override fun set(code: String) {
        set(MCResourceManager.get(code))
    }

    override fun set(language: MCLanguage) {
        this.language = language
        //#if MC <= 1.12.2
        //$$ LocalePropertiesManipulator.replace(language)
        //#endif
    }

    override fun get(key: String, vararg replacements: Any): String {
        val translations = language?.translations ?: return key
        var translation = translations[key]
        if (translation == null) {
            val fallbackTranslations = language?.fallback?.translations ?: return key
            translation = fallbackTranslations[key] ?: return key
        }

        return translation.format(*replacements)
    }

    override fun get(key: String, replacements: List<Any>): String {
        return get(key, *replacements.toTypedArray())
    }

    override fun isTranslated(key: String): Boolean {
        return language?.translations?.containsKey(key) ?: language?.fallback?.translations?.containsKey(key) ?: false
    }

}
