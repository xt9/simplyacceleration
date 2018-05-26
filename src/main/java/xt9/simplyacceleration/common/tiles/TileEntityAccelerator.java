package xt9.simplyacceleration.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import xt9.simplyacceleration.SimplyConstants;

/**
 * Created by xt9 on 2018-05-26.
 */
public class TileEntityAccelerator extends TileEntity implements ITickable, IGuiTile {
    @Override
    public void update() {

    }

    @Override
    public int getGuiID() {
        return SimplyConstants.ACCELERATOR_GUI_ID;
    }
}
