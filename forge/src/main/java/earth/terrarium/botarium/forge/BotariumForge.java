package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.base.EnergyAttachment;
import earth.terrarium.botarium.common.fluid.base.FluidAttachment;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.forge.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.forge.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.forge.item.ItemContainerWrapper;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(Botarium.MOD_ID)
@SuppressWarnings("unchecked")
public class BotariumForge {
    public BotariumForge() {
        Botarium.init();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, BotariumForge::attachBlockCapabilities);
    }

    public static void attachBlockCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof EnergyAttachment energyBlock && energyBlock.getEnergyHolderType() == BlockEntity.class) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeEnergyContainer(energyBlock.getEnergyStorage(event.getObject()), (Updatable<BlockEntity>) energyBlock.getEnergyStorage(event.getObject()), event.getObject()));
        }

        if (event.getObject() instanceof FluidAttachment fluidHoldingBlock && fluidHoldingBlock.getFluidHolderType() == BlockEntity.class) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeFluidContainer(fluidHoldingBlock.getFluidContainer(event.getObject())));
        }

        if (event.getObject() instanceof ItemContainerBlock itemContainerBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "item"), new ItemContainerWrapper(itemContainerBlock.getContainer()));
        }
    }
}