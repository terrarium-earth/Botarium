package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import earth.terrarium.botarium.util.Snapshotable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class ItemApi {
    public static final ItemContainerLookup<ItemContainer, Void> ITEM = getItemLookup();
    public static final BlockContainerLookup<ItemContainer, Direction> SIDED = getBlockLookup();
    public static final EntityContainerLookup<ItemContainer, Void> ENTITY = getEntityLookup();
    public static final EntityContainerLookup<ItemContainer, Direction> ENTITY_AUTOMATION = getEntityAutomationLookup();

    private static ItemContainerLookup<ItemContainer, Void> getItemLookup() {
        throw new NotImplementedException();
    }

    private static BlockContainerLookup<ItemContainer, Direction> getBlockLookup() {
        throw new NotImplementedException();
    }

    private static EntityContainerLookup<ItemContainer, Void> getEntityLookup() {
        throw new NotImplementedException();
    }

    private static EntityContainerLookup<ItemContainer, Direction> getEntityAutomationLookup() {
        throw new NotImplementedException();
    }
}
