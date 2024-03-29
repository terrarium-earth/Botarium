package earth.terrarium.botarium.fabric.data;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.DataManagerBuilder;
import earth.terrarium.botarium.common.data.DataManagerRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.fabricmc.fabric.impl.attachment.AttachmentTypeImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricDataManagerRegistry implements DataManagerRegistry {
    private final String modid;

    public FabricDataManagerRegistry(String modid) {
        this.modid = modid;
    }

    @Override
    public <T> DataManager<T> register(@NotNull String name, @NotNull Supplier<T> factory, @Nullable Codec<T> codec, boolean copyOnDeath) {
        ResourceLocation id = new ResourceLocation(modid, name);
        var type = new AttachmentTypeImpl<>(id, factory, codec, copyOnDeath);
        AttachmentRegistryImpl.register(id, type);
        return new FabricDataManager<>(type);
    }

    @Override
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new FabricDataManagerBuilder<>(modid, factory);
    }

    @Override
    public void initialize() {

    }
}
