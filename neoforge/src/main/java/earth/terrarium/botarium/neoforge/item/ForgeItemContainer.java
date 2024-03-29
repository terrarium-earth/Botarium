package earth.terrarium.botarium.neoforge.item;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForgeItemContainer(ItemContainer container) implements IItemHandler {
    public static ForgeItemContainer of(@Nullable ItemContainer container) {
        return container == null ? null : new ForgeItemContainer(container);
    }

    @Override
    public int getSlots() {
        return container.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return container.getStackInSlot(i);
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean simulate) {
        ItemStack stack = container.insertIntoSlot(i, arg, simulate);
        if (!simulate && container instanceof Updatable updatable) {
            updatable.update();
        }
        if (stack.getCount() >= arg.getCount()) {
            return ItemStack.EMPTY;
        }
        return arg.copyWithCount(arg.getCount() - stack.getCount());
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int j, boolean simulate) {
        ItemStack stack = container.extractFromSlot(i, j, simulate);
        if (!simulate && container instanceof Updatable updatable) {
            updatable.update();
        }
        return stack;
    }

    @Override
    public int getSlotLimit(int i) {
        return container.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return container.isItemValid(i, arg);
    }
}
