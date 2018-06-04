package xt9.simplyacceleration;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xt9.simplyacceleration.common.utils.MathHelper;

/**
 * Created by xt9 on 2018-06-02.
 */
@Mod.EventBusSubscriber
@Config(modid = SimplyConstants.MODID)
public class SimplyConfig {
    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(SimplyConstants.MODID)) {
            ConfigManager.sync(SimplyConstants.MODID, Type.INSTANCE);
        }
    }


    @Comment({
        "Max accelerator reach specified in blocks",
        "Machines outside of this range won't be able to be linked",
        "Default: 16"
    })
    @Name("Max accelerator reach")
    @RangeInt(min = 1, max = 64)
    @SuppressWarnings("WeakerAccess")
    public static int maxLinkingRange = 16;
    public static double getMaxLinkingRange() {
        return MathHelper.ensureRange(maxLinkingRange, 1, 64);
    }


    @Comment({
        "WARNING: If you're on a server keep this at a reasonable number",
        "(Default should be considered an absolute maximum, only go higher in SP/Testworlds)",
        "Default: 40"
    })
    @Name("Max speed multiplier for the accelerator")
    @RangeInt(min = 2, max = 999)
    @SuppressWarnings("WeakerAccess")
    public static int maxSpeedMultiplier = 40;
    public static int getMaxSpeedMultiplier() {
        return MathHelper.ensureRange(maxSpeedMultiplier, 2, 999);
    }


    @Comment({
        "Number of machines that can be linked to a single accelerator",
        "Default: 6"
    })
    @Name("Max linked machines")
    @RangeInt(min = 1, max = 32)
    @SuppressWarnings("WeakerAccess")
    public static int maxLinkedMachines = 6;
    public static int getMaxLinkedMachines() {
        return MathHelper.ensureRange(maxLinkedMachines, 1, 32);
    }


    @Comment({
        "The energy consumption for the accelerator, specified in FE/t",
        "Default: 1024"
    })
    @Name("Accelerator energy consumption")
    @RangeInt(min = 256, max = 512000)
    @SuppressWarnings("WeakerAccess")
    public static int acceleratorEnergyConsumption = 1024;
    public static int getAcceleratorEnergyConsumption() { return MathHelper.ensureRange(acceleratorEnergyConsumption, 256, 512000); }
}
