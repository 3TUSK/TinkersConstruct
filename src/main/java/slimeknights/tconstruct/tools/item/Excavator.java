package slimeknights.tconstruct.tools.item;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.ToolMaterialStats;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

public class Excavator extends Shovel {

  public Excavator() {
    super(new PartMaterialType.ToolPartType(TinkerTools.toughToolRod),
          new PartMaterialType.ToolPartType(TinkerTools.excavatorHead),
          new PartMaterialType.ToolPartType(TinkerTools.largePlate),
          new PartMaterialType.ToolPartType(TinkerTools.toughBinding));
  }

  @Override
  public float miningSpeedModifier() {
    return 0.35f; // a bit faster than hammers to make terraforming easier
  }

  @Override
  public float damagePotential() {
    return 0.3f;
  }

  @Override
  public ImmutableList<BlockPos> getAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin) {
    return ToolHelper.calcAOEBlocks(stack, world, player, origin, 3, 3, 1);
  }

  @Override
  public NBTTagCompound buildTag(List<Material> materials) {
    ToolMaterialStats handle = materials.get(0).getStats(ToolMaterialStats.TYPE);
    ToolMaterialStats head = materials.get(1).getStats(ToolMaterialStats.TYPE);
    ToolMaterialStats plate = materials.get(2).getStats(ToolMaterialStats.TYPE);
    ToolMaterialStats binding = materials.get(3).getStats(ToolMaterialStats.TYPE);

    ToolNBT data = new ToolNBT(head);
    data.handle(handle).extra(plate, binding);

    data.durability *= 1f + 0.05f * (binding.extraQuality - 0.5f);
    data.speed *= 1f + 0.3f * (handle.handleQuality * handle.miningspeed);
    data.attack = head.attack/2f + plate.attack/3f;
    data.attack *= 1f + 0.1f * handle.handleQuality * binding.extraQuality;

/*
    data.durability += ((0.5f + 0.5f * plate.extraQuality) * plate.durability) * (0.4f + 0.6f * binding.extraQuality);
    data.durability += 0.3f * handle.durability + 0.1f * binding.durability;
    data.durability *= 0.8f + 0.4f * handle.handleQuality;

    data.speed *= 0.3f + 0.4f * handle.handleQuality;
    data.speed += 0.3f * handle.miningspeed * binding.extraQuality;
    data.speed += 0.1f * plate.miningspeed;

    data.attack *= 1.2f;
    data.attack += (0.2f + 0.3f*plate.extraQuality) * plate.attack;
    data.attack += 0.15f * binding.attack;
*/
    // 3 free modifiers
    data.modifiers = DEFAULT_MODIFIERS;

    return data.get();
  }
}
