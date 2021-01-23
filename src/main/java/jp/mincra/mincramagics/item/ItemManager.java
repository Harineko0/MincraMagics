package jp.mincra.mincramagics.item;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import jp.mincra.mincramagics.MincraMagics;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ItemManager {

    private Map<String, ItemStack> itemStackMap = new HashMap<>();

    public void loadItem() {
//        JSONArray itemArray = MincraMagics.getJSONManager().getAllJSONArray("./plugins/MincraMagics/data/items/");

        String path = "";
        JSONArray itemArray = new JSONArray(MincraMagics.getJSONManager().getItemNode().toString());

        if (!itemArray.isEmpty()) {
            ChatUtil.sendConsoleMessage("アイテムの登録を開始します...");

            itemStackMap = new HashMap<>();;


            ItemMeta recipeIngredientMeta;
            List<String> recipeIngredientLoreList;

            JSONObject itemObject;
            //            JSONObject itemDisplayObject;
//            JSONArray itemEnchantmentsArray;
//            JSONObject itemEachEnchant;
//                    JSONArray itemAttributeModifiersArray;
//                    JSONObject itemEachAttribute;
//            JSONObject itemMincraMagicsObject = null;
//            JSONObject itemRecipeObject = null;
//            JSONArray itemRecipeShapeArray;

            NBTItem nbtItem;
            NBTCompound nbtDisplay;
            NBTList nbtLore;
            NBTCompoundList nbtEnchantments;
            NBTCompound nbtEachEnchant;
            NBTCompound nbtMincraMagics;

            int item_amount = 0;
            int recipe_amount = 0;

            for (int i = 0, len = itemArray.length(); i < len; i++) {

                if (itemArray.get(i) instanceof JSONObject) { //配列内の値がJSONObjectかどうか

                    //初期化する変数
                    ItemStack item;
                    Material material = null;
                    String mcr_id;

                    itemObject = itemArray.getJSONObject(i);

                    //id-------------------------------------↓
                    if (itemObject.has("id")) {
                        if (itemObject.get("id") instanceof String) {
                            material = Material.getMaterial(itemObject.getString("id").toUpperCase());
                            if (material != null) {
                                item = new ItemStack(Objects.requireNonNull(material));
                            } else {
                                ChatUtil.sendConsoleMessage("エラー: " + path + "の" + i + "番目のアイテムIDが無効です。 id: " + itemObject.getString("id"));
                                continue;
                            }
                        } else {
                            ChatUtil.sendConsoleMessage("エラー: " + path + "の" + i + "番目のアイテムIDがString型である必要があります。 id: " + itemObject.get("id"));
                            continue;
                        }
                    } else {
                        ChatUtil.sendConsoleMessage("エラー: " + path + "の" + i + "番目のアイテムIDが未指定です。");
                        continue;
                    }
                    //---------------------------------------↑

                    //mcr_id---------------------------------↓
                    if (itemObject.has("mcr_id")) {
                        if (itemObject.get("mcr_id") instanceof String) {
                            mcr_id = itemObject.getString("mcr_id");

                        } else {
                            ChatUtil.sendConsoleMessage("エラー: " + path + "の" + i + "番目のMCR_IDがString型である必要があります。 id: " + itemObject.get("mcr_id"));
                            continue;
                        }
                    } else {
                        ChatUtil.sendConsoleMessage("エラー: " + path + "の"+i+"番目のMCR_IDが未指定です。");
                        continue;
                    }
                    //---------------------------------------↑

                    //nbt------------------------------------↓
                    nbtItem = new NBTItem(item);

                    if (itemObject.has("nbt") && itemObject.get("nbt") instanceof JSONObject) {

                        loadNBT(nbtItem, itemObject.getJSONObject("nbt"));
                    }
                    //---------------------------------------↑


                } else { //配列の値がJSONObject以外なら飛ばす
                    continue;
                }
            }
        } else {
            ChatUtil.sendConsoleMessage("アイテムファイルが存在しません。");
        }
    }

    //NBTをロードする
    public void loadNBT(NBTItem nbtItem, JSONObject itemNBTObject) {

        Iterator<String> keys = itemNBTObject.keys(); //キー探索

        while (keys.hasNext()) {
            ChatUtil.sendConsoleMessage("key: "+ keys.next() + itemNBTObject.get(keys.next()));
            if(itemNBTObject.get(keys.next()) instanceof JSONObject) {
                loadNBT(nbtItem, itemNBTObject.getJSONObject(keys.next()));

            } else if (itemNBTObject.get(keys.next()) instanceof JSONArray) {
                for (int i = 0, len = itemNBTObject.getJSONArray(keys.next()).length(); i<len; i++) {
                    if (itemNBTObject.getJSONArray(keys.next()).get(i) instanceof JSONObject) {
                        loadNBT(nbtItem, itemNBTObject.getJSONArray(keys.next()).getJSONObject(i));
                    } else {
                        loadNBTValue(nbtItem, keys.next(), itemNBTObject.getJSONArray(keys.next()).get(i));
                    }
                }
            } else {
                loadNBTValue(nbtItem, keys.next(), itemNBTObject.get(keys.next()));
            }

        }
    }

    //NBTのうちint, boolean, stringをセットする
    public void loadNBTValue(NBTItem nbtItem, String key, Object object) {
        ChatUtil.sendConsoleMessage("loadNBTValue: "+key + object);
        if (object instanceof Integer) {
            nbtItem.setInteger(key, (Integer) object);
        } else if (object instanceof String) {
            nbtItem.setString(key, (String) object);
        } else if (object instanceof Boolean) {
            nbtItem.setBoolean(key, (Boolean) object);
        }
    }

    public void registerItem() {

        itemStackMap = new HashMap<>();;

        ChatUtil.sendConsoleMessage("アイテムの登録を開始します...");

        JSONArray itemArray = new JSONArray(MincraMagics.getJSONManager().getItemNode().toString());

        ItemStack item;
        Material material = null;
        String mcr_id;
        ItemMeta recipeIngredientMeta;
        List<String> recipeIngredientLoreList;

        JSONObject itemObject;
        JSONObject itemNBTObject;
        JSONObject itemDisplayObject;
        JSONArray itemEnchantmentsArray;
        JSONObject itemEachEnchant;
//                    JSONArray itemAttributeModifiersArray;
//                    JSONObject itemEachAttribute;
        JSONObject itemMincraMagicsObject = null;
        JSONObject itemRecipeObject = null;
        JSONArray itemRecipeShapeArray;

        NBTItem nbtItem;
        NBTCompound nbtDisplay;
        NBTList nbtLore;
        NBTCompoundList nbtEnchantments;
        NBTCompound nbtEachEnchant;
        NBTCompound nbtMincraMagics;
//                    NBTCompoundList nbtAttributeModifiers;
//                    NBTCompound nbtEachAttribute = null;


        int item_amount = 0;
        int recipe_amount = 0;

        for(int i=0, len=itemArray.length(); i<len; i++) {
            itemObject = itemArray.getJSONObject(i);

            //id-------------------------------------↓
            if (itemObject.has("id")) {
                if (itemObject.get("id") instanceof String) {
                    material = material.getMaterial(itemObject.getString("id").toUpperCase());
                    if (material != null) {
                        item = new ItemStack(Objects.requireNonNull(material));
                    } else {
                        ChatUtil.sendConsoleMessage("エラー: /data/items.jsonの" + i + "番目のアイテムIDが無効です。 id: " + itemObject.getString("id"));
                        continue;
                    }
                } else {
                    ChatUtil.sendConsoleMessage("エラー: /data/items.jsonの" + i + "番目のアイテムIDがString型である必要があります。 id: " + itemObject.get("id"));
                    continue;
                }
            } else {
                ChatUtil.sendConsoleMessage("エラー: /data/items.jsonの" + i + "番目のアイテムIDが未指定です。");
                continue;
            }
            //---------------------------------------↑

            //mcr_id---------------------------------↓
            if (itemObject.has("mcr_id")) {
                if (itemObject.get("mcr_id") instanceof String) {
                    mcr_id = itemObject.getString("mcr_id");

                } else {
                    ChatUtil.sendConsoleMessage("エラー: /data/items.jsonの" + i + "番目のMCR_IDがString型である必要があります。 id: " + itemObject.get("mcr_id"));
                    continue;
                }
            } else {
                ChatUtil.sendConsoleMessage("エラー: /data/items.jsonの" + i + "番目のMCR_IDが未指定です。");
                continue;
            }
            //---------------------------------------↑

            //-nbt----------------------------------↓↓
            nbtItem = new NBTItem(item);

            //-mcr_id------------------------------↓↓↓
            nbtMincraMagics = nbtItem.addCompound("MincraMagics");
            nbtMincraMagics.setString("id", mcr_id);
            //-------------------------------------↑↑↑

            if (itemObject.has("nbt") && itemObject.get("nbt") instanceof JSONObject) {
                itemNBTObject = itemObject.getJSONObject("nbt");

                //-MincraMagics------------------------↓↓↓
//            itemMincraMagicsObject = itemNBTObject.getJSONObject("MincraMagics");
                //id
//            if (itemMincraMagicsObject.has("id") && itemMincraMagicsObject.get("id") instanceof String) {
//                nbtMincraMagics.setString("id", itemMincraMagicsObject.getString("id"));
//            }
                //-------------------------------------↑↑↑

                //-display-----------------------------↓↓↓
                itemDisplayObject = itemNBTObject.getJSONObject("display");
                nbtDisplay = nbtItem.addCompound("display");
                //Name
                if (itemDisplayObject.has("Name") && itemDisplayObject.get("Name") instanceof String) {
                    nbtDisplay.setString("Name", itemDisplayObject.getString("Name"));
                }
                //Lore
                if (itemDisplayObject.has("Lore") && itemDisplayObject.get("Lore") instanceof JSONArray) {
                    nbtLore = nbtDisplay.getStringList("Lore");
                    for (int j = 0, loreLen = itemDisplayObject.getJSONArray("Lore").length(); j < loreLen; j++) {
                        nbtLore.add(itemDisplayObject.getJSONArray("Lore").get(j));
                    }
                }
                //-------------------------------------↑↑↑

                //CustomModelData
                if (itemNBTObject.has("CustomModelData") && itemNBTObject.get("CustomModelData") instanceof Integer) {
                    nbtItem.setInteger("CustomModelData", itemNBTObject.getInt("CustomModelData"));
                }

                //HideFlags
                if (itemNBTObject.has("HideFlags") && itemNBTObject.get("HideFlags") instanceof Integer) {
                    nbtItem.setInteger("HideFlags", itemNBTObject.getInt("HideFlags"));
                }

                //-Enchantments------------------------↓↓↓
                if (itemNBTObject.has("Enchantments") && itemNBTObject.get("Enchantments") instanceof JSONArray) {
                    itemEnchantmentsArray = itemNBTObject.getJSONArray("Enchantments");
                    nbtEnchantments = nbtItem.getCompoundList("Enchantments");
                    for (int j = 0, enLen = itemEnchantmentsArray.length(); j < enLen; j++) {
                        itemEachEnchant = ((JSONObject) itemEnchantmentsArray.get(j));
                        nbtEachEnchant = nbtItem.addCompound("Enchantments");

                        if (itemEachEnchant.has("id") && itemEachEnchant.get("id") instanceof String) {
                            nbtEachEnchant.setString("id", itemEachEnchant.getString("id"));
                        }

                        if (itemEachEnchant.has("lvl") && itemEachEnchant.get("lvl") instanceof Integer) {
                            nbtEachEnchant.setInteger("lvl", itemEachEnchant.getInt("lvl"));
                        }

                        nbtEnchantments.addCompound(nbtEachEnchant);
                    }
                }
                //-------------------------------------↑↑↑

                //-AttributeModifiers------------------↓↓↓
//                        if (itemNBTObject.has("AttributeModifiers") && itemNBTObject.get("AttributeModifiers") instanceof JSONArray) {
//                            itemAttributeModifiersArray = itemNBTObject.getJSONArray("AttributeModifiers");
//                            nbtAttributeModifiers = nbtItem.getCompoundList("AttributeModifiers");
//                            for (int j = 0, atrLen = itemAttributeModifiersArray.length(); j < atrLen; j++) {
//                                itemEachAttribute = ((JSONObject) itemAttributeModifiersArray.get(j));
//                                nbtEachAttribute = nbtItem.addCompound("AttributeModifiers");
//
//                                if (itemEachAttribute.has("AttributeName") && itemEachAttribute.get("AttributeName") instanceof String){
//                                    nbtEachAttribute.setString("AttributeName",itemEachAttribute.getString("AttributeName"));
//                                }
//
//                                if (itemEachAttribute.has("Name") && itemEachAttribute.get("Name") instanceof String){
//                                    nbtEachAttribute.setString("Name",itemEachAttribute.getString("Name"));
//                                }
//
//                                if (itemEachAttribute.has("Amount") && itemEachAttribute.get("Amount") instanceof Integer){
//                                    nbtEachAttribute.setInteger("Amount",itemEachAttribute.getInt("Amount"));
//                                }
//
//                                if (itemEachAttribute.has("Operation") && itemEachAttribute.get("Operation") instanceof Integer){
//                                    nbtEachAttribute.setInteger("Operation",itemEachAttribute.getInt("Operation"));
//                                }
//
//                                nbtAttributeModifiers.addCompound(nbtEachAttribute);
//                            }
//                        }
                //-------------------------------------↑↑↑
            }
            //--------------------------------------↑↑


            item = nbtItem.getItem();

            item_amount = item_amount + 1;
            itemStackMap.put(mcr_id, item);
        }

        ItemStack ingredient;
        ShapedRecipe shapedRecipe;
        NamespacedKey namespacedKey;
        for(int i=0, len=itemArray.length(); i<len; i++) {
            itemObject = itemArray.getJSONObject(i);
            mcr_id = itemObject.getString("mcr_id");

            //レシピ登録
            NamespacedKey key = new NamespacedKey(MincraMagics.getInstance(), mcr_id);
            //一度登録解除
            Bukkit.removeRecipe(key);

            if (itemObject.has("craftable") && itemObject.getBoolean("craftable")) {
                if (itemObject.has("recipe"))
                    itemRecipeObject = itemObject.getJSONObject("recipe");
                    if (itemRecipeObject.has("shape")
                        && itemRecipeObject.get("shape") instanceof JSONArray
                        && ((JSONArray) itemRecipeObject.get("shape")).length() == 3) {
                    itemRecipeShapeArray = (JSONArray) itemRecipeObject.get("shape");

                    ShapedRecipe recipe = new ShapedRecipe(key, getItem(itemObject.getString("mcr_id")));

                    recipe.shape(itemRecipeShapeArray.get(0).toString(),
                            itemRecipeShapeArray.get(1).toString(),
                            itemRecipeShapeArray.get(2).toString());

                        if (itemRecipeObject.getJSONObject("ingredient").has("a")) {
                            recipe.setIngredient('a', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("a").toUpperCase())));
//                            material = material.getMaterial(itemObject.getString("id").toUpperCase())
//                            ingredient = new ItemStack(material);
//                            recipeIngredientMeta = getItem(itemRecipeObject.getJSONObject("ingredient").getJSONObject("a").getString("mcr_id")).getItemMeta();
//                            ingredient.setItemMeta(recipeIngredientMeta);
//                            recipe.setIngredient('a', ingredient);
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("b")) {
                            recipe.setIngredient('b', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("b").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("c")) {
                            recipe.setIngredient('c', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("c").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("d")) {
                            recipe.setIngredient('d', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("d").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("e")) {
                            recipe.setIngredient('e', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("e").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("f")) {
                            recipe.setIngredient('f', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("f").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("g")) {
                            recipe.setIngredient('g', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("g").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("h")) {
                            recipe.setIngredient('h', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("h").toUpperCase())));
                        }
                        if (itemRecipeObject.getJSONObject("ingredient").has("i")) {
                            recipe.setIngredient('i', Objects.requireNonNull(material.getMaterial(itemRecipeObject.getJSONObject("ingredient").getString("i").toUpperCase())));
                        }

                        Bukkit.addRecipe(recipe);
                        recipe_amount = recipe_amount + 1;

                } else {
                    ChatUtil.sendConsoleMessage("エラー: /data/items.jsonのレシピが不適切です。 mcr_id: " + mcr_id);

                }
            }
        }

        ChatUtil.sendConsoleMessage("アイテムの登録が完了しました。 ");
        ChatUtil.sendConsoleMessage(item_amount + "個のアイテムと" + recipe_amount + "個のレシピを登録しました。");
    }

    public ItemStack getItem(String mcr_id) {
        return itemStackMap.get(mcr_id);
    }
}