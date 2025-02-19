package dev.deftu.textualizer

//#if MC <= 1.12.2
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import net.minecraft.client.Minecraft
//$$ import net.minecraft.client.resources.data.MetadataSerializer
//#endif

import net.minecraft.client.resource.language.LanguageDefinition
import net.minecraft.client.resource.metadata.LanguageResourceMetadata
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourcePack
import org.jetbrains.annotations.ApiStatus
import java.util.*
import kotlin.streams.toList

@ApiStatus.Internal
public object AvailableLanguageLoader {

    //#if MC <= 1.12.2
    //$$ private lateinit var metadataSerializer: MetadataSerializer
    //#endif

    @ApiStatus.Internal
    public fun getResourcePacks(resourceManager: ResourceManager): Collection<ResourcePack> {
        //#if MC >= 1.16.5
        @Suppress("RedundantSuppression", "Since15")
        return resourceManager.streamResourcePacks().toList()
        //#else
        //$$ return getResourcePacks()
        //#endif
    }

    @ApiStatus.Internal
    public fun loadFromResourcePacks(packs: Collection<ResourcePack>): Map<String, LanguageMetadata> {
        //#if MC <= 1.12.2
        //$$ cacheMetadataSerializer()
        //#endif
        val result = mutableMapOf<String, LanguageMetadata>()
        for (pack in packs) {
            val metadata =
                //#if MC >= 1.16.5
                pack.parseMetadata(
                //#else
                //$$ pack.getPackMetadata(
                //#endif

                //#if MC >= 1.19.3
                LanguageResourceMetadata.SERIALIZER
                //#elseif MC >= 1.16.5
                //$$ LanguageResourceMetadata.READER
                //#else
                //$$ metadataSerializer,
                //$$ "language"
                //#endif
            )
                    //#if MC <= 1.12.2
                    //$$ as? LanguageMetadataSection
                    //#endif
                    ?: continue
            for (
                //#if MC >= 1.19.3
                (key, value) in metadata.definitions
                //#else
                //$$ definition in metadata.languageDefinitions
                //#endif
            ) {
                //#if MC <= 1.19.2
                //$$ val key = definition.code
                //$$ val value = definition
                //#endif
                result[key] = loadFromLanguageDefinition(value)
            }
        }

        return result
    }

    @ApiStatus.Internal
    public fun loadFromLanguageDefinition(definition: LanguageDefinition): LanguageMetadata {
        return LanguageMetadata(
            //#if FABRIC && MC >= 1.16.5
            //#if MC >= 1.19.3
            definition.comp_1198.lowercase(Locale.US),
            definition.comp_1199.lowercase(Locale.US),
            //#else
            //$$ definition.region.lowercase(Locale.US),
            //$$ definition.name.lowercase(Locale.US),
            //#endif
            //#else
            //#if MC >= 1.16.5
            //$$ definition.region.lowercase(Locale.US),
            //$$ definition.name.lowercase(Locale.US),
            //#else
            //$$ definition.getRegion().lowercase(Locale.US),
            //$$ definition.getName().lowercase(Locale.US),
            //#endif
            //#endif
            //#if MC >= 1.19.3
            definition.rightToLeft
            //#else
            //$$ definition.isRightToLeft
            //#endif
        )
    }

    //#if MC <= 1.12.2
    //$$ private fun getResourcePacks(): List<IResourcePack> {
    //$$     val result = mutableListOf<IResourcePack>(
    //#if FORGE && MC == 1.8.9
    //$$             OmniClient.getInstance().mcDefaultResourcePack
    //#else
    //$$             OmniClient.getInstance().defaultResourcePack
    //#endif
    //$$     )
    //$$     val packRepository = OmniClient.getInstance().resourcePackRepository
    //$$     for (entry in packRepository.repositoryEntries) {
    //$$         result.add(entry.resourcePack)
    //$$     }
    //$$
    //$$     packRepository.serverResourcePack?.let(result::add)
    //$$     return result
    //$$ }
    //$$
    //$$ private fun cacheMetadataSerializer() {
    //$$     if (::metadataSerializer.isInitialized) {
    //$$         return
    //$$     }
    //$$
    //$$     val field = Minecraft::class.java.getDeclaredField(
    //#if MC >= 1.12.2
    //$$         "metadataSerializer"
    //#else
    //$$         "metadataSerializer_"
    //#endif
    //$$     )
    //$$     field.isAccessible = true
    //$$     metadataSerializer = field.get(OmniClient.getInstance()) as MetadataSerializer
    //$$ }
    //$$
    //$$ private fun Language.getRegion(): String {
    //$$     val field = Language::class.java.getDeclaredField("region")
    //$$     field.isAccessible = true
    //$$     return field.get(this) as String
    //$$ }
    //$$
    //$$ private fun Language.getName(): String {
    //$$     val field = Language::class.java.getDeclaredField("name")
    //$$     field.isAccessible = true
    //$$     return field.get(this) as String
    //$$ }
    //$$
    //#if FABRIC
    //$$ private val MinecraftClient.defaultResourcePack: ResourcePack
    //$$     get() {
    //$$         val field = MinecraftClient::class.java.getDeclaredField("defaultResourcePack")
    //$$         field.isAccessible = true
    //$$         return field.get(this) as ResourcePack
    //$$     }
    //#endif
    //#endif

}
