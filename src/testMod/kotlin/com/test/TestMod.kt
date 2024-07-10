package com.test

//#if FABRIC
import net.fabricmc.api.ClientModInitializer
//#else
//#if FORGE
//#if MC >= 1.15.2
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#else
//$$ import dev.deftu.textualizer.LanguageManager
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.Mod.EventHandler
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#else
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//$$ import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
//#endif
//#endif

import dev.deftu.omnicore.client.OmniKeyBinding
import dev.deftu.omnicore.client.OmniKeyboard
import dev.deftu.textualizer.text.TextualizerTextHolder
import java.util.Timer
import java.util.TimerTask

//#if FABRIC
class TestMod : ClientModInitializer {
//#else
//#if MC >= 1.15.2
//$$ @Mod(value = "test-mod")
//#else
//$$ @Mod(modid = "test-mod")
//#endif
//$$ class TestMod {
//#endif

    private val keyBinding = OmniKeyBinding("testmod.test", "Test Mod", OmniKeyboard.KEY_M)

    //#if FORGE && MC >= 1.15.2
    //$$ init {
    //$$     FMLJavaModLoadingContext.get().modEventBus.register(this)
    //$$ }
    //#endif

    //#if NEOFORGE
    //$$ constructor(modEventBus: IEventBus) {
    //$$     modEventBus.register(this)
    //$$ }
    //#endif

    //#if FABRIC
    override
    //#elseif MC <= 1.12.2
    //$$ @Mod.EventHandler
    //#endif
    fun onInitializeClient(
        //#if FORGE-LIKE
        //#if MC >= 1.15.2
        //$$ event: FMLClientSetupEvent
        //#else
        //$$ event: FMLInitializationEvent
        //#endif
        //#endif
    ) {
        //#if MC <= 1.12.2
        //$$ LanguageManager.initialize()
        //#endif
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val holder = TextualizerTextHolder("testmod.test")
                println(holder.asString())
            }
        }, 10_000)
        //#if FABRIC || MC <= 1.12.2
        keyBinding.attemptRegister()
        //#endif
    }

}
