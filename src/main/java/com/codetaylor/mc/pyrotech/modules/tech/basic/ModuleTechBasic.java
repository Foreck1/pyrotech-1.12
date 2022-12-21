package com.codetaylor.mc.pyrotech.modules.tech.basic;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.network.tile.ITileDataService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.pyrotech.ModPyrotech;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.FocusedPlayerData;
import com.codetaylor.mc.pyrotech.modules.tech.basic.capability.IFocusedPlayerData;
import com.codetaylor.mc.pyrotech.modules.tech.basic.event.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.init.recipe.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.item.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.potion.*;
import com.codetaylor.mc.pyrotech.modules.tech.basic.recipe.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleTechBasic
    extends ModuleBase {

  public static final String MODULE_ID = "module.tech.basic";
  public static final String MOD_ID = ModPyrotech.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModPyrotech.CREATIVE_TAB;

  public static final Logger LOGGER = LogManager.getLogger(MOD_ID + "." + ModuleTechBasic.class.getSimpleName());

  public static IPacketService PACKET_SERVICE;
  public static ITileDataService TILE_DATA_SERVICE;

  public ModuleTechBasic() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    PACKET_SERVICE = this.enableNetwork();
    TILE_DATA_SERVICE = this.enableNetworkTileDataService(PACKET_SERVICE);

    MinecraftForge.EVENT_BUS.register(this);

    String[] craftTweakerPlugins = {
        "ZenKilnPit",
        "ZenCrudeDryingRack",
        "ZenDryingRack",
        "ZenChoppingBlock",
        "ZenAnvilGranite",
        "ZenAnvilIronclad",
        "ZenAnvilObsidian",
        "ZenCompactingBin",
        "ZenCampfire",
        "ZenWorktable",
        "ZenSoakingPot",
        "ZenCompostBin",
        "ZenBarrel",
        "ZenTanningRack"
    };

    for (String plugin : craftTweakerPlugins) {
      this.registerIntegrationPlugin(
          "crafttweaker",
          "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.crafttweaker." + plugin
      );
    }

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.jei.PluginJEI"
    );

    this.registerIntegrationPlugin(
        "gamestages",
        "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.gamestages.PluginGameStages"
    );
  }

  @SubscribeEvent
  public void onNewRegistryEvent(RegistryEvent.NewRegistry event) {

    RegistryInitializer.createRegistries(event);
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    FMLInterModComms.sendFunctionMessage(
        "theoneprobe",
        "getTheOneProbe",
        "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.top.PluginTOP$Callback"
    );

    if (ModuleTechBasicConfig.WORKTABLE_COMMON.ALLOW_RECIPE_REPEAT) {
      MinecraftForge.EVENT_BUS.register(new RecipeRepeat.RightClickBlockEventHandler());
    }

    CapabilityManager.INSTANCE.register(IFocusedPlayerData.class, new FocusedPlayerData(), FocusedPlayerData::new);

    MinecraftForge.EVENT_BUS.register(new CampfireEffectTracker());
    MinecraftForge.EVENT_BUS.register(new CampfireComfortEffectEventHandler());
    MinecraftForge.EVENT_BUS.register(new CampfireRestingEffectEventHandler());
    MinecraftForge.EVENT_BUS.register(new CampfireWellFedEffectEventHandler());
    MinecraftForge.EVENT_BUS.register(new CampfireFocusEffectEventHandler());
    MinecraftForge.EVENT_BUS.register(new CampfireEffectDurationFix());
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onClientPreInitializationEvent(event);

    FMLInterModComms.sendMessage(
        "waila",
        "register",
        "com.codetaylor.mc.pyrotech.modules.tech.basic.plugin.waila.PluginWaila.wailaCallback"
    );

    if (ModuleTechBasicConfig.COMPOST_BIN.SHOW_COMPOST_VALUE_IN_TOOLTIPS) {
      MinecraftForge.EVENT_BUS.register(new TooltipEventHandler.CompostValue());
    }
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    PacketInitializer.register(registry);
  }

  @Override
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    super.onRegisterRecipesEvent(event);

    // blacklist campfire config recipes
    CampfireRecipe.registerConfigBlacklist();

    PitKilnRecipesAdd.apply(ModuleTechBasic.Registries.KILN_PIT_RECIPE);
    CrudeDryingRackRecipesAdd.apply(ModuleTechBasic.Registries.CRUDE_DRYING_RACK_RECIPE);
    DryingRackRecipesAdd.apply(ModuleTechBasic.Registries.DRYING_RACK_RECIPE);
    AnvilGraniteRecipesAdd.apply(ModuleTechBasic.Registries.ANVIL_RECIPE);
    AnvilIroncladRecipesAdd.apply(ModuleTechBasic.Registries.ANVIL_RECIPE);
    AnvilObsidianRecipesAdd.apply(ModuleTechBasic.Registries.ANVIL_RECIPE);
    ChoppingBlockRecipesAdd.applyCompatRecipes(this.getConfigurationDirectory().toPath(), ModuleTechBasic.Registries.CHOPPING_BLOCK_RECIPE);
    CompactingBinRecipesAdd.apply(ModuleTechBasic.Registries.COMPACTING_BIN_RECIPE);
    SoakingPotRecipesAdd.apply(ModuleTechBasic.Registries.SOAKING_POT_RECIPE);
    CampfireRecipesAdd.apply(ModuleTechBasic.Registries.CAMPFIRE_RECIPE);
    CompostBinRecipesAdd.apply(ModuleTechBasic.Registries.COMPOST_BIN_RECIPE);
    BarrelRecipesAdd.apply(ModuleTechBasic.Registries.BARREL_RECIPE);
    TanningRackRecipesAdd.apply(ModuleTechBasic.Registries.TANNING_RACK_RECIPE);

    DryingRackRecipesAdd.registerInheritedRecipes(Registries.CRUDE_DRYING_RACK_RECIPE, Registries.DRYING_RACK_RECIPE);
    AnvilIroncladRecipesAdd.registerInheritedRecipes(Registries.ANVIL_RECIPE);
    AnvilObsidianRecipesAdd.registerInheritedRecipes(Registries.ANVIL_RECIPE);
  }

  @Override
  public void onRegister(Registry registry) {

    BlockInitializer.onRegister(registry);
    ItemInitializer.onRegister(registry);
    PotionInitializer.onRegister(registry);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    BlockInitializer.onClientRegister(registry);
    ItemInitializer.onClientRegister(registry);
  }

  @Override
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super.onPostInitializationEvent(event);
  }

  @GameRegistry.ObjectHolder(ModuleTechBasic.MOD_ID)
  public static class Potions {

    @GameRegistry.ObjectHolder(PotionComfort.NAME)
    public static final PotionComfort COMFORT;

    @GameRegistry.ObjectHolder(PotionResting.NAME)
    public static final PotionResting RESTING;

    @GameRegistry.ObjectHolder(PotionWellFed.NAME)
    public static final PotionWellFed WELL_FED;

    @GameRegistry.ObjectHolder(PotionWellRested.NAME)
    public static final PotionWellRested WELL_RESTED;

    @GameRegistry.ObjectHolder(PotionFocused.NAME)
    public static final PotionFocused FOCUSED;

    static {
      COMFORT = null;
      RESTING = null;
      WELL_FED = null;
      WELL_RESTED = null;
      FOCUSED = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechBasic.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockKilnPit.NAME)
    public static final BlockKilnPit KILN_PIT;

    @GameRegistry.ObjectHolder(BlockCampfire.NAME)
    public static final BlockCampfire CAMPFIRE;

    @GameRegistry.ObjectHolder(BlockDryingRack.NAME)
    public static final BlockDryingRack DRYING_RACK;

    @GameRegistry.ObjectHolder(BlockChoppingBlock.NAME)
    public static final BlockChoppingBlock CHOPPING_BLOCK;

    @GameRegistry.ObjectHolder(BlockAnvilGranite.NAME)
    public static final BlockAnvilGranite ANVIL_GRANITE;

    @GameRegistry.ObjectHolder(BlockAnvilIronPlated.NAME)
    public static final BlockAnvilIronPlated ANVIL_IRON_PLATED;

    @GameRegistry.ObjectHolder(BlockAnvilObsidian.NAME)
    public static final BlockAnvilObsidian ANVIL_OBSIDIAN;

    @GameRegistry.ObjectHolder(BlockWorktable.NAME)
    public static final BlockWorktable WORKTABLE;

    @GameRegistry.ObjectHolder(BlockWorktableStone.NAME)
    public static final BlockWorktableStone WORKTABLE_STONE;

    @GameRegistry.ObjectHolder(BlockCompactingBin.NAME)
    public static final BlockCompactingBin COMPACTING_BIN;

    @GameRegistry.ObjectHolder(BlockSoakingPot.NAME)
    public static final BlockSoakingPot SOAKING_POT;

    @GameRegistry.ObjectHolder(BlockCompostBin.NAME)
    public static final BlockCompostBin COMPOST_BIN;

    @GameRegistry.ObjectHolder(BlockBarrel.NAME)
    public static final BlockBarrel BARREL;

    @GameRegistry.ObjectHolder(BlockBarrel.NAME_SEALED)
    public static final BlockBarrel BARREL_SEALED;

    @GameRegistry.ObjectHolder(BlockTanningRack.NAME)
    public static final BlockTanningRack TANNING_RACK;

    static {
      KILN_PIT = null;
      CAMPFIRE = null;
      DRYING_RACK = null;
      CHOPPING_BLOCK = null;
      ANVIL_GRANITE = null;
      ANVIL_IRON_PLATED = null;
      ANVIL_OBSIDIAN = null;
      WORKTABLE = null;
      WORKTABLE_STONE = null;
      COMPACTING_BIN = null;
      SOAKING_POT = null;
      COMPOST_BIN = null;
      BARREL = null;
      BARREL_SEALED = null;
      TANNING_RACK = null;
    }
  }

  @GameRegistry.ObjectHolder(ModuleTechBasic.MOD_ID)
  public static class Items {

    @GameRegistry.ObjectHolder(ItemTinder.NAME)
    public static final ItemTinder TINDER;

    @GameRegistry.ObjectHolder(ItemMarshmallow.NAME)
    public static final ItemMarshmallow MARSHMALLOW;

    @GameRegistry.ObjectHolder(ItemMarshmallowRoasted.NAME)
    public static final ItemMarshmallow MARSHMALLOW_ROASTED;

    @GameRegistry.ObjectHolder(ItemMarshmallowBurned.NAME)
    public static final ItemMarshmallow MARSHMALLOW_BURNED;

    @GameRegistry.ObjectHolder(ItemMarshmallowStickEmpty.NAME)
    public static final ItemMarshmallowStickEmpty MARSHMALLOW_STICK_EMPTY;

    @GameRegistry.ObjectHolder(ItemMarshmallowStick.NAME)
    public static final ItemMarshmallowStick MARSHMALLOW_STICK;

    @GameRegistry.ObjectHolder(ItemBarrelLid.NAME)
    public static final ItemBarrelLid BARREL_LID;

    static {
      TINDER = null;
      MARSHMALLOW = null;
      MARSHMALLOW_ROASTED = null;
      MARSHMALLOW_BURNED = null;
      MARSHMALLOW_STICK_EMPTY = null;
      MARSHMALLOW_STICK = null;
      BARREL_LID = null;
    }
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<KilnPitRecipe> KILN_PIT_RECIPE;
    public static final IForgeRegistryModifiable<CrudeDryingRackRecipe> CRUDE_DRYING_RACK_RECIPE;
    public static final IForgeRegistryModifiable<DryingRackRecipe> DRYING_RACK_RECIPE;
    public static final IForgeRegistryModifiable<ChoppingBlockRecipe> CHOPPING_BLOCK_RECIPE;
    public static final IForgeRegistryModifiable<AnvilRecipe> ANVIL_RECIPE;
    public static final IForgeRegistryModifiable<CompactingBinRecipe> COMPACTING_BIN_RECIPE;
    public static final IForgeRegistryModifiable<CompostBinRecipe> COMPOST_BIN_RECIPE;
    public static final IForgeRegistryModifiable<CampfireRecipe> CAMPFIRE_RECIPE;
    public static final IForgeRegistryModifiable<WorktableRecipe> WORKTABLE_RECIPE;
    public static final IForgeRegistryModifiable<SoakingPotRecipe> SOAKING_POT_RECIPE;
    public static final IForgeRegistryModifiable<BarrelRecipe> BARREL_RECIPE;
    public static final IForgeRegistryModifiable<TanningRackRecipe> TANNING_RACK_RECIPE;

    static {
      KILN_PIT_RECIPE = null;
      CRUDE_DRYING_RACK_RECIPE = null;
      DRYING_RACK_RECIPE = null;
      CHOPPING_BLOCK_RECIPE = null;
      ANVIL_RECIPE = null;
      COMPACTING_BIN_RECIPE = null;
      COMPOST_BIN_RECIPE = null;
      CAMPFIRE_RECIPE = null;
      WORKTABLE_RECIPE = null;
      SOAKING_POT_RECIPE = null;
      BARREL_RECIPE = null;
      TANNING_RACK_RECIPE = null;
    }
  }
}
