package xt9.simplyacceleration.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import xt9.simplyacceleration.SimplyAcceleration;
import xt9.simplyacceleration.SimplyConfig;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.capabilities.Energy;
import xt9.simplyacceleration.common.utils.NBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xt9 on 2018-05-26.
 */
public class TileEntityAccelerator extends TileEntity implements ITickable, IGuiTile {
    private Energy energyCap = new Energy(SimplyConfig.getAcceleratorEnergyConsumption(), SimplyConfig.getAcceleratorEnergyConsumption() , 0, 0);
    private int tickMultiplier = 1;
    private NonNullList<Long> longPositions = NonNullList.create();
    private boolean isHighlighting = false;

    @Override
    public void update() {
        if(!world.isRemote) {
            NonNullList<ITickable> linkedMachines = getValidAndUpdate();

            if(energyCap.getEnergyStored() >= SimplyConfig.getAcceleratorEnergyConsumption()) {
                energyCap.voidEnergy(SimplyConfig.getAcceleratorEnergyConsumption());
                linkedMachines.forEach(machine -> {
                    for (int i = 1; i < tickMultiplier; i++) {
                        machine.update();
                    }
                });
            }

        } else {
            if(isHighlighting) {
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                longPositions.forEach(l -> {
                    BlockPos pos = BlockPos.fromLong(l);
                    SimplyAcceleration.proxy.spawnParticle(
                        world,
                        pos.getX() + 0.5D + rand.nextDouble(-0.33D, 0.33D),
                        pos.getY() + 1.2D,
                        pos.getZ() + 0.5D + rand.nextDouble(-0.33D, 0.33D),
                        rand.nextDouble(-0.02D, 0.02D),
                        0,
                        rand.nextDouble(-0.02D, 0.02D)
                    );
                });
            }
        }
    }

    private NonNullList<ITickable> getValidAndUpdate() {
        NonNullList<ITickable> result = NonNullList.create();
        for (int i = 0; i < longPositions.size(); i++) {
            TileEntity tile = world.getTileEntity(BlockPos.fromLong(longPositions.get(i)));
            if(tile instanceof ITickable && !(tile instanceof TileEntityAccelerator)) {
                result.add((ITickable) tile);
            } else {
                longPositions.remove(i);
                updateTile();
            }
        }
        return result;
    }

    public boolean isUniquePosition(long pos) {
        for (long l : longPositions) {
            if(l == pos) {
                return false;
            }
        }
        return true;
    }

    public void addMachinePosition(TileEntity tile) {
        if(tile instanceof ITickable && !(tile instanceof TileEntityAccelerator)) {
            if(isUniquePosition(tile.getPos().toLong())) {
                longPositions.add(tile.getPos().toLong());
                updateTile();
            }
        }
    }

    public void removeMachinePosition(long pos) {
        for (int i = 0; i < longPositions.size(); i++) {
            if(longPositions.get(i) == pos) {
                longPositions.remove(i);
                updateTile();
            }
        }
    }

    public boolean canLinkMachine() {
        return longPositions.size() < SimplyConfig.getMaxLinkedMachines();
    }

    public void clearLinked() {
        longPositions.clear();
        updateTile();
    }

    public NonNullList<Long> getLongPositions() {
        return longPositions;
    }

    public void toggleHighlighting() {
        isHighlighting = !isHighlighting;
        updateTile();
    }

    public boolean isHighlighting() {
        return isHighlighting;
    }


    public void setTickMultiplier(int tickMultiplier) {
        this.tickMultiplier = tickMultiplier;
        updateTile();
    }

    public int getTickMultiplier() {
        return tickMultiplier;
    }

    private void updateTile() {
        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);
        markDirty();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 3, writeToNBT(new NBTTagCompound()));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("tickMultiplier", tickMultiplier);
        compound.setBoolean("isHighlighting", isHighlighting);
        energyCap.writeEnergy(compound);
        compound.setTag("longs", NBTHelper.getLongTagList(longPositions));
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        tickMultiplier = compound.hasKey("tickMultiplier", Constants.NBT.TAG_INT) ? compound.getInteger("tickMultiplier") : 1;
        isHighlighting = compound.hasKey("isHighlighting", Constants.NBT.TAG_BYTE) ? compound.getBoolean("isHighlighting") : isHighlighting;
        energyCap.readEnergy(compound);
        longPositions = NBTHelper.getLongListFromTag(compound, "longs");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(energyCap) : super.getCapability(capability, facing);
    }

    @Override
    public int getGuiID() {
        return SimplyConstants.ACCELERATOR_GUI_ID;
    }

}
