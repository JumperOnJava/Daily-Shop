package dev.v4lk.exmod.client.mixin;

import dev.v4lk.exmod.DailyShopTradeOffer;
import dev.v4lk.exmod.client.ExchangemachinemodClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.event.ItemEvent;
import java.util.Optional;

@Mixin(Item.class)
public class ItemTooltipMixin {
    @Inject(method = "getTooltipData",at = @At("HEAD"),cancellable = true)
    void addBundleSlots(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir){
        var dailyShopTradeOffer = ExchangemachinemodClient.getTradeMap().get(Registries.ITEM.getId(stack.getItem()));
        if(dailyShopTradeOffer!=null)
            cir.setReturnValue(Optional.of(dailyShopTradeOffer));

    }

}
