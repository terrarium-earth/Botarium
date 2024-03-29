package earth.terrarium.botarium.fabric.data;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.DataManagerBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricDataManagerBuilder<T> implements DataManagerBuilder<T> {
    private final AttachmentRegistry.Builder<T> builder;
    private final String modid;

    public FabricDataManagerBuilder(String modid, Supplier<T> factory) {
        this.builder = AttachmentRegistry.builder();
        this.modid = modid;
        this.builder.initializer(factory);
    }

    @Override
    public DataManagerBuilder<T> serialize(Codec<T> codec) {
        this.builder.persistent(codec);
        return this;
    }

    @Override
    public DataManagerBuilder<T> copyOnDeath() {
        this.builder.copyOnDeath();
        return this;
    }

    @Override
    public DataManager<T> buildAndRegister(String name) {
        AttachmentType<T> tAttachmentType = this.builder.buildAndRegister(new ResourceLocation(modid, name));
        return new FabricDataManager<>(tAttachmentType);
    }
}
