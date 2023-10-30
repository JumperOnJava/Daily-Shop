package dev.v4lk.exmod;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class DailyShopTradeOffer{
    private String color = Integer.toString(Formatting.DARK_GRAY.getColorIndex(),16).toUpperCase();
    private static final String DEFAULT_COLOR = "FF555555";
    public final Identifier toShopItem;
    public final Identifier fromShopItem;
    public final int toShopAmount;
    public final int fromShopAmount;

    public DailyShopTradeOffer(Identifier toShopItem, Identifier fromShopItem, int toShopAmount, int fromShopAmount, int color) {
        this(toShopItem,fromShopItem,toShopAmount,fromShopAmount,null);
        setColor(color);
    }

    public DailyShopTradeOffer(Identifier toShopItem, Identifier fromShopItem, int toShopAmount, int fromShopAmount, String color) {
        this.toShopItem = toShopItem;
        this.fromShopItem = fromShopItem;
        this.toShopAmount = toShopAmount;
        this.fromShopAmount = fromShopAmount;
        this.color=color;
        if(this.color==null)
            this.color=DEFAULT_COLOR;
    }

    public int getColor() {
        return Integer.parseUnsignedInt(this.color,16);
    }
    public void setColor(int color) {
        this.color = Integer.toHexString(color);
    }
}
