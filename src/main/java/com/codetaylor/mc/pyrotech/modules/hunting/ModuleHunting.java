package com.codetaylor.mc.pyrotech.modules.hunting;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.hunting.block.BlockCarcass;
import com.codetaylor.mc.pyrotech.modules.hunting.init.BlockInitializer;
import com.codetaylor.mc.pyrotech.modules.hunting.init.ItemInitializer;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemHide;
import com.codetaylor.mc.pyrotech.modules.hunting.item.ItemPelt;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleHunting
    extends ModuleBase {

  public static final String MODULE_ID = "module.hunting";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;
  public static final Logger LOGGER = LogManager.getLogger(ModuleHunting.class);

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleHunting() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onRegister(Registry registry) {

    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    BlockInitializer.onClientRegister(registry);
    ItemInitializer.onClientRegister(registry);
  }

  @GameRegistry.ObjectHolder(ModuleHunting.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockCarcass.NAME)
    public static final BlockCarcass CARCASS;

    static {
      CARCASS = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleHunting.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemHide.NAME_PIG)
    public static final ItemHide HIDE_PIG;

    @GameRegistry.ObjectHolder(ItemHide.NAME_SHEEP_SHEARED)
    public static final ItemHide HIDE_SHEEP_SHEARED;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_COW)
    public static final ItemPelt PELT_COW;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_MOOSHROOM)
    public static final ItemPelt PELT_MOOSHROOM;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_POLAR_BEAR)
    public static final ItemPelt PELT_POLAR_BEAR;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_BAT)
    public static final ItemPelt PELT_BAT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_HORSE)
    public static final ItemPelt PELT_HORSE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_YELLOW)
    public static final ItemPelt PELT_SHEEP_YELLOW;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_WHITE)
    public static final ItemPelt PELT_SHEEP_WHITE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_GRAY_LIGHT)
    public static final ItemPelt PELT_SHEEP_GRAY_LIGHT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_RED)
    public static final ItemPelt PELT_SHEEP_RED;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_PURPLE)
    public static final ItemPelt PELT_SHEEP_PURPLE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_PINK)
    public static final ItemPelt PELT_SHEEP_PINK;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_ORANGE)
    public static final ItemPelt PELT_SHEEP_ORANGE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_MAGENTA)
    public static final ItemPelt PELT_SHEEP_MAGENTA;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_LIME)
    public static final ItemPelt PELT_SHEEP_LIME;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BLUE_LIGHT)
    public static final ItemPelt PELT_SHEEP_BLUE_LIGHT;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_GREEN)
    public static final ItemPelt PELT_SHEEP_GREEN;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_GRAY)
    public static final ItemPelt PELT_SHEEP_GRAY;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_CYAN)
    public static final ItemPelt PELT_SHEEP_CYAN;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BROWN)
    public static final ItemPelt PELT_SHEEP_BROWN;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BLUE)
    public static final ItemPelt PELT_SHEEP_BLUE;

    @GameRegistry.ObjectHolder(ItemPelt.NAME_SHEEP_BLACK)
    public static final ItemPelt PELT_SHEEP_BLACK;

    static {
      HIDE_PIG = null;
      HIDE_SHEEP_SHEARED = null;
      PELT_COW = null;
      PELT_MOOSHROOM = null;
      PELT_POLAR_BEAR = null;
      PELT_BAT = null;
      PELT_HORSE = null;
      PELT_SHEEP_YELLOW = null;
      PELT_SHEEP_WHITE = null;
      PELT_SHEEP_GRAY_LIGHT = null;
      PELT_SHEEP_RED = null;
      PELT_SHEEP_PURPLE = null;
      PELT_SHEEP_PINK = null;
      PELT_SHEEP_ORANGE = null;
      PELT_SHEEP_MAGENTA = null;
      PELT_SHEEP_LIME = null;
      PELT_SHEEP_BLUE_LIGHT = null;
      PELT_SHEEP_GREEN = null;
      PELT_SHEEP_GRAY = null;
      PELT_SHEEP_CYAN = null;
      PELT_SHEEP_BROWN = null;
      PELT_SHEEP_BLUE = null;
      PELT_SHEEP_BLACK = null;
    }
  }

}