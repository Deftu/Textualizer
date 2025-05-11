package dev.deftu.textualizer

public interface Localization<T : Language<T>> {

    public fun refresh()

    public fun with(language: T): LocalizationContext

}
