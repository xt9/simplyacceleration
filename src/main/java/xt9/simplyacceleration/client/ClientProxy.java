package xt9.simplyacceleration.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.client.gui.AcceleratorGui;
import xt9.simplyacceleration.client.gui.InformationOverlay;
import xt9.simplyacceleration.client.particles.ParticleIndicator;
import xt9.simplyacceleration.common.Registry;
import xt9.simplyacceleration.common.ServerProxy;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

import javax.annotation.Nullable;

/**
 * Created by xt9 on 2018-05-26.
 */
public class ClientProxy extends ServerProxy {
    @Override
    public void preInit() {}

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case SimplyConstants.ACCELERATOR_GUI_ID: return new AcceleratorGui((TileEntityAccelerator) world.getTileEntity(new BlockPos(x, y, z)), player.inventory, world);
            default: return null;
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void registerModels() {
        Registry.blocks.forEach(block -> ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory")));
        Registry.items.forEach(item -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory")));
    }

    @Override
    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(new InformationOverlay(Minecraft.getMinecraft()));
    }


    public void spawnParticle(World world, double x, double y, double z, double mx, double my, double mz) {
        Particle particle = new ParticleIndicator(world, x, y, z, mx, my, mz, 1.3F);
        particle.setRBGColorF(1.0F, 1.0F, 0.2F);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
