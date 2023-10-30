package dev.v4lk.exmod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExchangeBlockMerchant extends MerchantEntity {
    TradeOfferList list;
    public ExchangeBlockMerchant(EntityType<? extends MerchantEntity> entityType, World world, TradeOfferList list) {
        super(entityType, world);
        this.list = list;
    }

    @Override
    protected void afterUsing(TradeOffer offer) {

    }

    @Override
    protected void fillRecipes() {
       offers = list;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
