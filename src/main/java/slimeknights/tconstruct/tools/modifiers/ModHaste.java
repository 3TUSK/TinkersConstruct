package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;

public class ModHaste extends Modifier {

  private final int max;

  public ModHaste(int max) {
    super("haste");

    this.max = max;

    addAspects(new ModifierAspect.MultiAspect(this, 0x910000, 5, max, 1), ModifierAspect.harvestOnly);
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);

    ToolNBT toolData = TagUtil.getOriginalToolStats(rootCompound);
    float speed = toolData.speed;
    int level = data.current / max;
    for(int count = data.current; count > 0; count--) {
      if(speed <= 10f) {
        // linear scaling from 0.08 to 0.06 per piece till 10 miningspeed
        speed += 0.15f - 0.05f * speed / 10f;
      }
      else if(speed <= 20f) {
        speed += 0.1f - 0.05 * speed / 20f;
      }
      else {
        speed += 0.05;
      }
    }

    // each full level gives a flat 0.5 bonus, not influenced by dimishing returns
    speed += level * 0.5f;

    // save it to the tool
    NBTTagCompound tag = TagUtil.getToolTag(rootCompound);
    speed -= toolData.speed;
    speed += tag.getFloat(Tags.MININGSPEED);
    tag.setFloat(Tags.MININGSPEED, speed);
  }

  @Override
  public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
    return getLeveledTooltip(modifierTag, detailed);
  }
}
