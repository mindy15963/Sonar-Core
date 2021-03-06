package sonar.core.network;

import java.util.Optional;

import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.core.SonarCore;
import sonar.core.integration.multipart.SonarMultipartHelper;

public abstract class PacketMultipartHandler<T extends PacketMultipart> implements IMessageHandler<T, IMessage> {

	public final IMessage onMessage(T message, MessageContext ctx) {
		EntityPlayer player = SonarCore.proxy.getPlayerEntity(ctx);
		if (player != null) {
			World world = player.getEntityWorld();
			if (world.isBlockLoaded(message.pos)) { // prevents errors and arbitrary chunk generation
				if (message.slotID != -1) {
					Optional<IMultipartTile> multipartTile = SonarMultipartHelper.getMultipartTileFromSlotID(world, message.pos, message.slotID);
					if (multipartTile.isPresent()) {
						return processMessage(message, player, world, multipartTile.get(), ctx);
					}
					return onFailure(message, player, world, ctx);
				} else {
					TileEntity tile = world.getTileEntity(message.pos);
					if (tile instanceof IMultipartTile) {
						return processMessage(message, player, world, (IMultipartTile) tile, ctx);
					}
					return onFailure(message, player, world, ctx);
				}
			}
			return onFailure(message, player, world, ctx);
		}
		return null;
	}

	public abstract IMessage processMessage(T message, EntityPlayer player, World world, IMultipartTile part, MessageContext ctx);

	public IMessage onFailure(T message, EntityPlayer player, World world, MessageContext ctx){
		return null;
	}
}