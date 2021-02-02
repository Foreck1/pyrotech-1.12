package com.codetaylor.mc.pyrotech.modules.hunting.item;

import com.codetaylor.mc.pyrotech.modules.core.network.SCPacketParticleProgress;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHunting;
import com.codetaylor.mc.pyrotech.modules.hunting.ModuleHuntingConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;

public class EntityItemHideScraped
    extends EntityItem {

  public static final String NAME = "pyrotech." + EntityItemHideScraped.class.getSimpleName();

  private int ticksInWater;
  private ItemStack transformedItem;

  @SuppressWarnings("unused")
  public EntityItemHideScraped(World world) {

    // serialization
    super(world);
    this.setNoDespawn();
  }

  public EntityItemHideScraped(World world, double x, double y, double z, ItemStack stack, ItemStack transformedItem) {

    super(world, x, y, z, stack);
    this.setNoDespawn();
    this.transformedItem = transformedItem;
  }

  @Override
  public void onEntityUpdate() {

    super.onEntityUpdate();

    if (this.world == null) {
      return;
    }

    if (this.transformedItem == null) {
      return;
    }

    if (this.getItem().getItem() == this.transformedItem.getItem()) {
      return;
    }

    if (!this.world.isRemote) {
      BlockPos pos = this.getPosition();
      IBlockState blockState = this.world.getBlockState(pos);
      Block block = blockState.getBlock();

      if ((block instanceof BlockFluidBase
          && ((BlockFluidBase) block).getFluid() == FluidRegistry.WATER)
          || block == Blocks.WATER) {

        this.ticksInWater += 1;

        if (this.ticksInWater % 20 == 0) {
          ModuleHunting.PACKET_SERVICE.sendToAllAround(
              new SCPacketParticleProgress(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 2),
              this.world.provider.getDimension(),
              pos
          );
        }
      }

      if (this.ticksInWater >= ModuleHuntingConfig.IN_WORLD_HIDE_SOAK_TICKS) {
        ItemStack copy = this.transformedItem.copy();
        copy.setCount(this.getItem().getCount());
        this.setItem(copy);
      }
    }
  }

  @Override
  public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    super.writeEntityToNBT(compound);
    compound.setInteger("ticksInWater", this.ticksInWater);
    compound.setTag("transformedItem", this.transformedItem.serializeNBT());
  }

  @Override
  public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    super.readEntityFromNBT(compound);
    this.ticksInWater = compound.getInteger("ticksInWater");
    this.transformedItem = new ItemStack(compound.getCompoundTag("transformedItem"));
  }
}