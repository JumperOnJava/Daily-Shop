package dev.v4lk.multitooltip;

import dev.v4lk.exmod.DailyShopTradeOffer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class TextItemTooltipComponent implements TooltipComponent {

    private final ItemStack renderItem;
    private final Text renderText;

    public TextItemTooltipComponent(Text renderText, ItemStack renderItem) {
        this.renderText = renderText;
        this.renderItem = renderItem;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int purchaseText = textRenderer.getWidth(Text.translatable("exmod.tooltip.purchase"));
        return purchaseText + 20;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y - 4, 0);
        //var purchasableText = Text.translatable("exmod.avaliable");
        //context.drawText(textRenderer,purchasableText,0,0,0xFFFFFFFF,false);
        //context.getMatrices().translate(0,8,0);

        int position = 0;
        var purchaseText = renderText;
        var fromShopItem = renderItem;
        context.drawText(textRenderer, purchaseText, position, 4, 0xFFFFFFFF, false);
        position += 0;
        position += textRenderer.getWidth(purchaseText);
        position += 2;
        context.drawItem(fromShopItem, position, 0);
        context.drawItemInSlot(textRenderer, fromShopItem, position, 0, String.valueOf(renderItem.getCount()));
        position += 18;
        context.getMatrices().pop();

    }
}
