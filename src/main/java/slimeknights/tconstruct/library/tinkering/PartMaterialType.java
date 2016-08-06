package slimeknights.tconstruct.library.tinkering;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.traits.ITrait;

public class PartMaterialType {

  // ANY of these has to match
  private final Set<IToolPart> neededPart = new HashSet<IToolPart>();
  // ALL of the material stats have to be there
  private final String[] neededTypes;

  public PartMaterialType(IToolPart part, String... statIDs) {
    neededPart.add(part);
    neededTypes = statIDs;
  }

  public boolean isValid(ItemStack stack) {
    if(stack == null || stack.getItem() == null) {
      return false;
    }

    if(!(stack.getItem() instanceof IToolPart)) {
      return false;
    }

    IToolPart toolPart = (IToolPart) stack.getItem();
    return isValid(toolPart, toolPart.getMaterial(stack));
  }

  public boolean isValid(IToolPart part, Material material) {
    return isValidItem(part) && isValidMaterial(material);
  }

  public boolean isValidItem(IToolPart part) {
    return neededPart.contains(part);
  }

  public boolean isValidMaterial(Material material) {
    for(String type : neededTypes) {
      if(!material.hasStats(type)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true if the passed stat is used by this.</br>
   * This does NOT mean that a material having this stat is usable, since multiple stats might be required!
   */
  public boolean usesStat(String statID) {
    for(String type : neededTypes) {
      if(type.equals(statID)) {
        return true;
      }
    }

    return false;
  }

  public Collection<ITrait> getApplicableTraitsForMaterial(Material material) {
    if(!isValidMaterial(material)) {
      return ImmutableList.of();
    }

    ImmutableList.Builder<ITrait> traits = ImmutableList.builder();
    // traits of the types used
    for(String type : neededTypes) {
      traits.addAll(material.getAllTraitsForStats(type));
    }

    // use default trait if none is present
    if(traits.build().isEmpty()) {
      traits.addAll(material.getDefaultTraits());
    }

    return traits.build();
  }

  public Set<IToolPart> getPossibleParts() {
    return ImmutableSet.copyOf(neededPart);
  }

  public static PartMaterialType head(IToolPart part) {
    return new PartMaterialType(part, HeadMaterialStats.TYPE);
  }

  public static PartMaterialType handle(IToolPart part) {
    return new PartMaterialType(part, HandleMaterialStats.TYPE);
  }

  public static PartMaterialType extra(IToolPart part) {
    return new PartMaterialType(part, ExtraMaterialStats.TYPE);
  }

  public static PartMaterialType bow(IToolPart part) {
    return new PartMaterialType(part, BowMaterialStats.TYPE, HeadMaterialStats.TYPE);
  }

  public static PartMaterialType bowstring(IToolPart part) {
    return new PartMaterialType(part, BowStringMaterialStats.TYPE);
  }
}
