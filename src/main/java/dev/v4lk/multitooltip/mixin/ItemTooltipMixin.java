package dev.v4lk.multitooltip.mixin;

import dev.v4lk.multitooltip.MultiTooltipData;
import dev.v4lk.multitooltip.TooltipInit;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(HandledScreen.class)
public class ItemTooltipMixin {
    @Redirect(method = "drawMouseoverTooltip",at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;getTooltipData()Ljava/util/Optional;"))
    Optional<TooltipData> addMultiData(ItemStack stack){
        var original = stack.getTooltipData();
        var mutlidata = new MultiTooltipData();
        original.ifPresent(mutlidata.tooltipDataList::add);
        for (var m : TooltipInit.matches) {
            if(m.matches(stack)){
                mutlidata.tooltipDataList.add(m.data());
            }
        }
        return mutlidata.tooltipDataList.size() > 0 ? Optional.of(mutlidata) : original;
    }

}
