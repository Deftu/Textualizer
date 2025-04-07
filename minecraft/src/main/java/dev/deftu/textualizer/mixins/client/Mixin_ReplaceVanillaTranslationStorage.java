package dev.deftu.textualizer.mixins.client;

//#if MC >= 1.16.5
import dev.deftu.textualizer.minecraft.MCLocalization;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TranslationStorage.class)
public class Mixin_ReplaceVanillaTranslationStorage {

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void textualizer$get(
            String key,
            //#if MC >= 1.19.4
            String fallback,
            //#endif
            CallbackInfoReturnable<String> cir
    ) {
        if (!MCLocalization.INSTANCE.current().isTranslated(key)) {
            return;
        }

        cir.setReturnValue(MCLocalization.INSTANCE.current().get(key));
    }

    @Inject(method = "hasTranslation", at = @At("HEAD"), cancellable = true)
    private void textualizer$hasTranslation(String key, CallbackInfoReturnable<Boolean> cir) {
        if (!MCLocalization.INSTANCE.current().isTranslated(key)) {
            return;
        }

        cir.setReturnValue(true);
    }

}
//#endif
