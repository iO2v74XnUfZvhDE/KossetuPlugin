package me.io2.kossetuplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class KossetuPlugin extends JavaPlugin {
    public static final HashMap<UUID, Double> damageList = new HashMap<>();
    public static final HashMap<UUID, DebuffData> debuffList = new HashMap<>();
    public static final HashMap<UUID, Long> analgesiaList = new HashMap<>();
    public static final HashMap<UUID, Integer> curHealingList = new HashMap<>();
    public static final boolean debug = false;
    public static int fractureMinDist;
    public static int lightBleedingMin;
    public static int lightBleedingMax;
    public static int heavyBleeding;
    public static int pain;
    public static int lightBleedingEffectTime;
    public static int heavyBleedingEffectTime;
    public static int painEffectTime;

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        debuffList.clear();
        // Plugin startup logic
        getLogger().info("HI!");
        Bukkit.getServer().getPluginManager().registerEvents(new KossetuListener(), this);

        saveDefaultConfig();
        FileConfiguration config = getConfig();
        fractureMinDist = config.getInt("FractureMinDist", 5);
        lightBleedingMin = config.getInt("LightBleedingMin",  5);
        lightBleedingMax = config.getInt("LightBleedingMax", 10);
        heavyBleeding = config.getInt("HeavyBleeding", 15);
        pain = config.getInt("Pain", 3);
        lightBleedingEffectTime = config.getInt("LightBleedingEffectTime", 0);
        heavyBleedingEffectTime = config.getInt("HeavyBleedingEffectTime", 0);
        painEffectTime = config.getInt("PainEffectTime", 0);

        DebuffType.PAIN.debuffTime = painEffectTime;
        DebuffType.HEAVY_BLEEDING.debuffTime = heavyBleedingEffectTime;
        DebuffType.LIGHT_BLEEDING.debuffTime = lightBleedingEffectTime;

        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<UUID, DebuffData> uuidDebuffDataHashMap = new HashMap<>(debuffList);
                uuidDebuffDataHashMap.forEach((uuid, debuffData) -> {
                    List<DebuffType> arrayList = new ArrayList<>(debuffData.debuffList);
                    for (DebuffType debuffType : arrayList) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (debuffType.debuffTime == -1 || player == null) continue;
                        Long aLong = debuffData.debuffTime.get(debuffType);
                        if (System.currentTimeMillis() - aLong > debuffType.debuffTime) {
                            DebuffData debuffData1 = debuffList.get(uuid);
                            debuffData1.debuffTime.remove(debuffType);
                            debuffData1.debuffList.remove(debuffType);

                            player.sendMessage("解消したデバフ: §c" + debuffType.getJapaneseName());
                            continue;
                        }

                        if (debuffType == DebuffType.LIGHT_BLEEDING) {
                            player.damage(1);
                        }

                        if (debuffType == DebuffType.HEAVY_BLEEDING) {
                            player.damage(3);
                        }
                    }

                    HashMap<UUID, Long> uuidLongHashMap = new HashMap<>(analgesiaList);
                    uuidLongHashMap.forEach((uuid1, aLong) -> {
                        if (System.currentTimeMillis() - aLong >  175000) {
                            analgesiaList.remove(uuid1);
                            Player player = Bukkit.getPlayer(uuid1);
                            if (player == null) return;
                            player.sendMessage("消失したバフ: §b鎮痛");
                        }
                    });
                });
            }
        }.runTaskTimer(this, 0, 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<UUID, Double> uuidDoubleHashMap = new HashMap<>(damageList);
                uuidDoubleHashMap.forEach((uuid, aDouble) -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) return;
                    DebuffData debuffData = KossetuPlugin.debuffList.get(player.getUniqueId());

                    if (aDouble > KossetuPlugin.lightBleedingMin && aDouble < KossetuPlugin.lightBleedingMax) {
                        debuffData.addDebuff(DebuffType.LIGHT_BLEEDING);
                        // Bukkit.broadcastMessage("Light!");
                        player.sendMessage("デバフ: §c軽い出血");
                    } else if (aDouble >= KossetuPlugin.heavyBleeding) {
                        debuffData.addDebuff(DebuffType.HEAVY_BLEEDING);
                        // Bukkit.broadcastMessage("Heavy!");
                        player.sendMessage("デバフ: §c重い出血");
                    }

                    if (aDouble > KossetuPlugin.pain) {
                        debuffData.addDebuff(DebuffType.PAIN);
                        // Bukkit.broadcastMessage("Pain!");
                        player.sendMessage("デバフ: §c痛み");
                    }
                });

                for (UUID uuid : damageList.keySet()) {
                    damageList.replace(uuid, 0D);
                }
            }
        }.runTaskTimer(this, 0L, 5L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
