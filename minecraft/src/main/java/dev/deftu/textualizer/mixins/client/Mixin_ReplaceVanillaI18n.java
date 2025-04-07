package dev.deftu.textualizer.mixins.client;

//#if MC >= 1.16.5
import dev.deftu.textualizer.minecraft.MCLocalization;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(I18n.class)
public class Mixin_ReplaceVanillaI18n {

    @Inject(method = "translate", at = @At("HEAD"), cancellable = true)
    private static void textualizer$translate(String key, Object[] replacements, CallbackInfoReturnable<String> cir) {
        if (!MCLocalization.INSTANCE.current().isTranslated(key)) {
            return;
        }

        cir.setReturnValue(MCLocalization.INSTANCE.current().get(key, replacements));
    }

    @Inject(method = "hasTranslation", at = @At("HEAD"), cancellable = true)
    private static void textualizer$hasTranslation(String key, CallbackInfoReturnable<Boolean> cir) {
        if (!MCLocalization.INSTANCE.current().isTranslated(key)) {
            return;
        }

        cir.setReturnValue(true);
    }

}
//#endif
