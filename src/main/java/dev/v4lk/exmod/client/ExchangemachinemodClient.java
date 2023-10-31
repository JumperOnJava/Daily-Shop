package dev.v4lk.exmod.client;

import dev.v4lk.exmod.ConfigSynchronizer;
import dev.v4lk.exmod.DailyShopTradeOffer;
import dev.v4lk.multitooltip.MultiTooltipComponent;
import dev.v4lk.multitooltip.MultiTooltipData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangemachinemodClient implements ClientModInitializer {
    public static List<DailyShopTradeOffer> trades = new ArrayList<>();
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.INIT.register(ConfigSynchronizer::client);
    }

}
