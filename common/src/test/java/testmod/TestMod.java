package testmod;

import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.DataManagerRegistry;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.UnlimitedEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.UnlimitedFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.common.generic.LookupApi;
import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.common.registry.RegistryHelpers;
import earth.terrarium.botarium.common.registry.RegistryHolder;
import earth.terrarium.botarium.common.registry.fluid.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TestMod {
    public static final String MOD_ID = "testmod";

    public static final RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final RegistryHolder<Block> BLOCKS = new RegistryHolder<>(BuiltInRegistries.BLOCK, MOD_ID);
    public static final RegistryHolder<Item> ITEMS = new RegistryHolder<>(BuiltInRegistries.ITEM, MOD_ID);

    public static final RegistryHolder<Fluid> FLUIDS = new RegistryHolder<>(BuiltInRegistries.FLUID, MOD_ID);
    public static final DataManagerRegistry DATA_REG = DataManagerRegistry.create(MOD_ID);
    public static final FluidRegistry FLUID_TYPES = new FluidRegistry(MOD_ID);


    public static final Supplier<Block> EXAMPLE_BLOCK = BLOCKS.register("block", () -> new TestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<Block> EXAMPLE_BLOCK_UNLIMITED = BLOCKS.register("block_unlimited", () -> new TestBlockNonInterface(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<?>> EXAMPLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("block", () -> RegistryHelpers.createBlockEntityType(TestBlockEntity::new, EXAMPLE_BLOCK.get()));
    public static final Supplier<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.register("block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Item> EXAMPLE_ITEM = ITEMS.register("item", () -> new TestItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> EXAMPLE_ITEM_NO_INTERFACE = ITEMS.register("item_two", () -> new TestNonInterfaceItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> EXAMPLE_ITEM_UNLIMITED = ITEMS.register("item_unlimited", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final Supplier<Block> EXAMPLE_PIPE = BLOCKS.register("pipe", () -> new ExamplePipe(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<?>> EXAMPLE_PIPE_ENTITY = BLOCK_ENTITIES.register("pipe", () -> RegistryHelpers.createBlockEntityType(ExampleN2SPipeBlockEntity::new, EXAMPLE_PIPE.get()));
    public static final Supplier<BlockItem> EXAMPLE_PIPE_ITEM = ITEMS.register("pipe", () -> new BlockItem(EXAMPLE_PIPE.get(), new Item.Properties()));

    public static final FluidData TEST_FLUID = FLUID_TYPES.register(new TestFluidInformation());

    public static final Supplier<Fluid> TEST_FLUID_SOURCE = FLUIDS.register("test_fluid", () -> new BotariumSourceFluid(TEST_FLUID));
    public static final Supplier<Fluid> TEST_FLUID_FLOWING = FLUIDS.register("test_fluid_flowing", () -> new BotariumFlowingFluid(TEST_FLUID));
    public static final Supplier<Block> TEST_FLUID_BLOCK = BLOCKS.register("test_fluid_block", () -> new BotariumLiquidBlock(TEST_FLUID, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final Supplier<FluidBucketItem> TEST_BUCKET = ITEMS.register("test_bucket", () -> new FluidBucketItem(TEST_FLUID, new Item.Properties()));

    public static final BlockContainerLookup<ManaContainer, @Nullable Direction> MANA_LOOKUP_BLOCK = LookupApi.createBlockLookup(new ResourceLocation(MOD_ID, "mana"), ManaContainer.class);
    public static final ItemContainerLookup<ManaContainerItem, Void> MANA_ITEM_LOOKUP = LookupApi.createItemLookup(new ResourceLocation(MOD_ID, "mana"), ManaContainerItem.class);

    public static final DataManager<ManaContainer> MANA_DATA = DATA_REG.register("mana", ManaContainer::new, ManaContainer.CODEC, true);

    @SuppressWarnings("unchecked")
    public static void init() {
        BLOCK_ENTITIES.initialize();
        BLOCKS.initialize();
        ITEMS.initialize();

        FLUID_TYPES.initialize();
        FLUIDS.initialize();

        DATA_REG.initialize();

        FluidApi.registerFluidItem(EXAMPLE_ITEM_NO_INTERFACE, stack -> new WrappedItemFluidContainer(stack, new SimpleFluidContainer(FluidConstants.fromMillibuckets(4000), 1, (integer, fluidHolder) -> true)));
        FluidApi.registerFluidItem(EXAMPLE_ITEM_UNLIMITED, stack -> new WrappedItemFluidContainer(stack, new UnlimitedFluidContainer(TEST_FLUID_SOURCE.get())));
        FluidApi.registerFluidBlock(EXAMPLE_BLOCK_UNLIMITED, (level, blockPos, blockState, blockEntity, direction) -> new UnlimitedFluidContainer(TEST_FLUID_SOURCE.get()));
        EnergyApi.registerEnergyItem(EXAMPLE_ITEM_UNLIMITED, stack -> new UnlimitedEnergyContainer());
        EnergyApi.registerEnergyItem(EXAMPLE_ITEM_NO_INTERFACE, stack -> new WrappedItemEnergyContainer(stack, new SimpleEnergyContainer(100000)));
        EnergyApi.registerEnergyBlock(EXAMPLE_BLOCK_UNLIMITED, (level, blockPos, blockState, blockEntity, direction) -> new UnlimitedEnergyContainer());
        MANA_LOOKUP_BLOCK.registerBlockEntities((level, pos, state, entity, direction) -> {
            if (entity instanceof TestBlockEntity testBlockEntity) {
                return testBlockEntity.getManaContainer();
            }
            return null;
        }, EXAMPLE_BLOCK_ENTITY);
        MANA_ITEM_LOOKUP.registerItems((stack, context) -> new ManaContainerItem(stack), EXAMPLE_ITEM);
    }
}
