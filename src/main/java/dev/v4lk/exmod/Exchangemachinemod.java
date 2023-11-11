package dev.v4lk.exmod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;

public class Exchangemachinemod implements ModInitializer {

    public static final Block EXCHANGE_BLOCK  = new ExchangeBlock(FabricBlockSettings.create().strength(1.0f).nonOpaque());
    public static final Item EXCHANGE_BLOCK_ITEM = new BlockItem(EXCHANGE_BLOCK, new FabricItemSettings());
    public static final List<DailyShopTradeOffer> SYNC_TRADES = new ArrayList<>();

    // Default Config
    public static String defaultConfig = "{\n" +
            "  \"trades-amount\": 4,\n" +
            "  \"trades\": {\n" +
            "    \"minecraft:cobblestone\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:glowstone\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 3,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1,\n" +
            "      \"color\": \"FFFFFF00\"\n" +
            "    },\n" +
            "    \"minecraft:wheat_seeds\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:melon_seeds\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:pumpkin_seeds\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:beetroot_seeds\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:torchflower_seeds\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:pitcher_pod\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 1,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:carrot\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:potato\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 64,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:oak_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:spruce_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:birch_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:jungle_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:acacia_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:dark_oak_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    },\n" +
            "    \"minecraft:cherry_sapling\": {\n" +
            "      \"currency\": \"minecraft:iron_ingot\",\n" +
            "      \"price\": 2,\n" +
            "      \"amount\": 16,\n" +
            "      \"rarity\": 1\n" +
            "    }\n" +
            "  }\n" +
            "}\n";

    public static File configFile = new File("config/daily-shop.json");

    public static int tradeAmounts;
    public static TradeOffers.Factory[] TRADES;
    public static ArrayList<Integer> raritiesList = new ArrayList<>();
    private static HashMap<Identifier, Item> wrongIdItemsCheck = new HashMap<>();

    public static class ExchangeFactory implements TradeOffers.Factory {
        Item item, currency;
        int price, amount;

        public ExchangeFactory(Item item, Item currency, int price, int amount) {
            this.item = item;
            this.currency = currency;
            this.price = price;
            this.amount = amount;
        }

        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            ItemStack currency = new ItemStack(this.currency, this.price);
            ItemStack item = new ItemStack(this.item, this.amount);
            return new TradeOffer(currency, item, 9999, 999, 0.05F);
        }
    }

    public static final Logger LOGGER = LoggerFactory.getLogger("exmod");

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(Exchangemachinemod::reload);

        Registry.register(Registries.BLOCK, new Identifier("exmod", "exchange_block"), EXCHANGE_BLOCK);
        Registry.register(Registries.ITEM, new Identifier("exmod", "exchange_block"), EXCHANGE_BLOCK_ITEM);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            content.add(EXCHANGE_BLOCK_ITEM);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reloadmachineconfig")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    reload(null);
                    context.getSource().sendMessage(Text.literal("Config reloaded."));
                    return 1;
                })));
        CommandRegistrationCallback.EVENT.register((dispatcher,registryAccess,registrationEnvironment)-> {
            dispatcher.register(
                    literal("dailyshop_log_wrong_ids").executes(context -> {
                        for (var key : wrongIdItemsCheck.keySet()) {
                            if (wrongIdItemsCheck.get(key).equals(Items.AIR)) {
                                LOGGER.error("WRONG ITEM IDENTIFIER %s".formatted(key));
                                if(context.getSource().isExecutedByPlayer()){
                                    context.getSource().getPlayer().sendMessage(Text.literal("WRONG ITEM IDENTIFIER %s".formatted(key)).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                                }
                            }
                        }
                        return 1;
                    })
            );
        });
        ServerPlayConnectionEvents.INIT.register(ConfigSynchronizer::server);
    }

    public static void reload(MinecraftServer server) {
        if (!(configFile.exists())) {
            try {
                configFile.createNewFile();

                FileWriter fileWriter = new FileWriter(configFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(defaultConfig);
                bufferedWriter.close();

                LOGGER.info("Default config has been written to the file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonObject json = JsonParser.parseReader(new FileReader(configFile)).getAsJsonObject();
            ArrayList<Integer> rarities = new ArrayList<>();

            wrongIdItemsCheck = new HashMap<>();
            tradeAmounts = json.get("trades-amount").getAsInt();
            List<TradeOffers.Factory> tradeOfferList = new ArrayList<>();
            JsonObject items = json.getAsJsonObject("trades");
            for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                String toShopItem = entry.getKey();
                int fromShopAmount = entry.getValue().getAsJsonObject().get("price").getAsInt();
                int toShopAmount = entry.getValue().getAsJsonObject().get("amount").getAsInt();
                String fromShopItem = entry.getValue().getAsJsonObject().get("currency").getAsString();
                int rarity = entry.getValue().getAsJsonObject().get("rarity").getAsInt();
                var el = entry.getValue().getAsJsonObject().get("color");
                String color = null;
                if(el != null){
                    color = el.getAsString();
                }
                rarities.add(rarity);
                var toShopId = new Identifier(toShopItem);
                var fromShopId = new Identifier(fromShopItem);
                SYNC_TRADES.add(new DailyShopTradeOffer(toShopId, fromShopId, toShopAmount, fromShopAmount, color));
                var toShop = Registries.ITEM.get(toShopId);
                var fromShop = Registries.ITEM.get(fromShopId);

                wrongIdItemsCheck.put(toShopId, toShop);
                wrongIdItemsCheck.put(fromShopId, fromShop);

                tradeOfferList.add(new ExchangeFactory(toShop, fromShop, fromShopAmount, toShopAmount));
            }



            TRADES = tradeOfferList.toArray(new TradeOffers.Factory[0]);
            raritiesList = rarities;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
