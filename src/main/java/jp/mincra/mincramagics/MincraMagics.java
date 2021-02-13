package jp.mincra.mincramagics;

import jp.mincra.mincramagics.command.*;
import jp.mincra.mincramagics.entity.mob.MobManager;
import jp.mincra.mincramagics.entity.mob.hostile.ExampleZombieMob;
import jp.mincra.mincramagics.entity.player.PlayerManager;
import jp.mincra.mincramagics.event.EventNotifier;
import jp.mincra.mincramagics.item.ItemManager;
import jp.mincra.mincramagics.listener.*;
import jp.mincra.mincramagics.property.JSONManager;
import jp.mincra.mincramagics.property.PropertyManager;
import jp.mincra.mincramagics.skill.SkillManager;
import jp.mincra.mincramagics.skill.material.JumpMaterial;
import jp.mincra.mincramagics.skill.material.MoveMaterial;
import jp.mincra.mincramagics.skill.rod.*;
import jp.mincra.mincramagics.sql.SQLManager;
import jp.mincra.mincramagics.ui.UIManager;
import jp.mincra.mincramagics.util.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MincraMagics extends JavaPlugin {

    private static MincraMagics instance;

    private static EventNotifier eventNotifier;

    private static PlayerManager playerManager;
    private static MobManager mobManager;
    private static PropertyManager propertyManager;
    private static SQLManager sqlManager;
    private static UIManager uiManager;
    private static JSONManager jsonManager;
    private static ItemManager itemManager;
    private static SkillManager skillManager;

    @Override
    public void onEnable() {

        instance = this;

        //manager
        getPropertyManager();
        propertyManager.loadProperty();
        getPlayerManager();
        playerManager.setOnlinePlayerList();
        getSQLManager();
//        sqlManager.getConnection();
        sqlManager.createTables();
        getUIManager();
        getJSONManager();
        getSkillManager();
        skillManager.register(jsonManager.getAllJSONArray("./plugins/MincraMagics/data/skills"));
        getItemManager();
        itemManager.register(jsonManager.getAllJSONArray("./plugins/MincraMagics/data/items"));
        getMobManager();
        mobManager.register(jsonManager.getAllJSONArray("./plugins/MincraMagics/data/mobs"));

        //モブスキル
        new BukkitRunnable() {
            @Override
            public void run(){
                mobManager.loadAllCustomEntity();
            }
        }.runTaskLater(this, 60);

        //listener
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new onPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new onPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new onPrepareItemCraft(), this);
        getServer().getPluginManager().registerEvents(new onPlayerToggleFlight(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteractEntity(), this);
        getServer().getPluginManager().registerEvents(new onEntitySpawn(), this);
        getServer().getPluginManager().registerEvents(new onEntityDeath(), this);
        getServer().getPluginManager().registerEvents(new onInventory(), this);
        //独自リスナー
        getEventNotifier();
        //Skills
        //rod
        eventNotifier.registerEvents(new MoveRod());
        eventNotifier.registerEvents(new CureRod());
        eventNotifier.registerEvents(new ExpRod());
        eventNotifier.registerEvents(new InfernoRod());
        eventNotifier.registerEvents(new JumpRod());
        eventNotifier.registerEvents(new WaterRod());
        eventNotifier.registerEvents(new BarrierRod());
        eventNotifier.registerEvents(new BeastRod());
        eventNotifier.registerEvents(new DestroyRod());
        //material
        eventNotifier.registerEvents(new MoveMaterial());
        eventNotifier.registerEvents(new JumpMaterial());
        //Mobs
        eventNotifier.registerEvents(new ExampleZombieMob());

        //command
        getCommand("mcr").setExecutor(new MincraCommands(this));
        getCommand("mcr").setTabCompleter(new MincraTabCompleter());
        getCommand("skill").setExecutor(new SkillCommands());
        getCommand("translate").setExecutor(new TranslateCommands());
        getCommand("translate").setTabCompleter(new TranslateTabCompleter());
    }

    @Override
    public void onDisable() {
        //SQL全て保存
        sqlManager.getMincraPlayerSQL().saveMincraPlayer();

        instance = null;
        ChatUtil.sendConsoleMessage("プラグインが正常に終了しました。");
        setEnabled(false);
    }

    public static void reload() {
        ChatUtil.sendConsoleMessage("プラグインをリロードします...");
        propertyManager.loadProperty();
        itemManager.register(jsonManager.getAllJSONArray("./plugins/MincraMagics/data/items"));
        skillManager.register(jsonManager.getAllJSONArray("./plugins/MincraMagics/data/skills"));
        mobManager.register(jsonManager.getAllJSONArray("./plugins/MincraMagics/data/mobs"));
    }


    public static MincraMagics getInstance(){
        return instance;
    }

    public static EventNotifier getEventNotifier() {
        if (eventNotifier == null) {
            eventNotifier = new EventNotifier();
        }
        return eventNotifier;
    }

    public static PlayerManager getPlayerManager() {
        if (playerManager == null)
            playerManager = new PlayerManager();
        return playerManager;
    }

    public static MobManager getMobManager() {
        if (mobManager == null)
            mobManager = new MobManager();
        return mobManager;
    }

    public static PropertyManager getPropertyManager() {
        if (propertyManager == null)
            propertyManager = new PropertyManager();
        return propertyManager;
    }

    public static SQLManager getSQLManager() {
        if (sqlManager == null)
            sqlManager = new SQLManager();
        return sqlManager;
    }

    public static UIManager getUIManager(){
        if (uiManager == null)
            uiManager = new UIManager();
        return uiManager;
    }

    public static JSONManager getJSONManager() {
        if (jsonManager == null)
            jsonManager = new JSONManager();
        return jsonManager;
    }

    public static ItemManager getItemManager() {
        if (itemManager == null)
            itemManager = new ItemManager();
        return itemManager;
    }

    public static SkillManager getSkillManager() {
        if (skillManager == null)
            skillManager = new SkillManager();
        return skillManager;
    }
}

        /*
       y↑
         H.----------.G :loc2
         /|         /|
       E.----------.F|
        |D.--------|-.C
        |/         |/
 loc1: A.----------.B  →x
       /
      /↗z
         */


        /*
        　　   　 ＿＿　　　　　　           　 ｼﾞｬｰ 　 　　＿＿__
　  ／⌒ヽ　 　  　|;;lヽ::/　　  　 　 　 　　　　∧_∧　  /__　o、 |、
　（　＾ω＾）∫.　.|;;|:: :|~　　 　　　　　　　（ ´・ω・）ﾉ.ii | ・　＼ﾉ
　（　　つc□　　i===i=i c□c□c□　　 　　旦旦旦旦（　ｏ　   旦| ・　　|
|￣￣￣￣￣￣￣￣￣￣￣￣￣￣|　　|￣￣￣￣￣￣￣￣￣￣￣￣￣￣|
|　　　コーヒーの方はこちらへ 　　|　　|　　　お茶の方はこちらへ  　  .|
         */