package dev.v4lk.multitooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;

public interface Match {
    TooltipData data();
    boolean matches(ItemStack stack);
}
