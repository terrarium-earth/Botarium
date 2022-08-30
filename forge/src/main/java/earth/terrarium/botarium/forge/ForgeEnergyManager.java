package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ForgeEnergyManager implements PlatformEnergyManager {
    private final IEnergyStorage energy;

    @SuppressWarnings("UnstableApiUsage")
    public ForgeEnergyManager(CapabilityProvider<?> energyItem) {
        this.energy = energyItem.getCapability(ForgeCapabilities.ENERGY).orElseThrow(IllegalArgumentException::new);
    }

    @SuppressWarnings("UnstableApiUsage")
    public ForgeEnergyManager(CapabilityProvider<?> energyItem, Direction direction) {
        this.energy = energyItem.getCapability(ForgeCapabilities.ENERGY, direction).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public long getStoredEnergy() {
        return energy.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public long extract(int amount, boolean simulate) {
        return energy.extractEnergy(amount, simulate);
    }

    @Override
    public long insert(int amount, boolean simulate) {
        return energy.receiveEnergy(amount, simulate);
    }
}
