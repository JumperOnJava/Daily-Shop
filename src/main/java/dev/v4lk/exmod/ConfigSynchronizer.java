package dev.v4lk.exmod;

import io.github.jumperonjava.multitooltipapi.MutliTooltipApi;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConfigSynchronizer {
    public static final Identifier CHANNEL = new Identifier("exmod","init");
    public static void server(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        ServerPlayNetworking.send(serverPlayNetworkHandler.player,new SyncPacket(Exchangemachinemod.SYNC_TRADES));
    }
    public static void client(ClientPlayNetworkHandler networkHandler, MinecraftClient client) {
        ClientPlayNetworking.registerGlobalReceiver(SyncPacket.TYPE,ConfigSynchronizer::sync);
    }

    private static void sync(SyncPacket syncPacket, ClientPlayerEntity clientPlayerEntity, PacketSender packetSender) {
        MutliTooltipApi.matches.removeIf(match -> match instanceof DailyShopTradeOffer);
        MutliTooltipApi.matches.addAll(syncPacket.trades);
    }

    public static class SyncPacket implements FabricPacket {

        public final List<DailyShopTradeOffer> trades;

        public SyncPacket(PacketByteBuf buf){
            var l = new LinkedList<DailyShopTradeOffer>();
            var len = buf.readVarInt();
            for(int i=0;i<len;i++){
                l.add(new DailyShopTradeOffer(buf.readIdentifier(), buf.readIdentifier(), buf.readVarInt(), buf.readVarInt(),buf.readInt()));
            }
            trades = l;
        }
        public SyncPacket(java.util.List<DailyShopTradeOffer> trades){
            this.trades = trades;
        }
        @Override
        public void write(PacketByteBuf buf) {
            buf.writeVarInt(trades.size());
            for(var t : trades){
                buf.writeIdentifier(t.toShopItem);
                buf.writeIdentifier(t.fromShopItem);
                buf.writeVarInt(t.toShopAmount);
                buf.writeVarInt(t.fromShopAmount);
                buf.writeInt(t.getColor());
            }
        }
        public PacketType<?> getType() {
            return TYPE;
        }

        public static final PacketType<SyncPacket> TYPE = PacketType.create(CHANNEL,SyncPacket::new);
    }
}
