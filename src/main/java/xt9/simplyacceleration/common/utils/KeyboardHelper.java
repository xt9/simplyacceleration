package xt9.simplyacceleration.common.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by xt9 on 2018-05-28.
 */
public class KeyboardHelper {
    @SideOnly(Side.CLIENT)
    public static boolean isHoldingShift() {
        return Keyboard.isCreated() && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isHoldingCTRL() {
        return Keyboard.isCreated() && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL));
    }
}
