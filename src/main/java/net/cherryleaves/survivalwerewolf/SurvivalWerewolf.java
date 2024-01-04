package net.cherryleaves.survivalwerewolf;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
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
        getServer().getPluginManager().registerEvents(new Item(), this);
        getServer().getPluginManager().registerEvents(new ItemSystem(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LocateChatEnd();
        MainTimerEnd();
        super.onDisable();
    }

    // 人狼・狂人の数の変数作成
    int BeforeWolfPlayerCount = 1;
    int BeforeMadmanPlayerCount = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("startgame")) {
            CountReset();
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
            for (Player playerOI : Bukkit.getOnlinePlayers()){
                playerOI.playSound(playerOI.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, (float) 1 / 5, 1);
                playerOI.sendMessage(ChatColor.DARK_RED + "ゲームを強制終了させました");
            }
            LocateChatEnd();
            MainTimerEnd();
            Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
            scoreboard.resetScores("MainTimer");
        }
        if (command.getName().equalsIgnoreCase("resetgame")) {
            if (!(sender instanceof Player) || !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            for (Player playerOI : Bukkit.getOnlinePlayers()){
                playerOI.playSound(playerOI.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, (float) 1 / 5, 1);
                playerOI.sendMessage(ChatColor.DARK_RED + "ゲームをリセットします");
            }
            LocateChatEnd();
            MainTimerEnd();
            Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
            scoreboard.resetScores("MainTimer");
            CountReset();
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent Player){
        Player.setJoinMessage(ChatColor.YELLOW + Player.getPlayer().getName() + "さんがマイクラサバイバル人狼のサーバーに参加しました！");
    }

    Inventory StartGUI = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "プレイヤー人数と役職数の確認");

    public void openGUI(Player AdminPlayer) {
        // GUI表示
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

    int VillagerCount;
    int ALLPlayerCount;

    public void GameStart(){
        final ScoreboardManager managerW = Bukkit.getScoreboardManager();
        final ScoreboardManager managerV = Bukkit.getScoreboardManager();
        final ScoreboardManager managerM = Bukkit.getScoreboardManager();

        final Scoreboard scoreboardW = Objects.requireNonNull(managerW).getMainScoreboard();
        final Scoreboard scoreboardV = Objects.requireNonNull(managerV).getMainScoreboard();
        final Scoreboard scoreboardM = Objects.requireNonNull(managerM).getMainScoreboard();

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
            ALLPlayerCount++;
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
            playerALL5.setStatistic(org.bukkit.Statistic.DEATHS, 0);
        }
        VillagerCount = 2 * (ALLPlayerCount - BeforeWolfPlayerCount - BeforeMadmanPlayerCount);
        startTimer();
        LocateChat();
    }

    int TimerMain = 60 * 60 * 3; // 3時間
    int TimerHour;
    int TimerMinutes;
    int TimerSecond;
    int CLMain;
    int CLS;
    int CLM;

    String bar = "-------------------";
    String none = " ";

    private void startTimer() {
        ScoreboardManager managerTime = Bukkit.getScoreboardManager();
        Scoreboard boardTime = Objects.requireNonNull(managerTime).getNewScoreboard();
        Objective objectiveT = boardTime.registerNewObjective("MainTimer", "dummy", ChatColor.RED + " Survival-Werewolf ");
        Objects.requireNonNull(objectiveT).setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score9 = Objects.requireNonNull(objectiveT).getScore(ChatColor.DARK_GREEN + bar);
        score9.setScore(9);
        Score score8 = Objects.requireNonNull(objectiveT).getScore(ChatColor.GOLD + none);
        score8.setScore(8);
        Score score7 = Objects.requireNonNull(objectiveT).getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "残り時間 : " + ChatColor.RESET + ChatColor.YELLOW + TimerHour + ":" + TimerMinutes + ":" + TimerSecond);
        score7.setScore(7);
        Score score6 = Objects.requireNonNull(objectiveT).getScore(none);
        score6.setScore(6);
        Score score5 = Objects.requireNonNull(objectiveT).getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "次の位置情報まで : " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + CLM + ":" + CLS);
        score5.setScore(5);
        Score score4 = Objects.requireNonNull(objectiveT).getScore(ChatColor.RED + none);
        score4.setScore(4);
        Score score0 = Objects.requireNonNull(objectiveT).getScore(ChatColor.RESET + "" + ChatColor.DARK_GREEN + bar);
        score0.setScore(0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(boardTime);
        }
        TimerM = new BukkitRunnable() {
            @Override
            public void run() {
                boardTime.resetScores(ChatColor.AQUA + "" + ChatColor.BOLD + "残り時間 : " + ChatColor.RESET + ChatColor.YELLOW + TimerHour + ":" + TimerMinutes + ":" + TimerSecond);
                boardTime.resetScores(ChatColor.AQUA + "" + ChatColor.BOLD + "次の位置情報まで : " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + CLM + ":" + CLS);
                // メインタイマー
                TimerHour = TimerMain / 3600;
                TimerMinutes = (TimerMain - (TimerHour * 3600)) / 60;
                TimerSecond = (TimerMain - (TimerHour * 3600) - (TimerMinutes * 60));
                TimerMain = TimerMain - 1;
                // ロケートチャットタイマー
                CLMain = TimerMain % 300 + 1;
                CLM = CLMain / 60;
                CLS = CLMain - (CLM * 60);
                Score score7 = Objects.requireNonNull(objectiveT).getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "残り時間 : " + ChatColor.RESET + ChatColor.YELLOW + TimerHour + ":" + TimerMinutes + ":" + TimerSecond);
                score7.setScore(7);
                Score score5 = Objects.requireNonNull(objectiveT).getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "次の位置情報まで : " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + CLM + ":" + CLS);
                score5.setScore(5);
                for (Player playerA : Bukkit.getOnlinePlayers()) {
                    playerA.setScoreboard(boardTime);
                }
                if (TimerMain < 0){
                    MainTimerEnd();
                    LocateChatEnd();
                    WolfWin();
                }
                if (VillagerCount <= 0){
                    MainTimerEnd();
                    LocateChatEnd();
                    WolfWin();
                }
            }
        };
        TimerM.runTaskTimer(this, 0, 20); // 20 ticks = 1 second
    }

    public BukkitRunnable TimerM;

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player GUIClickedPlayer = (Player) event.getWhoClicked();
            // クリックされたGUIを取得する
            Inventory clickedInventory = Objects.requireNonNull(event.getClickedInventory());
            if (clickedInventory == StartGUI) {
                String ClickedItemName = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName();
                GUIClickedPlayer.addScoreboardTag("Admin1");
                // クリックされたアイテムを取得する
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
                    if (ClickedItemName.equals(ChatColor.RED + "クリックで人狼の数を減らす")) {
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                        if (BeforeWolfPlayerCount > 1) {
                            BeforeWolfPlayerCount += (-1);
                        } else {
                            GUIClickedPlayer.sendMessage(ChatColor.DARK_RED + "人狼の人数を1人未満にすることは出来ません。");
                            GUIClickedPlayer.playSound(Objects.requireNonNull(GUIClickedPlayer).getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.1f);
                        }
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    } else if (ClickedItemName.equals(ChatColor.RED + "クリックで狂人の数を減らす")) {
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
                        if (BeforeMadmanPlayerCount > 0) {
                            BeforeMadmanPlayerCount += (-1);
                        } else {
                            GUIClickedPlayer.sendMessage(ChatColor.DARK_RED + "狂人の人数を0人未満にすることは出来ません。");
                            GUIClickedPlayer.playSound(Objects.requireNonNull(GUIClickedPlayer).getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.1f);
                        }
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    }
                } else if (clickedItem != null && clickedItem.getType() == Material.BLUE_STAINED_GLASS_PANE) {
                    if (ClickedItemName.equals(ChatColor.BLUE + "クリックで人狼の数を増やす")) {
                        BeforeWolfPlayerCount += (1);
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.2f);
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    } else if (ClickedItemName.equals(ChatColor.BLUE + "クリックで狂人の数を増やす")) {
                        GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.2f);
                        BeforeMadmanPlayerCount += (1);
                        openGUI(Objects.requireNonNull(GUIClickedPlayer));
                    }
                }
                else if (clickedItem != null && clickedItem.getType() == Material.TOTEM_OF_UNDYING) {
                    GameStart();
                }
                else {
                    GUIClickedPlayer.sendMessage(ChatColor.AQUA + "サンゴに触らないで！！");
                    GUIClickedPlayer.playSound(GUIClickedPlayer.getLocation(), Sound.ENTITY_RABBIT_DEATH, 1.0f, 1.2f);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String originalDeathMessage = event.getDeathMessage();
        String modifiedDeathMessage = "誰かが死亡しました";
        event.setDeathMessage(modifiedDeathMessage);
        getLogger().info(originalDeathMessage);
        Player DeathPlayer = event.getEntity().getPlayer();
        assert DeathPlayer != null;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getEntryTeam(DeathPlayer.getName());
        if (team != null && team.getName().equals("villager")) {
            VillagerCount = VillagerCount - 1;
        }
        /*if (team != null && team.getName().equals("madman")) {
            DeathPlayer.setGameMode(GameMode.SPECTATOR);
        }*/
        if (DeathPlayer.getStatistic(org.bukkit.Statistic.DEATHS) >= 1) {
            DeathPlayer.setGameMode(GameMode.SPECTATOR);
        }
    }

    public void WolfWin(){
        for (Player playerUI : Bukkit.getOnlinePlayers()){
            sendTitle(playerUI, "&4人狼陣営の勝利","" , 10, 400, 10);
            playerUI.playSound(playerUI.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // エンダードラゴンを討伐した場合
        if (event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            for (Player playerUI : Bukkit.getOnlinePlayers()){
                sendTitle(playerUI, "&6村人陣営の勝利", "", 10, 400, 10);
            }
            if (event.getEntity().getKiller() != null) {
                Player player = (Player) event.getEntity().getKiller();
                String DragonKillPlayer = player.getName();
                for (Player playerA : Bukkit.getOnlinePlayers()) {
                    playerA.sendMessage(DragonKillPlayer + "は挑戦" + ChatColor.DARK_PURPLE + "[村人陣営勝利への貢献]" + ChatColor.RESET + ChatColor.WHITE + "を達成した");
                    playerA.playSound(playerA.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                }
                ItemStack diamond = new ItemStack(Material.DIAMOND);
                player.getInventory().addItem(diamond);
                player.updateInventory();
            }
            else{
                for (Player playerA : Bukkit.getOnlinePlayers()){
                    playerA.sendMessage(ChatColor.BOLD + "何者かがベットでエンダードラゴンを討伐しました...");
                }
            }
            LocateChatEnd();
            MainTimerEnd();
        }
    }

    public BukkitRunnable chatFlowTask;

    String LCPlayer;

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
                int LCx = randomPlayer.getLocation().getBlockX();
                int LCy = randomPlayer.getLocation().getBlockY();
                int LCz = randomPlayer.getLocation().getBlockZ();
                Location location = randomPlayer.getLocation();
                String dimensionName = Objects.requireNonNull(location.getWorld()).getEnvironment().name();
                for (Player playerA : Bukkit.getOnlinePlayers()) {
                    playerA.playSound(playerA.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
                }
                Bukkit.broadcastMessage(ChatColor.YELLOW + "位置情報公開の時間です。");
                Bukkit.broadcastMessage(ChatColor.RED + playerName + ChatColor.GREEN + " の座標は、" + ChatColor.RED + "X=" + LCx + ", Y=" + LCy + ", Z=" + LCz + ChatColor.GREEN + ", \nディメンションは" + ChatColor.RED + dimensionName + ChatColor.GREEN + "です");
                LCPlayer = playerName;
            }
        };
        chatFlowTask.runTaskTimer(this, 0, 6000); // 6000 ticks = 5 minutes
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getGameMode() == GameMode.SPECTATOR){
                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    teleportToSurface(player);
                    event.setCancelled(true);
                }
            }
        }
    }
    private void teleportToSurface(Player player) {
        World world = player.getWorld();
        Location surfaceLocation = world.getHighestBlockAt(player.getLocation()).getLocation();
        surfaceLocation.setY(surfaceLocation.getY() + 1);
        player.teleport(surfaceLocation);
    }

    public void LocateChatEnd(){
        if (chatFlowTask != null) {
            chatFlowTask.cancel();
            chatFlowTask = null;
        }
    }
    public void MainTimerEnd(){
        if (TimerM != null) {
            TimerM.cancel();
            TimerM = null;
        }
    }

    public void CountReset(){
        TimerMain = 60 * 60 * 3;
        CLMain = 300;
        VillagerCount = 0;
        ALLPlayerCount = 0;
        BeforeWolfPlayerCount = 1;
        BeforeMadmanPlayerCount = 0;
    }
}