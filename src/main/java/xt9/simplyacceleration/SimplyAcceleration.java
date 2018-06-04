package xt9.simplyacceleration;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xt9.simplyacceleration.common.Registry;
import xt9.simplyacceleration.common.ServerProxy;
import xt9.simplyacceleration.common.network.ClearAllLinkedMessage;
import xt9.simplyacceleration.common.network.ToggleAcceleratorHighlighting;
import xt9.simplyacceleration.common.network.UpdateAcceleratorTickMessage;

/**
 * Created by xt9 on 2018-05-26.
 */
@Mod(modid = SimplyConstants.MODID, version = SimplyConstants.VERSION, useMetadata = true, acceptedMinecraftVersions = "[1.12.2]")
@Mod.EventBusSubscriber
public class SimplyAcceleration {
    private int networkID = 0;

    @Mod.Instance(SimplyConstants.MODID)
    public static SimplyAcceleration instance;

    @SidedProxy(clientSide="xt9.simplyacceleration.client.ClientProxy", serverSide="xt9.simplyacceleration.common.ServerProxy")
    public static ServerProxy proxy;

    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(SimplyConstants.MODID);
        network.registerMessage(UpdateAcceleratorTickMessage.Handler.class, UpdateAcceleratorTickMessage.class, networkID++, Side.SERVER);
        network.registerMessage(ToggleAcceleratorHighlighting.Handler.class, ToggleAcceleratorHighlighting.class, networkID++, Side.SERVER);
        network.registerMessage(ClearAllLinkedMessage.Handler.class, ClearAllLinkedMessage.class, networkID++, Side.SERVER);
        proxy.preInit();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Registry.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Registry.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        proxy.registerModels();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderers();
    }

    public static CreativeTabs creativeTab = new CreativeTabs(SimplyConstants.MODID) {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getTabIconItem() {
            return ItemStack.EMPTY;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(Registry.blockAcceleratorItem);
        }
    };
}
