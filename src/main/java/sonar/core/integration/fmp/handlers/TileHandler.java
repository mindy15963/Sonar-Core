package sonar.core.integration.fmp.handlers;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sonar.core.api.nbt.INBTSyncable;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.network.sync.DirtyPart;
import sonar.core.network.sync.ISyncPart;
import sonar.core.network.sync.SyncTagType;

/**
 * used for creating embedded handlers for blocks to allow easier alteration for
 * Forge Multipart components
 */
public abstract class TileHandler extends DirtyPart implements INBTSyncable {

	public final TileEntity tile;
	public SyncTagType.BOOLEAN isMultipart = new SyncTagType.BOOLEAN(-1);

	public TileHandler(boolean isMultipart, TileEntity tile) {
		this.isMultipart.setObject(isMultipart);
		this.tile = tile;
	}

	public void addSyncParts(List<ISyncPart> parts) {
		parts.add(isMultipart);
	}
	
	public abstract void update(TileEntity te);

	public void readData(NBTTagCompound nbt, SyncType type) {}

	public NBTTagCompound writeData(NBTTagCompound nbt, SyncType type) {
		return nbt;}
	
	public void markDirty() {	
		this.setChanged(true);
	}

	public void removed(World world, BlockPos pos, IBlockState state) {		
	}
}
