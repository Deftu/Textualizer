package dev.deftu.textualizer.minecraft

//#if MC <= 1.12.2
//$$ internal object LocalePropertiesManipulator {
//$$
//$$     private const val CURRENT_LOCALE_FIELD_NAME =
//#if MC >= 1.12.2
//$$         "CURRENT_LOCALE"
//#else
//$$         "currentLocale"
//#endif
//$$
    //$$     fun replace(language: MCLanguage) {
//$$         val currentLocaleField = net.minecraft.client.resources.LanguageManager::class.java.getDeclaredField(CURRENT_LOCALE_FIELD_NAME)
//$$         currentLocaleField.isAccessible = true
//$$         val currentLocale = currentLocaleField.get(null) as net.minecraft.client.resources.Locale
//$$
//$$         val propertiesField = currentLocale.javaClass.getDeclaredField("properties")
//$$         propertiesField.isAccessible = true
//$$         val properties = propertiesField.get(currentLocale) as MutableMap<String, String>
//$$         properties.putAll(language.translations)
//$$         propertiesField.set(currentLocale, properties)
//$$     }
//$$
//$$ }
//#endif
