package dev.v4lk.exmod.client;

import dev.v4lk.exmod.ConfigSynchronizer;
import dev.v4lk.exmod.DailyShopTradeOffer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ExchangemachinemodClient implements ClientModInitializer {
    public static List<DailyShopTradeOffer> matches = new ArrayList<>();
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.INIT.register(ConfigSynchronizer::client);
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            for(var item: matches){
                if(item.matches(stack)){
                    var addtext = Text.literal(String.format(I18n.translate("exmod.tooltip.purchase"),
                                    item.toShopAmount,
                                    item.fromShopAmount,
                                    I18n.translate(Registries.ITEM.get(item.fromShopItem).getTranslationKey())).substring("Format error: ".length()));
                    lines.add(addtext.setStyle(Style.EMPTY.withColor(item.getColor())));
                }
            }
        });
    }

}
