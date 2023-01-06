package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.menu.base.EnergyAttachment;
import earth.terrarium.botarium.common.menu.base.EnergyContainer;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidAttachment;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.fabric.energy.FabricBlockEnergyContainer;
import earth.terrarium.botarium.fabric.energy.FabricItemEnergyContainer;
import earth.terrarium.botarium.fabric.fluid.storage.FabricBlockFluidContainer;
import earth.terrarium.botarium.fabric.fluid.storage.FabricItemFluidContainer;
import earth.terrarium.botarium.util.Updatable;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class BotariumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Botarium.init();
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof EnergyAttachment energyCapable && energyCapable.getHolderType() == BlockEntity.class) {
                EnergyContainer container = energyCapable.getEnergyStorage(blockEntity).getContainer(context);
                return container == null ? null : new FabricBlockEnergyContainer(container, (Updatable<BlockEntity>) energyCapable.getEnergyStorage(blockEntity), blockEntity);
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if(itemStack.getItem() instanceof EnergyAttachment energyCapable && energyCapable.getHolderType() == ItemStack.class) {
                return new FabricItemEnergyContainer(context, energyCapable.getEnergyStorage(itemStack));
            }
            return null;
        });
        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if(blockEntity instanceof FluidAttachment fluidHoldingBlock && fluidHoldingBlock.getHolderType() == BlockEntity.class) {
                FluidContainer container = fluidHoldingBlock.getFluidContainer(blockEntity).getContainer(context);
                return container == null ? null : new FabricBlockFluidContainer(container, (Updatable<BlockEntity>) fluidHoldingBlock.getFluidContainer(blockEntity), blockEntity);
            }
            return null;
        });
        FluidStorage.ITEM.registerFallback((itemStack, context) -> {
            if(itemStack.getItem() instanceof FluidAttachment fluidHoldingBlock && fluidHoldingBlock.getHolderType() == ItemStack.class) {
                return new FabricItemFluidContainer(context, fluidHoldingBlock.getFluidContainer(itemStack));
            }
            return null;
        });
        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if(blockEntity instanceof ItemContainerBlock energyContainer) {
                return InventoryStorageImpl.of(energyContainer.getContainer(), context);
            }
            return null;
        });

    }
}