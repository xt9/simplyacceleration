package xt9.simplyacceleration.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xt9.simplyacceleration.SimplyAcceleration;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

import javax.annotation.Nullable;

/**
 * Created by xt9 on 2018-05-26.
 */
public class BlockAccelerator extends BlockBase  implements ITileEntityProvider {
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockAccelerator() {
        super("accelerator", Material.ROCK);
        setHardness(3f);
        setResistance(5.0f);
        setLightLevel(0.5F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityAccelerator tile = getTileEntity(world, pos);

        if(!world.isRemote) {
            if(!player.isSneaking()) {
                world.notifyBlockUpdate(pos, state, state, 3);
                player.openGui(SimplyAcceleration.instance, tile.getGuiID(), player.world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    private TileEntityAccelerator getTileEntity(World world, BlockPos pos) {
        return (TileEntityAccelerator) world.getTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAccelerator();
    }
}
