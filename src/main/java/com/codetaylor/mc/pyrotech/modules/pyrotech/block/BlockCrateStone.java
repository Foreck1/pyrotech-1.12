package com.codetaylor.mc.pyrotech.modules.pyrotech.block;

import com.codetaylor.mc.pyrotech.modules.pyrotech.tile.TileCrateStone;
import net.minecraft.tileentity.TileEntity;

public class BlockCrateStone
    extends BlockCrateBase {

  public static final String NAME = "crate_stone";

  public BlockCrateStone() {

    super(1.5f, 10.0f);
  }

  @Override
  protected TileEntity createTileEntity() {

    return new TileCrateStone();
  }
}