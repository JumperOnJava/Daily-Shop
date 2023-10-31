package dev.v4lk.multitooltip;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class MultiTooltipComponent implements TooltipComponent {
    private final int height;
    private final int width;
    private final List<TooltipComponent> components;

    public static MultiTooltipComponent of(MultiTooltipData data){
        var l = new ArrayList<TooltipComponent>(data.tooltipDataList.size());
        for(var d : data.tooltipDataList){
            l.add(TooltipComponent.of(d));
        }
        return new MultiTooltipComponent(l);
    }

    public MultiTooltipComponent(List<TooltipComponent> components) {
        this.components = components;
        int height=0;
        int width=0;
        for (TooltipComponent component : components) {
                height += component.getHeight();
            width = Math.max(width, component.getWidth(MinecraftClient.getInstance().textRenderer));
        }
        this.height = height;
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return width;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        int position = 0;
        for(var c : components){
            matrix.translate(0,position,0);
            c.drawText(textRenderer,x,y,matrix,vertexConsumers);
            matrix.translate(0,-position,0);
            position+=c.getHeight();
        }
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int position = 0;
        for(var c : components) {
            context.getMatrices().push();
            context.getMatrices().translate(0,position,0);
            c.drawItems(textRenderer,x,y,context);
            context.getMatrices().pop();
            position+=c.getHeight();
        }
    }
}
