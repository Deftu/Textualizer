package dev.deftu.textualizer.minecraft

import dev.deftu.omnicore.common.OmniLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.ReloadableResourceManagerImpl
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SynchronousResourceReloader
import java.io.InputStream
import java.util.*

//#if FORGE-LIKE && MC >= 1.16.5
//$$ import cpw.mods.modlauncher.Launcher
//#endif

public object MCResourceManager : SynchronousResourceReloader {

    private const val DEFAULT = "en_us"

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ private val modLauncherClassLoader: ClassLoader
    //$$     get() {
    //$$         val launcher = Launcher.INSTANCE
    //$$         val field = launcher.javaClass.getDeclaredField("classLoader")
    //$$         field.isAccessible = true
    //$$         return field.get(launcher) as ClassLoader
    //$$     }
    //#endif

    private val allMetadata = mutableMapOf<String, LanguageMetadata>()

    public val currentLanguageCode: String
        get() {
            return MinecraftClient.getInstance().languageManager.language
                //#if MC <= 1.19.2
                //$$ .code
                //#endif
                .lowercase(Locale.US)
        }

    public val defaultLanguage: MCLanguage by lazy {
        val resourceManager = this.resourceManager ?: throw IllegalStateException("Resource manager is not set")
        val translations = load(resourceManager, DEFAULT)

        MCLanguage(
            fallback = null, // This IS the fallback...
            code = DEFAULT,
            region = "US",
            isRightToLeft = false,
            nativeName = "English (US)",
            translations = translations
        )
    }

    private var isInitialized = false
    private var resourceManager: ResourceManager? = null

    public fun initialize() {
        if (this.isInitialized) {
            return
        }

        val resourceManager = MinecraftClient.getInstance().resourceManager ?: return
        if (resourceManager is ReloadableResourceManagerImpl) {
            this.isInitialized = true
            resourceManager.registerReloader(this)
            //#if MC >= 1.16.5
            this.reload(resourceManager)
            //#endif
        }
    }

    public fun get(code: String): MCLanguage {
        initialize() // Ensure that we've initialized at least once
        val resourceManager = this.resourceManager ?: throw IllegalStateException("Resource manager is not set")

        val loaded = mutableSetOf(DEFAULT)
        var metadata = this.allMetadata[DEFAULT]
        var isRightToLeft = false
        if (this.currentLanguageCode !in loaded) {
            metadata = this.allMetadata[this.currentLanguageCode]
            if (metadata != null) {
                isRightToLeft = metadata.isRightToLeft
            }

            loaded.add(currentLanguageCode)
        }

        metadata ?: throw IllegalStateException("Language metadata not found")

        val translations = load(resourceManager, code)
        println("Loaded translations for $code: $translations")
        return MCLanguage(
            fallback = defaultLanguage,
            code = code,
            region = metadata.region,
            isRightToLeft = isRightToLeft,
            nativeName = code,
            translations = translations
        )
    }

    public fun get(): MCLanguage {
        return this.get(currentLanguageCode)
    }

    override fun reload(resourceManager: ResourceManager) {
        this.resourceManager = resourceManager

        this.allMetadata.clear()
        this.allMetadata.putAll(loadFromResourcePacks(getResourcePacks(resourceManager)))

        MCLocalization.refresh()
    }

    private fun load(resourceManager: ResourceManager, code: String): Map<String, String> {
        val loaded = mutableMapOf<String, String>()

        val formattedCode = buildString {
            val split = code.split("_")
            append(split[0].lowercase(Locale.ENGLISH))
            if (split.size > 1) {
                append("_")
                append(split[1].uppercase(Locale.ENGLISH))
            }
        }

        val checkedPaths = mutableSetOf<String>()
        val path = "languages/$formattedCode.json"
        val namespaces = resourceManager.allNamespaces + OmniLoader.loadedMods.map(OmniLoader.ModInfo::id)
        for (namespace in namespaces) {
            val fullPath = "/assets/$namespace/$path"
            if (fullPath in checkedPaths) {
                continue
            }

            try {
                loaded.putAll(loadPath(fullPath))
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        return loaded
    }

    private fun loadPath(path: String): Map<String, String> {
        val inputStream = getResource(path) ?: return emptyMap()
        val loaded = mutableMapOf<String, String>()
        MCLanguage.load(inputStream, loaded::put)
        inputStream.close()
        return loaded
    }

    private fun getResource(path: String): InputStream? {
        //#if FABRIC || MC <= 1.12.2
        return this::class.java.getResourceAsStream(path)
        //#else
        //$$ println(modLauncherClassLoader)
        //$$ return modLauncherClassLoader.getResourceAsStream(path)
        //#endif
    }

}
