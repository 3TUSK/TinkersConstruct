package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitTasty extends AbstractTrait {

  public static final int NOM_COST = 100;

  public TraitTasty() {
    super("tasty", TextFormatting.RED);
  }

  @Override
  public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
    // needs to be in hand to be eaten!
    if(!isSelected || !(entity instanceof EntityPlayer)) {
      return;
    }

    FoodStats foodStats = ((EntityPlayer) entity).getFoodStats();
    // we only eat our tools if the food level is half empty
    if(foodStats.getFoodLevel() > 10) {
      return;
    }
    // more than 3 chickenwings left? we only take a bite randomly
    else if(foodStats.getFoodLevel() > 6) {
      // on average we take a bite every 25 seconds (1/(25s * 20 ticks))
      if(random.nextFloat() < 0.002f) {
        nom(tool, (EntityPlayer) entity);
      }
    }
    // less than 3 chickens left? we take a bite out before the situation becomes too.. dire(wolf20)
    else {
      float chance = 0f;
      chance += (5 - foodStats.getFoodLevel()) * 0.0025f;
      chance -= foodStats.getSaturationLevel() * 0.005f;

      if(random.nextFloat() < chance) {
        nom(tool, (EntityPlayer) entity);
      }
    }
  }

  protected void nom(ItemStack tool, EntityPlayer player) {
    if(ToolHelper.isBroken(tool) || ToolHelper.getCurrentDurability(tool) < NOM_COST) {
      return;
    }

    player.getFoodStats().addStats(1, 0);
    player.worldObj.playSound(null, player.getPosition(), SoundEvents.entity_generic_eat, SoundCategory.PLAYERS, 0.8f, 1.0f);
    ToolHelper.damageTool(tool, NOM_COST, player);
  }
}
