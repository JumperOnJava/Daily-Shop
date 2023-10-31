package dev.v4lk.multitooltip;

import dev.v4lk.exmod.DailyShopTradeOffer;
import dev.v4lk.sellingbin.Trade;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import java.util.ArrayList;

public class TooltipInit implements ClientModInitializer {
    public static java.util.List<Match> matches = new ArrayList<>();
    public void onInitializeClient() {
        TooltipComponentCallback.EVENT.register((tooltipData)->{
            if(tooltipData instanceof DailyShopTradeOffer offer){
                return offer.getTooltipComponent();
            }
            if(tooltipData instanceof Trade trade) {
                return trade.getTooltipComponent();
            }
            if(tooltipData instanceof MultiTooltipData multiTooltipData){
                return MultiTooltipComponent.of(multiTooltipData);
            }
            return null;
        });
    }
}
