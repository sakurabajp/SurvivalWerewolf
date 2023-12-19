package net.cherryleaves.survivalwerewolf;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;

public final class SurvivalWerewolf extends JavaPlugin implements Listener {

    public BukkitRunnable chatFlowTask;
    public BukkitRunnable TimerTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        getLogger().info(ChatColor.GREEN + "ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー");
        getLogger().info(ChatColor.AQUA + "Minecraft Survival Werewolf plugin activated!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info(ChatColor.GREEN + "このプラグインは、こるんちゃんねる樣主催の「マインクラフトサバイバル人狼」");
        getLogger().info(ChatColor.GREEN + "を完全プラグインのみで遊べるように勝手に作ったものです");
        getLogger().info("");
        getLogger().info(ChatColor.GREEN + "java素人の私が書いたコードですので、");
        getLogger().info(ChatColor.GREEN + "修正とかあれば是非Githubにプルリク投げて下さい(ただし見るとは言ってない)");
        getLogger().info("");
        getLogger().info(ChatColor.GREEN + "ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー");
        Objects.requireNonNull(getCommand("startgame")).setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stopChatFlowTask();
        super.onDisable();
    }

    // 人狼・狂人の数の変数作成
    int BeforeWolfPlayerCount = 1;
    int BeforeMadmanPlayerCount = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("startgame")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            Player AdminPlayer = ((Player) sender).getPlayer();
            // GUiを開く
            assert AdminPlayer != null;
            AdminPlayer.playSound(AdminPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7f, 1.0f);
            openGUI(Objects.requireNonNull(AdminPlayer));
        }
        if (command.getName().equalsIgnoreCase("stopgame")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            LocateChatEnd();
        }
        return false;
    }

    @EventHandler
    public void onPlayerClickInventory(PlayerJoinEvent Player){
        Player.getPlayer().setGameMode(GameMode.ADVENTURE);
        Player.setJoinMessage(ChatColor.YELLOW + Player.getPlayer().getName() + "さんがマイクラサバイバル人狼のサーバーに参加しました！");
    }

    public void openGUI(Player AdminPlayer) {
        // GUI表示
        Inventory StartGUI = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "プレイヤー人数と役職数の確認");
        // 人狼数表示アイテム
        ItemStack WolfPlayerCountItem = new ItemStack(Material.FIRE_CORAL);
        ItemMeta WolfPlayerCountItemMeta = WolfPlayerCountItem.getItemMeta();
        Objects.requireNonNull(WolfPlayerCountItemMeta).setDisplayName(ChatColor.DARK_AQUA + "現在の設定では人狼の数は" + ChatColor.GOLD + BeforeWolfPlayerCount + ChatColor.DARK_AQUA + "人です");
        WolfPlayerCountItem.setItemMeta(WolfPlayerCountItemMeta);
        StartGUI.setItem(2, WolfPlayerCountItem);
        // 人狼数減らす
        ItemStack WolfCountDownItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta WolfCountDownItemMeta = WolfCountDownItem.getItemMeta();
        Objects.requireNonNull(WolfCountDownItemMeta).setDisplayName(ChatColor.RED + "クリックで人狼の数を減らす");
        WolfCountDownItem.setItemMeta(WolfCountDownItemMeta);
        StartGUI.setItem(1, WolfCountDownItem);
        // 人狼数増やす
        ItemStack WolfCountUpItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta WolfCountUpItemMeta = WolfCountUpItem.getItemMeta();
        Objects.requireNonNull(WolfCountUpItemMeta).setDisplayName(ChatColor.BLUE + "クリックで人狼の数を増やす");
        WolfCountUpItem.setItemMeta(WolfCountUpItemMeta);
        StartGUI.setItem(3, WolfCountUpItem);
        // 狂人数表示アイテム
        ItemStack MadmanPlayerCountItem = new ItemStack(Material.BUBBLE_CORAL);
        ItemMeta MadmanPlayerCountItemMeta = MadmanPlayerCountItem.getItemMeta();
        Objects.requireNonNull(MadmanPlayerCountItemMeta).setDisplayName(ChatColor.DARK_AQUA + "現在の設定では狂人の数は" + ChatColor.GOLD + BeforeMadmanPlayerCount + ChatColor.DARK_AQUA + "人です");
        MadmanPlayerCountItem.setItemMeta(MadmanPlayerCountItemMeta);
        StartGUI.setItem(6, MadmanPlayerCountItem);
        // 狂人数減らす
        ItemStack MadmanCountDownItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta MadmanCountDownItemMeta = MadmanCountDownItem.getItemMeta();
        Objects.requireNonNull(MadmanCountDownItemMeta).setDisplayName(ChatColor.RED + "クリックで狂人の数を減らす");
        MadmanCountDownItem.setItemMeta(MadmanCountDownItemMeta);
        StartGUI.setItem(5, MadmanCountDownItem);
        // 狂人数増やす
        ItemStack MadmanCountUpItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta MadmanCountUpItemMeta = MadmanCountUpItem.getItemMeta();
        Objects.requireNonNull(MadmanCountUpItemMeta).setDisplayName(ChatColor.BLUE + "クリックで狂人の数を増やす");
        MadmanCountUpItem.setItemMeta(MadmanCountUpItemMeta);
        StartGUI.setItem(7, MadmanCountUpItem);
        //ゲームスタートアイテム
        ItemStack GameStartItem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta GameStartItemMeta = GameStartItem.getItemMeta();
        Objects.requireNonNull(GameStartItemMeta).setDisplayName(ChatColor.YELLOW + "ゲームスタート！");
        GameStartItem.setItemMeta(GameStartItemMeta);
        StartGUI.setItem(8, GameStartItem);

        // プレイヤーに上まで作ってきたGUIを表示する
        assert AdminPlayer != null;
        AdminPlayer.openInventory(StartGUI);
    }

    ScoreboardManager managerW = Bukkit.getScoreboardManager();
    ScoreboardManager managerV = Bukkit.getScoreboardManager();
    ScoreboardManager managerM = Bukkit.getScoreboardManager();
    ScoreboardManager ManagerT = Bukkit.getScoreboardManager();

    Scoreboard scoreboardW = Objects.requireNonNull(managerW).getMainScoreboard();
    Scoreboard scoreboardV = Objects.requireNonNull(managerV).getMainScoreboard();
    Scoreboard scoreboardM = Objects.requireNonNull(managerM).getMainScoreboard();
    Scoreboard scoreboardTimer = ManagerT.getMainScoreboard();

    public void GameStart(){
        List<Player> Players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (scoreboardW.getTeam("wolf") != null) {
            Objects.requireNonNull(scoreboardW.getTeam("wolf")).unregister();
        }
        if (scoreboardV.getTeam("villager") != null) {
            Objects.requireNonNull(scoreboardV.getTeam("villager")).unregister();
        }
        if (scoreboardM.getTeam("madman") != null) {
            Objects.requireNonNull(scoreboardM.getTeam("madman")).unregister();
        }
        Team teamW = scoreboardW.registerNewTeam("wolf");
        Team teamM = scoreboardM.registerNewTeam("madman");
        Team teamV = scoreboardV.registerNewTeam("villager");
        teamM.setSuffix("[←この人は狂人です]");
        teamW.setSuffix("[←この人は人狼です]");
        teamV.setSuffix("[←この人は村人です]");
        for (Player playerACC : Bukkit.getOnlinePlayers()) {
            playerACC.sendMessage("貴方を村人チームに追加しました");
            teamV.addPlayer(playerACC);
        }
        for (int i = BeforeWolfPlayerCount; i > 0; i += -1) {
            Random random = new Random();
            Player WolfTeamPlayers = Players.get(random.nextInt(Players.size()));
            if (teamW.hasEntry(WolfTeamPlayers.getName())){
                WolfTeamPlayers.sendMessage("貴方はすでに人狼チームに所属しているため再抽選が行われます");
                return;
            }
            teamW.addPlayer(WolfTeamPlayers);
            WolfTeamPlayers.sendMessage("貴方は人狼に選ばれました");
        }
        for (int i = BeforeMadmanPlayerCount; i > 0; i += -1) {
            Random random = new Random();
            Player MadmanTeamPlayers = Players.get(random.nextInt(Players.size()));
            if (teamM.hasEntry(MadmanTeamPlayers.getName())){
                MadmanTeamPlayers.sendMessage("貴方はすでに狂人チームに所属しているため再抽選が行われます");
                return;
            }
            else if (teamW.hasEntry(MadmanTeamPlayers.getName())){
                MadmanTeamPlayers.sendMessage("貴方はすでに狂人チームに所属しているため再抽選が行われます");
                return;
            }
            teamM.addPlayer(MadmanTeamPlayers);
            MadmanTeamPlayers.sendMessage("貴方は狂人に選ばれました");
        }
        for (Player playerALL5 : Bukkit.getOnlinePlayers()) {
            playerALL5.setGameMode(GameMode.SURVIVAL);
            playerALL5.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 80, true, false));
            playerALL5.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 80, true, false));
            playerALL5.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 5, true, false));
            playerALL5.playSound(playerALL5.getLocation(), Sound.ENTITY_WITHER_SPAWN , 0.5f, 1.0f);
            playerALL5.getInventory().clear();
            sendTitle(playerALL5, "&6ゲームスタート！", "", 10, 40, 10);
            playerALL5.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "-----------------------------------------------------");
            playerALL5.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "ゲームスタート！");
            playerALL5.sendMessage("");
            playerALL5.sendMessage(ChatColor.AQUA + "制限時間は" + ChatColor.RESET + ChatColor.GOLD + "3時間" + ChatColor.RESET + ChatColor.AQUA + "です");
            playerALL5.sendMessage("");
            if (teamV.hasEntry(playerALL5.getName())) {
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.GREEN + "村人陣営" + ChatColor.DARK_AQUA + "です");
            }
            if (teamW.hasEntry(playerALL5.getName())) {
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.RED + "人狼陣営" + ChatColor.DARK_AQUA + "です");
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "仲間は" + ChatColor.RED + teamW.getEntries() + ChatColor.DARK_AQUA + "です");
            }
            if (teamM.hasEntry(playerALL5.getName())) {
                playerALL5.sendMessage(ChatColor.DARK_AQUA + "あなたは" + ChatColor.LIGHT_PURPLE + "狂人陣営" + ChatColor.DARK_AQUA + "です");
            }
            playerALL5.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "-----------------------------------------------------");
            for (Player playerAdminTAG1 : Bukkit.getOnlinePlayers()) {
                if (playerALL5.getScoreboardTags().contains("Admin1")) {
                    playerAdminTAG1.teleport(playerALL5.getLocation());
                }
            }
            playerALL5.removeScoreboardTag("Admin1");
        }
        startTimer();
        LocateChat();
    }

    private void updateScoreboard(int timeRemaining) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        Objective objective = scoreboard.getObjective("timer");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("timer", "dummy", "Timer");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        Score score = objective.getScore("Time Left");
        score.setScore(timeRemaining);
    }

    private void startTimer() {
        TimerTask = new BukkitRunnable() {
            int timeRemaining = 60 * 60 * 3;
            @Override
            public void run() {
                if (timeRemaining <= 0) {
                    cancel(); // タイマー停止
                    return;
                }
                updateScoreboard(timeRemaining);
                timeRemaining--;
            }
        };
        TimerTask.runTaskTimer(this, 0, 20); // 1秒ごとにタイマーを更新
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {

        if (event.getWhoClicked() instanceof Player) {
            Player GUIClickedPlayer = (Player) event.getWhoClicked();
            // クリックされたGUIを取得する
            Inventory clickedInventory = event.getClickedInventory();
            /*String ClickedItemName = event.getClick().name();*/
            String ClickedItemName = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName();
            if (clickedInventory != null && event.getView().getTitle().equals(ChatColor.DARK_AQUA + "プレイヤー人数と役職数の確認")) {
                GUIClickedPlayer.addScoreboardTag("Admin1");
                // クリックされたアイテムを取得する
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
                    if (ClickedItemName.equals(ChatColor.RED + "クリックで人狼の数を減らす")) {
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                        if (BeforeWolfPlayerCount > 1) {
                            BeforeWolfPlayerCount += (-1);
                        }
                        else{
                            GUIClickedPlayer.sendMessage(ChatColor.DARK_RED + "人狼の人数を1人未満にすることは出来ません。");
                            GUIClickedPlayer.playSound(Objects.requireNonNull(GUIClickedPlayer).getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.1f);
                        }
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    } else if (ClickedItemName.equals(ChatColor.RED + "クリックで狂人の数を減らす")) {
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                        if (BeforeMadmanPlayerCount > 0) {
                            BeforeMadmanPlayerCount += (-1);
                        }
                        else{
                            GUIClickedPlayer.sendMessage(ChatColor.DARK_RED + "狂人の人数を0人未満にすることは出来ません。");
                            GUIClickedPlayer.playSound(Objects.requireNonNull(GUIClickedPlayer).getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.1f);
                        }
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    }
                }
                else if (clickedItem != null && clickedItem.getType() == Material.BLUE_STAINED_GLASS_PANE){
                     if (ClickedItemName.equals(ChatColor.BLUE + "クリックで人狼の数を増やす")) {
                         BeforeWolfPlayerCount += (1);
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.2f);
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    }
                    else if (ClickedItemName.equals(ChatColor.BLUE + "クリックで狂人の数を増やす")) {
                         GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.2f);
                         BeforeMadmanPlayerCount += (1);
                         openGUI(Objects.requireNonNull(GUIClickedPlayer));
                     }
                }
                else if (clickedItem != null && clickedItem.getType() == Material.TOTEM_OF_UNDYING){
                    GameStart();
                }
                else{
                    GUIClickedPlayer.sendMessage(ChatColor.AQUA + "サンゴに触らないで！！");
                    GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.ENTITY_RABBIT_DEATH, 1.0f, 1.2f);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String originalDeathMessage = event.getDeathMessage();
        // 死亡ログのメッセージを変更する
        String modifiedDeathMessage = "誰かが死亡しました";
        // 新しい死亡ログメッセージを設定する
        event.setDeathMessage(modifiedDeathMessage);
        // オリジナルの死亡ログをコンソールに表示する
        getLogger().info(originalDeathMessage);
    }

    public void LocateChat(){
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        chatFlowTask = new BukkitRunnable() {
            @Override
            public void run() {
                Random random = new Random();
                Player randomPlayer = players.get(random.nextInt(players.size()));
                String playerName = randomPlayer.getName();
                if (randomPlayer.getGameMode() == GameMode.SPECTATOR) {
                    return;
                }
                int x = randomPlayer.getLocation().getBlockX();
                int y = randomPlayer.getLocation().getBlockY();
                int z = randomPlayer.getLocation().getBlockZ();
                Location location = randomPlayer.getLocation();
                String dimensionName = Objects.requireNonNull(location.getWorld()).getEnvironment().name();
                for (Player playerA : Bukkit.getOnlinePlayers()) {
                    playerA.playSound(playerA.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
                }
                Bukkit.broadcastMessage(ChatColor.YELLOW + "位置情報公開の時間です。");
                Bukkit.broadcastMessage(ChatColor.RED + playerName + ChatColor.GREEN + " の座標は、" + ChatColor.RED + "X=" + x + ", Y=" + y + ", Z=" + z + ChatColor.GREEN + ", \nディメンションは" + ChatColor.RED + dimensionName + ChatColor.GREEN + "です");
            }
        };
        chatFlowTask.runTaskTimer(this, 0, 6000); // 6000 ticks = 5 minutes
        }

    public void LocateChatEnd(){
        if (chatFlowTask != null) {
            chatFlowTask.cancel();
            chatFlowTask = null;
        }
    }

    private void stopChatFlowTask() {
        if (chatFlowTask != null) {
            chatFlowTask.cancel();
            chatFlowTask = null;
        }
    }
}