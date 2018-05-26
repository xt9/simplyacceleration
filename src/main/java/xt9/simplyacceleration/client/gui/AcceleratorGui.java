package xt9.simplyacceleration.client.gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.inventory.ContainerAccelerator;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

/**
 * Created by xt9 on 2018-05-26.
 */
public class AcceleratorGui extends GuiContainer {
    private static final int WIDTH =  176;
    private static final int HEIGHT = 90;
    private static final ResourceLocation defaultGui = new ResourceLocation(SimplyConstants.MODID, "textures/gui/default_gui.png");

    public AcceleratorGui(TileEntityAccelerator te, InventoryPlayer inventory, World world) {
        super(new ContainerAccelerator(te, inventory, world));
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = getGuiLeft();
        int top = getGuiTop();

        // Draw player inventory
        Minecraft.getMinecraft().getTextureManager().bindTexture(defaultGui);
        drawTexturedModalRect(left, top , 0, 0, 176, 90);
    }
}
