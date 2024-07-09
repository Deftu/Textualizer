package dev.deftu.textualizer.mixins.client;

//#if MC >= 1.16.5
import dev.deftu.textualizer.LanguageManager;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftClient.class)
public class Mixin_InitializeLanguageManager {

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;addResourcePackProfilesToManager(Lnet/minecraft/resource/ResourcePackManager;)V"))
    private void textualizer$onResourceManagerInitialized(RunArgs runArgs, CallbackInfo ci) {
        LanguageManager.initialize();
    }

}
//#endif
