package xt9.simplyacceleration.client.gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xt9.simplyacceleration.SimplyAcceleration;
import xt9.simplyacceleration.SimplyConfig;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.inventory.ContainerAccelerator;
import xt9.simplyacceleration.common.network.ClearAllLinkedMessage;
import xt9.simplyacceleration.common.network.ToggleAcceleratorHighlighting;
import xt9.simplyacceleration.common.network.UpdateAcceleratorTickMessage;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;
import xt9.simplyacceleration.common.utils.KeyboardHelper;
import xt9.simplyacceleration.common.utils.MathHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xt9 on 2018-05-26.
 */
public class AcceleratorGui extends GuiContainer {
    private static final int WIDTH =  176;
    private static final int HEIGHT = 166;
    private static final ResourceLocation defaultGui = new ResourceLocation(SimplyConstants.MODID, "textures/gui/accelerator.png");
    private int writeSendCooldown;
    private GuiTextField tickMultiplierField;
    private GuiButton tickDecreaseButton;
    private GuiButton tickIncreaseButton;
    private ToggleButton toggleHighlightingButton;
    private GuiButton clearLinkedButton;
    private TileEntityAccelerator tile;

    public AcceleratorGui(TileEntityAccelerator te, InventoryPlayer inventory, World world) {
        super(new ContainerAccelerator(te, inventory, world));
        xSize = WIDTH;
        ySize = HEIGHT;
        this.tile = te;
    }

    @Override
    public void initGui() {
        super.initGui();
        tickMultiplierField = new GuiTextField(0, fontRenderer, getGuiLeft() + 75, getGuiTop() + 28, 26, 16);
        tickMultiplierField.setEnableBackgroundDrawing(true);
        tickMultiplierField.setMaxStringLength(3);
        tickMultiplierField.setVisible(true);
        tickMultiplierField.setEnabled(true);
        tickMultiplierField.setTextColor(0xFB6E15);
        tickMultiplierField.setText("" + tile.getTickMultiplier());
        tickMultiplierField.setValidator(s -> {
            assert s != null;
            return MathHelper.isStringNumeric(s) || s.equals("");
        });

        tickDecreaseButton = new GuiButton(0, getGuiLeft() + 48, getGuiTop() + 26, 24, 20, "-");
        tickIncreaseButton = new GuiButton(1, getGuiLeft() + 104, getGuiTop() + 26, 24, 20, "+");
        toggleHighlightingButton = new ToggleButton(2, getGuiLeft() + 7, getGuiTop() + 60, 80, 20, tile.isHighlighting());
        clearLinkedButton = new GuiButton(3, getGuiLeft() + 89, getGuiTop() + 60, 80, 20, "Clear linked");

        buttonList.add(tickDecreaseButton);
        buttonList.add(tickIncreaseButton);
        buttonList.add(toggleHighlightingButton);
        buttonList.add(clearLinkedButton);
    }


    @Override
    public void updateScreen() {
        super.updateScreen();
        tickMultiplierField.updateCursorCounter();
        toggleHighlightingButton.setHighlightingState(tile.isHighlighting());

        if(writeSendCooldown > 0) {
            writeSendCooldown--;
        }

        if(writeSendCooldown == 0 && MathHelper.getIntFromStringSafe(tickMultiplierField.getText()) != tile.getTickMultiplier()) {
            SimplyAcceleration.network.sendToServer(new UpdateAcceleratorTickMessage(MathHelper.getIntFromStringSafe(tickMultiplierField.getText())));
        }

        if(MathHelper.getIntFromStringSafe(tickMultiplierField.getText()) > SimplyConfig.getMaxSpeedMultiplier()) {
            tickMultiplierField.setText("" + SimplyConfig.getMaxSpeedMultiplier());
        }

        if(MathHelper.getIntFromStringSafe(tickMultiplierField.getText()) < 1) {
            tickMultiplierField.setText("" + 1);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(tickMultiplierField.textboxKeyTyped(typedChar, keyCode)) {
            writeSendCooldown = 6;
        }
        if(keyCode == 28) {
            tickMultiplierField.setFocused(false);
            SimplyAcceleration.network.sendToServer(new UpdateAcceleratorTickMessage(MathHelper.getIntFromStringSafe(tickMultiplierField.getText())));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(tickMultiplierField.mouseClicked(mouseX, mouseY, mouseButton)) {
            tickMultiplierField.setFocused(true);
        } else {
            tickMultiplierField.setFocused(false);
        }

        buttonList.forEach(guiButton -> {
            if(guiButton.isMouseOver()) {
                handleButtonClick(guiButton);
            }
        });
    }

    private void handleButtonClick(GuiButton guiButton) {
        int alterNum = 1;
        if(KeyboardHelper.isHoldingShift()) {
            alterNum = 5;
        } else if(KeyboardHelper.isHoldingCTRL()) {
            alterNum = 20;
        }

        if(guiButton.id == tickDecreaseButton.id) {
            decreaseTickMultiplier(alterNum);
        } else if(guiButton.id == tickIncreaseButton.id) {
            increaseTickMultiplier(alterNum);
        } else if(guiButton.id == toggleHighlightingButton.id) {
            SimplyAcceleration.network.sendToServer(new ToggleAcceleratorHighlighting());
        } else if(guiButton.id == clearLinkedButton.id) {
            SimplyAcceleration.network.sendToServer(new ClearAllLinkedMessage());
        }
    }

    private void decreaseTickMultiplier(int alterNum) {
        tickMultiplierField.setText("" + (MathHelper.getIntFromStringSafe(tickMultiplierField.getText()) - alterNum));
    }

    private void increaseTickMultiplier(int alterNum) {
        // ex 990 + 20 = 1010 but the text field would trim it to 101
        if((MathHelper.getIntFromStringSafe(tickMultiplierField.getText()) + alterNum) > 999) {
            tickMultiplierField.setText("" + 999);
        } else {
            tickMultiplierField.setText("" + (MathHelper.getIntFromStringSafe(tickMultiplierField.getText()) + alterNum));
        }
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String inputTitle = "Speed Multiplier";
        fontRenderer.drawString(inputTitle, xSize / 2 - fontRenderer.getStringWidth(inputTitle) / 2, 14, 0x404040);

        int x = mouseX - guiLeft;
        int y = mouseY - guiTop;

        List<String> tooltip = new ArrayList<>();
        buttonList.forEach(guiButton -> {
            if(guiButton.isMouseOver() && (guiButton.id == 0 || guiButton.id == 1)) {
                tooltip.add("Hold §eSHIFT§r to modify by 5");
                tooltip.add("Hold §e§oCTRL§r to modify by 20");
                drawHoveringText(tooltip, x - 32, y + 28);
            }
        });
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int left = getGuiLeft();
        int top = getGuiTop();

        // Draw player inventory
        Minecraft.getMinecraft().getTextureManager().bindTexture(defaultGui);
        drawTexturedModalRect(left, top , 0, 0, 176, 166);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
        tickMultiplierField.drawTextBox();
        buttonList.forEach(guiButton -> guiButton.drawButton(mc, mouseX, mouseY, partialTicks));
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }
}
