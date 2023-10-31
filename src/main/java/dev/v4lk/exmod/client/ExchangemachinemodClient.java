package dev.v4lk.exmod.client;

import dev.v4lk.exmod.ConfigSynchronizer;
import dev.v4lk.exmod.DailyShopTradeOffer;
import io.github.jumperonjava.multitooltipapi.MutliTooltipApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import java.util.ArrayList;
import java.util.List;

public class ExchangemachinemodClient implements ClientModInitializer {
    public static List<DailyShopTradeOffer> trades = new ArrayList<>();
    @Override
    public void onInitializeClient() {
        new MutliTooltipApi().initialize();
        ClientPlayConnectionEvents.INIT.register(ConfigSynchronizer::client);
        TooltipComponentCallback.EVENT.register(data -> {
            if(data instanceof DailyShopTradeOffer offer){
                return offer.getTooltipComponent();
            }
            return null;
        });
    }

}
