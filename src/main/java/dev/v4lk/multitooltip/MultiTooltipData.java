package dev.v4lk.multitooltip;

import net.minecraft.client.item.TooltipData;

import java.util.ArrayList;
import java.util.List;

public class MultiTooltipData implements TooltipData {
    public final List<TooltipData> tooltipDataList;

    public MultiTooltipData(List<TooltipData> tooltipDataList) {
        this.tooltipDataList = tooltipDataList;
    }
    public MultiTooltipData(){
        this.tooltipDataList = new ArrayList<>(8);
    }
}
