package net.cherryleaves.survivalwerewolf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class Item implements Listener {

    public static void getRecipe(JavaPlugin plugin){
        // ここからアイテムB
        ItemStack itemB = new ItemStack(Material.KNOWLEDGE_BOOK, 1); // アイテムを作る
        ItemMeta metaB = itemB.getItemMeta(); // metaを登録
        Objects.requireNonNull(metaB).setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "エンダーパール入手本");
        itemB.setItemMeta(metaB);
        NamespacedKey keyB = new NamespacedKey(Objects.requireNonNull(plugin), "EnderP");
        ShapedRecipe recipeB = new ShapedRecipe(keyB, itemB); // レシピオブジェクトの作成
        // レシピの形状を設定
        recipeB.shape("121", "232", "454");
        recipeB.setIngredient('1', Material.STONE_BRICKS);
        recipeB.setIngredient('2', Material.CRYING_OBSIDIAN);
        recipeB.setIngredient('3', Material.ENDER_PEARL);
        recipeB.setIngredient('4', Material.GOLD_NUGGET);
        recipeB.setIngredient('5', Material.SOUL_LANTERN);
        // レシピを登録
        plugin.getServer().addRecipe(recipeB);

        // アイテムC
        ItemStack itemC = new ItemStack(Material.KNOWLEDGE_BOOK, 1); // アイテムを作る
        ItemMeta metaC = itemC.getItemMeta(); // metaを登録
        Objects.requireNonNull(metaC).setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "復活本");
        itemC.setItemMeta(metaC);
        NamespacedKey keyC = new NamespacedKey(Objects.requireNonNull(plugin), "FukkatsuBook");
        ShapedRecipe recipeC = new ShapedRecipe(keyC, itemC); // レシピオブジェクトの作成
        // レシピの形状を設定
        recipeC.shape("666", "787", "909");
        recipeC.setIngredient('6', Material.GOLD_INGOT);
        recipeC.setIngredient('7', Material.GLASS_PANE);
        recipeC.setIngredient('8', Material.DIAMOND);
        recipeC.setIngredient('9', Material.IRON_BLOCK);
        recipeC.setIngredient('0', Material.REDSTONE);
        // レシピを登録
        Bukkit.getServer().addRecipe(recipeC);
    }

    public static void getRecipeD(JavaPlugin plugin){
        ItemStack itemD = new ItemStack(Material.KNOWLEDGE_BOOK, 1); // アイテムを作る
        ItemMeta metaD = itemD.getItemMeta(); // metaを登録
        Objects.requireNonNull(metaD).setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "盲目本");
        itemD.setItemMeta(metaD);

        NamespacedKey key = new NamespacedKey(Objects.requireNonNull(plugin), "WolfBook");
        ShapedRecipe recipeD = new ShapedRecipe(key, itemD); // レシピオブジェクトの作成
        // レシピの形状を設定
        recipeD.shape("AAA", "BCB", "DDD");
        recipeD.setIngredient('A', Material.NETHER_BRICKS); // 材料 A ネザーレンガ
        recipeD.setIngredient('B', Material.NETHER_BRICK_FENCE); // 材料 B ネザーレンガフェンス
        recipeD.setIngredient('C', Material.LANTERN); // 材料 C ランタン
        recipeD.setIngredient('D', Material.NETHERRACK); // 材料 C ネザーラック
        // レシピを登録
        Bukkit.getServer().addRecipe(recipeD);
    }

    public static void getRecipeE(JavaPlugin plugin){
        ItemStack itemE = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1); // アイテムを作る
        ItemMeta metaE = itemE.getItemMeta(); // metaを登録
        itemE.setItemMeta(metaE);

        NamespacedKey key = new NamespacedKey(Objects.requireNonNull(plugin), "Gapple");
        ShapedRecipe recipeE = new ShapedRecipe(key, itemE); // レシピオブジェクトの作成
        // レシピの形状を設定
        recipeE.shape("AAA", "ABA", "AAA");
        recipeE.setIngredient('A', Material.GOLD_BLOCK); // 材料 A 金ブロック
        recipeE.setIngredient('B', Material.APPLE); // 材料 B りんご
        // レシピを登録
        Bukkit.getServer().addRecipe(recipeE);
    }

    public static void getRecipeF(JavaPlugin plugin){
        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK, 1); // アイテムを作る
        ItemMeta meta = item.getItemMeta(); // metaを登録
        Objects.requireNonNull(meta).setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "透明化");
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Objects.requireNonNull(plugin), "Invisible");
        ShapedRecipe recipeF = new ShapedRecipe(key, item); // レシピオブジェクトの作成
        // レシピの形状を設定
        recipeF.shape("ABA", "BCB", "ABA");
        recipeF.setIngredient('A', Material.GLASS); // 材料 A クウォーツ
        recipeF.setIngredient('B', Material.GLASS_PANE); // 材料 B ダイヤモンド
        recipeF.setIngredient('C', Material.SUGAR); // 材料 C 空気
        // レシピを登録
        Bukkit.getServer().addRecipe(recipeF);
    }
}
