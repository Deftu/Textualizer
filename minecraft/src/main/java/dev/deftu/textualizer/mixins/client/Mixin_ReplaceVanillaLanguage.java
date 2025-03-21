package dev.deftu.textualizer.mixins.client;

//#if MC >= 1.19.4
import dev.deftu.textualizer.minecraft.MCLocalization;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Language.class)
public class Mixin_ReplaceVanillaLanguage {

    @Inject(method = "get(Ljava/lang/String;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void textualizer$get(String key, CallbackInfoReturnable<String> cir) {
        if (!MCLocalization.INSTANCE.isTranslated(key)) {
            return;
        }

        cir.setReturnValue(MCLocalization.INSTANCE.get(key));
    }

}
//#endif
