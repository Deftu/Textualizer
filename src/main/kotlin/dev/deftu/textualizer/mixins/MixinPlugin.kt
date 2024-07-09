package dev.deftu.textualizer.mixins

//#if MC >= 1.16.5
import org.jetbrains.annotations.ApiStatus
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

@ApiStatus.Internal
public class MixinPlugin : IMixinConfigPlugin {

    override fun getMixins(): MutableList<String> {
        val result = mutableListOf<String>()

        //#if MC >= 1.19.4
        //$$ result.add("client.Mixin_ReplaceVanillaLanguage")
        //#endif

        return result
    }

    override fun getRefMapperConfig(): String? = null
    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean = true

    override fun onLoad(mixinPackage: String) {
        // no-op
    }

    override fun acceptTargets(myTargets: MutableSet<String>, otherTargets: MutableSet<String>) {
        // no-op
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
        // no-op
    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
        // no-op
    }

}
//#endif
