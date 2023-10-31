package dev.v4lk.exmod;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class DailyShopTradeOffer implements TooltipData {
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

    public TooltipComponent getTooltipComponent(TooltipData tooltipData) {
        return new TradeTooltipComponent(this);
    }

    private static class TradeTooltipComponent implements TooltipComponent {
        private final DailyShopTradeOffer offer;

        public TradeTooltipComponent(DailyShopTradeOffer dailyShopTradeOffer) {
            this.offer = dailyShopTradeOffer;
        }

        @Override
        public int getHeight() {
            return 24;
        }

        @Override
        public int getWidth(TextRenderer textRenderer) {
            var purchaseText = textRenderer.getWidth(Text.translatable("exmod.tooltip.purchase"));
            //var fromShopItem = null;
            var forText = textRenderer.getWidth(Text.translatable("exmod.tooltip.for"));
            //var toShopItem = null;
            return purchaseText + 18*2 + 2*2 +forText;
        }

        @Override
        public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
            context.getMatrices().push();
            context.getMatrices().translate(x,y,0);
            var purchasableText = Text.translatable("exmod.avaliable").setStyle(Style.EMPTY.withColor(offer.getColor()));
            context.drawText(textRenderer,purchasableText,0,0,0xFFFFFFFF,false);

            int position = 0;
            var purchaseText = Text.translatable("exmod.tooltip.purchase");
            var forText = Text.translatable("exmod.tooltip.for");
            var fromShopItem = new ItemStack(Registries.ITEM.get(offer.fromShopItem), offer.fromShopAmount);
            var toShopItem = new ItemStack(Registries.ITEM.get(offer.toShopItem), offer.toShopAmount);
            context.getMatrices().translate(0,8,0);
            context.drawText(textRenderer,purchaseText,position,4,0xFFFFFFFF,false);
            position+=2;
            position+=textRenderer.getWidth(purchaseText);
            context.drawItem(toShopItem,position,0);
            context.drawItemInSlot(textRenderer,toShopItem,position,0);
            position+=18;
            context.drawText(textRenderer,forText,position,4,0xFFFFFFFF,false);
            position+=textRenderer.getWidth(forText);
            position+=2;
            context.drawItem(fromShopItem,position,0);
            context.drawItemInSlot(textRenderer,fromShopItem,position,0);
            position+=18;
            context.getMatrices().pop();

        }
    }
}
