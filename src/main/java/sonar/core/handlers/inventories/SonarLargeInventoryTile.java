package sonar.core.handlers.inventories;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import sonar.core.api.inventories.ISonarInventoryTile;
import sonar.core.handlers.inventories.handling.EnumFilterType;
import sonar.core.handlers.inventories.handling.IInventoryWrapper;

import javax.annotation.Nonnull;

public class SonarLargeInventoryTile extends SonarLargeInventory {

	public final ISonarInventoryTile tile;

	public SonarLargeInventoryTile(ISonarInventoryTile tile) {
		this(tile, 1, 64);
	}

	public SonarLargeInventoryTile(ISonarInventoryTile tile, int size, int stackSize) {
		super(size, stackSize);
		this.tile = tile;
		this.getInsertFilters().put((SLOT,STACK,FACE)-> tile.checkInsert(SLOT,STACK,FACE,EnumFilterType.INTERNAL), EnumFilterType.INTERNAL);
		this.getInsertFilters().put((SLOT,STACK,FACE)-> tile.checkInsert(SLOT,STACK,FACE,EnumFilterType.EXTERNAL), EnumFilterType.EXTERNAL);
		this.getExtractFilters().put((SLOT,COUNT,FACE)-> tile.checkExtract(SLOT,COUNT,FACE,EnumFilterType.INTERNAL), EnumFilterType.INTERNAL);
		this.getExtractFilters().put((SLOT,COUNT,FACE)-> tile.checkExtract(SLOT,COUNT,FACE,EnumFilterType.EXTERNAL), EnumFilterType.EXTERNAL);
	}

	@Override
	public IInventory getWrapperInventory() {
		if(!(tile instanceof TileEntity)){
			return super.getWrapperInventory();
		}
		return wrapped_inv == null ? wrapped_inv = new IInventoryWrapper(this, (TileEntity) tile) : wrapped_inv;
	}

	@Override
	public boolean checkDrop(int slot, @Nonnull ItemStack stack){
		return tile.checkDrop(slot, stack);
	}

	@Override
	protected void onContentsChanged(int slot){
		super.onContentsChanged(slot);
		tile.onInventoryContentsChanged(slot);
	}

}