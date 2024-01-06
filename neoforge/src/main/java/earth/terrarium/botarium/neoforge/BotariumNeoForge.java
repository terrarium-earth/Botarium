package earth.terrarium.botarium.neoforge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.neoforge.energy.ForgeBlockEnergyContainer;
import earth.terrarium.botarium.neoforge.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.neoforge.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidBlockContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeItemFluidContainer;
import earth.terrarium.botarium.neoforge.item.ItemContainerWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@SuppressWarnings({"unused", "CodeBlock2Expr"})
@Mod(Botarium.MOD_ID)
public class BotariumNeoForge {

    public BotariumNeoForge(IEventBus bus) {
        Botarium.init();
        bus.addListener(BotariumNeoForge::registerEnergy);
        bus.addListener(BotariumNeoForge::registerFluid);
        bus.addListener(BotariumNeoForge::registerItem);
    }

    public static void registerItem(RegisterCapabilitiesEvent event) {
        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (blockEntity, object2) -> {
                if (blockEntity instanceof ItemContainerBlock itemContainerBlock) {
                    return new ItemContainerWrapper(itemContainerBlock.getContainer());
                }
                return null;
            });
        });
    }

    public static void registerEnergy(RegisterCapabilitiesEvent event) {
        EnergyApi.getBlockEntityRegistry().forEach((blockEntityType, blockEnergyGetter1) -> {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> {
                return new ForgeEnergyContainer<>(blockEnergyGetter1.getEnergyStorage(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
            });
        });

        EnergyApi.getBlockRegistry().forEach((block, blockEnergyGetter) -> {
            event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                return new ForgeBlockEnergyContainer<>(blockEnergyGetter.getEnergyStorage(level, blockPos, blockState, blockEntity, direction));
            }, block);
        });

        EnergyApi.getItemRegistry().forEach((item, itemEnergyGetter) -> {
            event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
                var energyContainer = itemEnergyGetter.getEnergyStorage(itemStack);
                if (energyContainer != null) {
                    return new ForgeItemEnergyContainer<>(energyContainer);
                }
                return null;
            }, item);
        });

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> {
                if (blockEntity instanceof BotariumEnergyBlock<?> energyBlock) {
                    return new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
                }
                return null;
            });
        });

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumEnergyItem<?>).forEach(item -> {
            event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
                BotariumEnergyItem<?> energyItem = (BotariumEnergyItem<?>) item;
                return new ForgeItemEnergyContainer<>(energyItem.getEnergyStorage(itemStack));
            }, item);
        });
    }

    public static void registerFluid(RegisterCapabilitiesEvent event) {
        FluidApi.FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.forEach((blockEntityType, blockFluidGetter1) -> {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
                return new ForgeFluidContainer<>(blockFluidGetter1.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
            });
        });

        FluidApi.FINALIZED_BLOCK_LOOKUP_MAP.forEach((block, blockFluidGetter) -> {
            event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                return new ForgeFluidBlockContainer<>(blockFluidGetter.getFluidContainer(level, blockPos, blockState, blockEntity, direction));
            }, block);
        });

        FluidApi.FINALIZED_ITEM_LOOKUP_MAP.forEach((item, itemFluidGetter) -> {
            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
                var fluidContainer = itemFluidGetter.getFluidContainer(itemStack);
                if (fluidContainer != null) {
                    return new ForgeItemFluidContainer<>(fluidContainer);
                }
                return null;
            }, item);
        });

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
                if (blockEntity instanceof BotariumFluidBlock<?> fluidBlock) {
                    return new ForgeFluidContainer<>(fluidBlock.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
                }
                return null;
            });
        });

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumFluidItem<?>).forEach(item -> {
            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
                BotariumFluidItem<?> fluidHoldingItem = (BotariumFluidItem<?>) item;
                return new ForgeItemFluidContainer<>(fluidHoldingItem.getFluidContainer(itemStack));
            }, item);
        });
    }
}