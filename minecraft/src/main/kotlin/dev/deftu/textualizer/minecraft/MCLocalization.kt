package dev.deftu.textualizer.minecraft

import dev.deftu.textualizer.Localization
import dev.deftu.textualizer.LocalizationContext

public object MCLocalization : Localization<MCLanguage> {

    private var language: MCLanguage? = null

    override fun refresh() {
        set(MCResourceManager.get())
    }

    override fun with(language: MCLanguage): LocalizationContext {
        return MCLocalizationContext(language)
    }

    public fun current(): LocalizationContext {
        return with(get())
    }

    public fun set(code: String) {
        set(MCResourceManager.get(code))
    }

    public fun set(language: MCLanguage) {
        this.language = language
        //#if MC <= 1.12.2
        //$$ LocalePropertiesManipulator.replace(language)
        //#endif
    }

    public fun get(): MCLanguage {
        return this.language ?: MCResourceManager.defaultLanguage
    }

}
