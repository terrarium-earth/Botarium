package earth.terrarium.botarium.neoforge;

import earth.terrarium.botarium.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface AutoSerializable extends INBTSerializable<CompoundTag> {
    Serializable getSerializable();

    @Override
    default CompoundTag serializeNBT() {
        return getSerializable().serialize(new CompoundTag());
    }

    @Override
    default void deserializeNBT(CompoundTag arg) {
        getSerializable().deserialize(arg);
    }
}
