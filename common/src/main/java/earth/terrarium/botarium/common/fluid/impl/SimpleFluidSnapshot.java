package earth.terrarium.botarium.common.fluid.impl;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SimpleFluidSnapshot implements FluidSnapshot {
    public final List<FluidHolder> fluids;

    public SimpleFluidSnapshot(FluidContainer fluidContainer) {
        this.fluids = new ArrayList<>();
        fluidContainer.getFluids().forEach(fluidHolder -> this.fluids.add(fluidHolder.copyHolder()));
    }

    @Override
    public void loadSnapshot(FluidContainer container) {
        for (int i = 0; i < Math.min(container.getSize(), fluids.size()); i++) {
            container.getFluids().set(i, fluids.get(i).copyHolder());
        }
    }
}