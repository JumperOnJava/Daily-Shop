package dev.v4lk.exmod;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExchangeBlock extends HorizontalFacingBlock {
    protected TradeOfferList offers;
    protected ArrayList<Item> prevItems = new ArrayList<>();
    TradeOfferList prevList = new TradeOfferList();
    protected long prevSeed = -1;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public TradeOfferList getOffers(World world) {
        if (this.offers == null) {
            this.offers = new TradeOfferList();
            this.fillRecipes(world);
        }

        return this.offers;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    protected void fillRecipesFromPool(TradeOfferList recipeList, TradeOffers.Factory[] pool, ArrayList<Integer> rarities, int count, World world) {
        long seed = (world.getTimeOfDay() / 24000L % 2147483647L);

        if (prevSeed != seed) {
            prevList.clear();
            ArrayList<Item> prevprevItems = new ArrayList<>(prevItems);
            prevItems.clear();
            Random random = Random.create(seed);
            CopyOnWriteArrayList<Integer> weightedIndices = new CopyOnWriteArrayList<>();

            int totalWeight = 0;
            for (int rarity : rarities) {
                totalWeight += rarity;
            }

            for (int i = 0; i < pool.length; ++i) {
                int rarity = rarities.get(i);
                int weight = totalWeight / rarity;
                for (int j = 0; j < weight; j++) {
                    weightedIndices.add(i);
                }
            }

            Collections.shuffle(weightedIndices, new RandomWrapper(random));

            weightedIndices.removeIf((n) -> {
                TradeOffers.Factory factory = pool[n];
                TradeOffer tradeOffer = factory.create(null, random);

                if (prevprevItems.contains(tradeOffer.getSellItem().getItem())) {
                    return true;
                }

                return false;
            });

            for (int i = 0; i < Math.min(weightedIndices.size(), count); i++) {
                int index = weightedIndices.get(i);
                TradeOffers.Factory factory = pool[index];
                TradeOffer tradeOffer = factory.create(null, random);

                if (tradeOffer != null) {
                    recipeList.add(tradeOffer);
                    prevList.add(tradeOffer);
                    prevItems.add(tradeOffer.getSellItem().getItem());
                    weightedIndices.removeIf(n -> n.equals(index));
                }
            }
        }else{
            recipeList.clear();
            recipeList.addAll(prevList);
        }

        prevSeed = seed;
    }


    protected void fillRecipes(World world) {
        TradeOffers.Factory[] factorys = Exchangemachinemod.TRADES;
        if (factorys != null) {
            TradeOfferList tradeOfferList = this.getOffers(world);
            this.fillRecipesFromPool(tradeOfferList, factorys, Exchangemachinemod.raritiesList, Exchangemachinemod.tradeAmounts, world);
        }
    }

    public void sendOffers(PlayerEntity player, Text test, int levelProgress, World world) {
        TradeOfferList list = getOffers(world);
        ExchangeBlockMerchant merchant = new ExchangeBlockMerchant(FabricEntityType.VILLAGER, world, list);
        merchant.setCustomer(player);
        OptionalInt optionalInt = player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> {
            return new MerchantScreenHandler(syncId, playerInventory, merchant);
        }, test));
        if (optionalInt.isPresent()) {
            TradeOfferList tradeOfferList = list;
            if (!tradeOfferList.isEmpty()) {
                player.sendTradeOffers(optionalInt.getAsInt(), tradeOfferList, levelProgress, 999, true, true);
            }
        }

    }
    public ExchangeBlock(Settings settings) {
        super(settings);
    }

    public void refreshOffers(World world) {
        this.offers = new TradeOfferList();
        this.fillRecipes(world);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        refreshOffers(world);
        if (!world.isClient) {
            sendOffers(player, Text.of("Daily Shop"), 5, world);
        }

        return ActionResult.success(world.isClient);
    }

    private static TradeOffers.Factory[] removeElementByPosition(TradeOffers.Factory[] arr, int position) {
        if (position < 0 || position >= arr.length) {
            return arr;
        }

        TradeOffers.Factory[] result = new TradeOffers.Factory[arr.length - 1];
        int index = 0;

        for (int i = 0; i < arr.length; i++) {
            if (i != position) {
                result[index] = arr[i];
                index++;
            }
        }

        return result;
    }
}


