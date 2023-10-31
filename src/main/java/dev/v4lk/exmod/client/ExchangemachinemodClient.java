package dev.v4lk.exmod.client;

import dev.v4lk.exmod.ConfigSynchronizer;
import dev.v4lk.exmod.DailyShopTradeOffer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangemachinemodClient implements ClientModInitializer {
    public static List<DailyShopTradeOffer> trades = new ArrayList<>();

    public static Map<Identifier,DailyShopTradeOffer> getTradeMap(){
        var tradeMap = new HashMap<Identifier, DailyShopTradeOffer>();
        trades.forEach(t->tradeMap.put(t.toShopItem,t));
        return tradeMap;
    }
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.INIT.register(ConfigSynchronizer::client);
        TooltipComponentCallback.EVENT.register(this::tooltipContextEvent);
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            var tmap = getTradeMap();
            //stack.getTooltip()
            var dailyShopTradeOffer = tmap.get(Registries.ITEM.getId(stack.getItem()));
            if(dailyShopTradeOffer==null)
                return;
            //lines.add();
        });
    }

    private TooltipComponent tooltipContextEvent(TooltipData tooltipData) {
        if(tooltipData instanceof DailyShopTradeOffer offer){
            return offer.getTooltipComponent(tooltipData);
        }
        return null;
    }
}
