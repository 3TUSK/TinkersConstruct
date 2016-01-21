package slimeknights.tconstruct;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.tools.TinkerMaterials;

// Takes care of adding all the generic-ish materials
@Pulse(id = TinkerIntegration.PulseId, forced = true)
public class TinkerIntegration extends TinkerPulse {

  public static final String PulseId = "TinkerIntegration";
  public static List<MaterialIntegration> integrationList = Lists.newLinkedList();

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    integrate(TinkerMaterials.wood);
    integrate(TinkerMaterials.stone);
    integrate(TinkerMaterials.flint);
    integrate(TinkerMaterials.cactus);
    integrate(TinkerMaterials.bone);
    integrate(TinkerMaterials.obsidian, TinkerFluids.obsidian);
    integrate(TinkerMaterials.prismarine);
    integrate(TinkerMaterials.endstone);
    integrate(TinkerMaterials.sponge);

    integrate(TinkerMaterials.knightslime, TinkerFluids.knightslime, "Knightslime");
    integrate(TinkerMaterials.slime, "slimecrystalGreen");
    integrate(TinkerMaterials.blueslime, "slimecrystalBlue");

    integrate(TinkerMaterials.iron, TinkerFluids.iron, "Iron");
    integrate(TinkerMaterials.pigiron, TinkerFluids.pigIron, "Pigiron");
    //integrate(TinkerMaterials.cobalt, TinkerFluids.copper, "Copper");
    //integrate(TinkerMaterials.bronze, TinkerFluids.bronze, "Bronze");


    integrate(TinkerMaterials.netherrack);
    integrate(TinkerMaterials.cobalt, TinkerFluids.cobalt, "Cobalt");
    integrate(TinkerMaterials.ardite, TinkerFluids.ardite, "Ardite");
    integrate(TinkerMaterials.manyullyn, TinkerFluids.manyullyn, "Manyullyn");

    // non-toolmaterial integration
    integrate(null, TinkerFluids.brass, "Brass");

    for(MaterialIntegration integration : integrationList) {
      integration.integrate();
    }
  }

  @Subscribe
  public void init(FMLInitializationEvent event) {
    for(MaterialIntegration integration : integrationList) {
      integration.integrateRecipes();
    }
  }

  public static void integrate(Material material) {
    integrationList.add(new MaterialIntegration(material));
  }

  public static void integrate(Material material, Fluid fluid) {
    integrationList.add(new MaterialIntegration(material, fluid));
  }

  public static void integrate(Material material, String oreRequirement) {
    integrationList.add(new MaterialIntegration(oreRequirement, material, null, null));
  }

  public static void integrate(Material material, Fluid fluid, String oreSuffix) {
    integrationList.add(new MaterialIntegration(material, fluid, oreSuffix));
  }

  public static void integrate(Fluid fluid, String oreSuffix) {
    integrationList.add(new MaterialIntegration("ingot" + oreSuffix, null, fluid, oreSuffix));
  }
}
