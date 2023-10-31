package dev.v4lk.sellingbin.client;

import dev.v4lk.sellingbin.ConfigSynchronizer;
import dev.v4lk.sellingbin.Trade;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellingBinModClient implements ClientModInitializer {
    public static List<Trade> trades = new ArrayList<>();
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.INIT.register(ConfigSynchronizer::client);
    }
}
