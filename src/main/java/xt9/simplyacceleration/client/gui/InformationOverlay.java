package xt9.simplyacceleration.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import xt9.simplyacceleration.SimplyConfig;
import xt9.simplyacceleration.common.blocks.BlockAccelerator;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

/**
 * Created by xt9 on 2018-06-02.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class InformationOverlay extends GuiScreen {
    private FontRenderer fontRender;
    private Minecraft minecraft;

    public InformationOverlay(Minecraft mc) {
        super();
        minecraft = mc;
        fontRender = mc.fontRenderer;
    }

    /* Needed on 1.12 to render tooltips */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        } else if(!minecraft.inGameHasFocus) {
            return;
        }

        RayTraceResult lookTrace = getPlayerLookRayTrace(minecraft.player, event.getPartialTicks());
        if(isLookingAtAccelerator(lookTrace, minecraft.player)) {
            renderAcceleratorInfo(lookTrace, minecraft.player);
        }
    }

    private RayTraceResult getPlayerLookRayTrace(EntityPlayerSP player, float partialTicks) {
        Vec3d start  = player.getPositionEyes(partialTicks);
        Vec3d vec31 = player.getLook(partialTicks);
        Vec3d end = start.addVector(vec31.x * 4, vec31.y * 4, vec31.z * 4);
        return player.world.rayTraceBlocks(start, end);
    }

    private boolean isLookingAtAccelerator(RayTraceResult lookTrace, EntityPlayerSP player) {
        if(lookTrace != null) {
            if(lookTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = lookTrace.getBlockPos();
                IBlockState blockState = player.world.getBlockState(pos);
                return blockState.getBlock() instanceof BlockAccelerator;
            }
        }
        return false;
    }

    private void renderAcceleratorInfo(RayTraceResult lookTrace, EntityPlayerSP player) {
        int x = getScreenCenterX();
        int y = getScreenCenterY() + 10;

        TileEntity accelerator = null;
        if(lookTrace != null) {
            if(lookTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = lookTrace.getBlockPos();
                accelerator = player.world.getTileEntity(pos);
            }
        }

        NonNullList<String> lines = NonNullList.create();
        if(accelerator instanceof TileEntityAccelerator) {
            NonNullList<Long> longPositions = ((TileEntityAccelerator) accelerator).getLongPositions();
            longPositions.forEach(l -> {
                BlockPos pos = BlockPos.fromLong(l);
                IBlockState state = player.world.getBlockState(pos);
                lines.add(state.getBlock().getLocalizedName() + " @ (x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ() + ")");
            });
        }

        int maxRows = new ScaledResolution(minecraft).getScaleFactor() == 4 ? 3 : 6;
        String headerText = lines.size() > 0 ? "Linked Machines (" + lines.size() + "/" + SimplyConfig.getMaxLinkedMachines() + ")" : "No linked machines.";
        drawCenteredString(fontRender, headerText, x, y, 0xFB6E15);
        for (int i = 0; i < lines.size(); i++) {
            if(i < maxRows) {
                drawCenteredString(fontRender, lines.get(i), x, y + 12 + (i * 14), 0xFFFFFF);
            }
        }

        if(lines.size() > maxRows) {
            drawCenteredString(fontRender, "...", x, y + (maxRows * 16), 0xFB6E15);
        }
    }

    private int getScreenCenterX() {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        int test = scaledResolution.getScaleFactor();
        return scaledResolution.getScaledWidth() / 2;
    }

    private int getScreenCenterY() {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        return scaledResolution.getScaledHeight() / 2;
    }
}
