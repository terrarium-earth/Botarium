package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.fluid.FluidConstants;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.fluids.FluidType;

@ClassExtension(FluidConstants.class)
public class FluidConstantsImpl {
    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount;
    }

    @ImplementsBaseElement
    public static long fromMillibuckets(long amount) {
        return amount;
    }

    @ImplementsBaseElement
    public static long getBucketAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    public static long getBottleAmount() {
        return FluidType.BUCKET_VOLUME / 4;
    }

    @ImplementsBaseElement
    public static long getBlockAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    public static long getIngotAmount() {
        return 90;
    }

    @ImplementsBaseElement
    public static long getNuggetAmount() {
        return 10;
    }
}
