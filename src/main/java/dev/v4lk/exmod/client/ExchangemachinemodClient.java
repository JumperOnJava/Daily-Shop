package dev.v4lk.exmod.client;

import dev.v4lk.exmod.ConfigSynchronizer;
import dev.v4lk.exmod.DailyShopTradeOffer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangemachinemodClient implements ClientModInitializer {
    public static List<DailyShopTradeOffer> trades = new ArrayList<>();
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.INIT.register(ConfigSynchronizer::client);
        TooltipComponentCallback.EVENT.register(data -> {
            if(data instanceof DailyShopTradeOffer offer){
                return offer.getTooltipComponent();
            }
            return null;
        });
    }

}
