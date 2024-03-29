package earth.terrarium.botarium;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.fluid.utils.FluidParticleOptions;
import earth.terrarium.botarium.common.registry.RegistryHolder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Botarium {
    public static final String MOD_ID = "botarium";
    public static final String BOTARIUM_DATA = "BotariumData";
    public static final Logger LOGGER = LogManager.getLogger("Botarium");

    public static final RegistryHolder<ParticleType<?>> PARTICLES = new RegistryHolder<>(BuiltInRegistries.PARTICLE_TYPE, MOD_ID);

    public static final Supplier<ParticleType<FluidParticleOptions>> FLUID_PARTICLE = PARTICLES.register("fluid", () -> new ParticleType<>(false, FluidParticleOptions.DESERIALIZER) {
        @Override
        public @NotNull Codec<FluidParticleOptions> codec() {
            return FluidParticleOptions.CODEC;
        }
    });

    public static void init() {
        PARTICLES.initialize();
    }

    public static <T, U> Map<T, U> finalizeRegistration(Map<Supplier<T>, U> unfinalized, @Nullable Map<T, U> finalized) {
        if (finalized == null) {
            Map<T, U> collected = unfinalized.entrySet().stream().map(entry -> Pair.of(entry.getKey().get(), entry.getValue())).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
            unfinalized.clear();
            return collected;
        }

        return finalized;
    }

}