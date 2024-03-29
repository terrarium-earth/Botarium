package testmod;

import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestItem extends Item implements BotariumEnergyItem<WrappedItemEnergyContainer>, BotariumFluidItem<WrappedItemFluidContainer> {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public WrappedItemEnergyContainer getEnergyStorage(ItemStack stack) {
        return new WrappedItemEnergyContainer(stack, new SimpleEnergyContainer(1000000));
    }

    @Override
    public WrappedItemFluidContainer getFluidContainer(ItemStack stack) {
        return new WrappedItemFluidContainer(stack, new SimpleFluidContainer(1, 1, (integer, fluidHolder) -> true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        ItemStackHolder holder = new ItemStackHolder(stack);
        ItemFluidContainer itemFluidManager = FluidContainer.of(holder);
        if (itemFluidManager != null) {
            long oxygen = FluidConstants.toMillibuckets(itemFluidManager.getFluids().get(0).getFluidAmount());
            long oxygenCapacity = itemFluidManager.getTankCapacity(0);
            tooltip.add(Component.literal("Water: " + oxygen + "mb / " + oxygenCapacity + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        EnergyContainer energyManager = EnergyContainer.of(holder);
        if (energyManager != null) {
            long energy = energyManager.getStoredEnergy();
            long energyCapacity = energyManager.getMaxCapacity();
            tooltip.add(Component.literal("Energy: " + energy + "FE / " + energyCapacity + "FE").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        ManaContainerItem manaContainer = TestMod.MANA_ITEM_LOOKUP.find(stack, null);
        if (manaContainer != null) {
            long mana = manaContainer.getStoredAmount();
            long manaCapacity = manaContainer.getCapacity();
            tooltip.add(Component.literal("Mana: " + mana + " / " + manaCapacity).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        if (TestMod.MANA_DATA.hasData(stack)) {
            ManaContainer dataMana = TestMod.MANA_DATA.getData(stack);
            tooltip.add(Component.literal("Mana DATA: " + dataMana.storedAmount + " / " + dataMana.getCapacity()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }

    /*
     * Tests fluid transfer between 2 test items. To use, fill 1 test item with water and put it in the mainhand.
     * Then put another empty test item in the offhand. then right click to transfer from the mainhand to the
     * offhand and print the offhand amount.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        } else {
            ItemStack stack = player.getItemInHand(interactionHand);
            EnergyContainer energyManager = getEnergyStorage(stack);
            if (energyManager != null) {
                energyManager.setEnergy(100000);
            }
            TestMod.MANA_DATA.getDataOrInit(stack).insert(100, false);
        }
        return InteractionResultHolder.success(player.getMainHandItem());
    }
}
