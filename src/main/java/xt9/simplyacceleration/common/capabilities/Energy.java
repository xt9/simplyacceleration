package xt9.simplyacceleration.common.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.EnergyStorage;

/**
 * Created by xt9 on 2018-06-03.
 */
public class Energy extends EnergyStorage {
    public Energy(int capacity, int maxIn, int maxOut, int energy) {
        super(capacity, maxIn, maxOut, energy);
    }

    public void voidEnergy(int energy) {
        this.energy = getEnergyStored() - energy;
    }

    public NBTTagCompound writeEnergy(NBTTagCompound compound) {
        compound.setInteger("energy", getEnergyStored());
        return compound;
    }

    public void readEnergy(NBTTagCompound compound) {
        this.energy = compound.hasKey("energy", Constants.NBT.TAG_INT) ? compound.getInteger("energy") : 0;
    }
}
