package me.io2.kossetuplugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class KossetuListener implements Listener {
    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() == null || event.getItem().getType() != Material.DIAMOND_HOE || event.getPlayer().hasCooldown(Material.DIAMOND_HOE)) return;
            AttributeInstance attribute = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute == null) return;
            ItemStack item = event.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) return;
            int customModelData = itemMeta.getCustomModelData();
            switch (customModelData) {
                case 1: // 3sec Car
                    if (attribute.getValue() - event.getPlayer().getHealth() < 0.1) return;
                    event.getPlayer().setCooldown(Material.DIAMOND_HOE, 60);
                    KossetuPlugin.curHealingList.put(event.getPlayer().getUniqueId(), 1);
                    new KossetuHealingRunnable(event.getPlayer(), 3).runTaskTimer(KossetuPlugin.plugin, 0, 20L);
                    break;
                case 2: // 3sec Salewa
                    if (attribute.getValue() - event.getPlayer().getHealth() < 0.1) return;
                    event.getPlayer().setCooldown(Material.DIAMOND_HOE, 60);
                    KossetuPlugin.curHealingList.put(event.getPlayer().getUniqueId(), 2);
                    new KossetuHealingRunnable(event.getPlayer(), 6).runTaskTimer(KossetuPlugin.plugin, 0, 20L);
                    break;
                case 3: // 5sec Immobilizing splint
                    if (KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId()).debuffList.contains(DebuffType.FRACTURE)) {
                        event.getPlayer().setCooldown(Material.DIAMOND_HOE, 100);
                        KossetuPlugin.curHealingList.put(event.getPlayer().getUniqueId(), 3);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (KossetuPlugin.curHealingList.containsKey(event.getPlayer().getUniqueId()) && KossetuPlugin.debuffList.containsKey(event.getPlayer().getUniqueId()) && KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId()).debuffList.contains(DebuffType.FRACTURE)) {
                                    DebuffData debuffData = KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId());
                                    debuffData.debuffList.remove(DebuffType.FRACTURE);
                                    debuffData.debuffTime.remove(DebuffType.FRACTURE);
                                    KossetuPlugin.debuffList.replace(event.getPlayer().getUniqueId(), debuffData);
                                    KossetuPlugin.curHealingList.remove(event.getPlayer().getUniqueId());
                                    event.getPlayer().sendMessage("解消したデバフ: §c骨折");

                                    ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
                                    ItemMeta itemMeta = itemInMainHand.getItemMeta();
                                    if (itemMeta instanceof Damageable) {
                                        Damageable damageable = (Damageable) itemMeta;
                                        int stackPercent = DamageUtils.getOnceGage(5);
                                        if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                                        damageable.setDamage(damageable.getDamage() + stackPercent);
                                        itemInMainHand.setItemMeta((ItemMeta) damageable);
                                        if (damageable.getDamage() >= 1561) event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                    }
                                }
                            }
                        }.runTaskLater(KossetuPlugin.plugin, 100L);
                    }
                    break;
                case 4: // 3sec CALOK-B
                    if (KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId()).debuffList.contains(DebuffType.HEAVY_BLEEDING)){
                        event.getPlayer().setCooldown(Material.DIAMOND_HOE, 60);
                        KossetuPlugin.curHealingList.put(event.getPlayer().getUniqueId(), 4);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (KossetuPlugin.curHealingList.containsKey(event.getPlayer().getUniqueId()) && KossetuPlugin.debuffList.containsKey(event.getPlayer().getUniqueId()) && KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId()).debuffList.contains(DebuffType.HEAVY_BLEEDING)) {
                                    DebuffData debuffData = KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId());
                                    debuffData.debuffList.remove(DebuffType.HEAVY_BLEEDING);
                                    debuffData.debuffTime.remove(DebuffType.HEAVY_BLEEDING);
                                    KossetuPlugin.debuffList.replace(event.getPlayer().getUniqueId(), debuffData);
                                    KossetuPlugin.curHealingList.remove(event.getPlayer().getUniqueId());
                                    event.getPlayer().sendMessage("解消したデバフ: §c重い出血");

                                    ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
                                    ItemMeta itemMeta = itemInMainHand.getItemMeta();
                                    if (itemMeta instanceof Damageable) {
                                        Damageable damageable = (Damageable) itemMeta;
                                        int stackPercent = DamageUtils.getOnceGage(3);
                                        if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                                        damageable.setDamage(damageable.getDamage() + stackPercent);
                                        itemInMainHand.setItemMeta((ItemMeta) damageable);
                                        if (damageable.getDamage() >= 1561) event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                    }
                                }
                            }
                        }.runTaskLater(KossetuPlugin.plugin, 60L);
                    }
                    break;
                case 5: // 7sec Golden-star
                    event.getPlayer().setCooldown(Material.DIAMOND_HOE, 140);
                    KossetuPlugin.curHealingList.put(event.getPlayer().getUniqueId(), 5);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (KossetuPlugin.curHealingList.containsKey(event.getPlayer().getUniqueId())) {
                                if (KossetuPlugin.analgesiaList.containsKey(event.getPlayer().getUniqueId())) {
                                    KossetuPlugin.analgesiaList.replace(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                                } else {
                                    KossetuPlugin.analgesiaList.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                                }
                                KossetuPlugin.curHealingList.remove(event.getPlayer().getUniqueId());
                                event.getPlayer().sendMessage("バフ: §b鎮痛");

                                ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
                                ItemMeta itemMeta = itemInMainHand.getItemMeta();
                                if (itemMeta instanceof Damageable) {
                                    Damageable damageable = (Damageable) itemMeta;
                                    int stackPercent = DamageUtils.getOnceGage(12);
                                    if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                                    damageable.setDamage(damageable.getDamage() + stackPercent);
                                    itemInMainHand.setItemMeta((ItemMeta) damageable);
                                    if (damageable.getDamage() >= 1561) event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                }
                            }
                        }
                    }.runTaskLater(KossetuPlugin.plugin, 140L);
                    break;
            }
        }
    }

    @EventHandler
    private void onItemChange(PlayerItemHeldEvent event) {
        if (KossetuPlugin.curHealingList.containsKey(event.getPlayer().getUniqueId())) {
            KossetuPlugin.curHealingList.remove(event.getPlayer().getUniqueId());
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cCancelled Healing"));
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            DebuffData debuffData = KossetuPlugin.debuffList.get(player.getUniqueId());
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                float fallDistance = player.getFallDistance();
                float v = fallDistance - KossetuPlugin.fractureMinDist;
                if (v < 0) return;
                if (new Random().nextInt(100) < v * 3) {
                    debuffData.addDebuff(DebuffType.FRACTURE);
                    player.sendMessage("デバフ: §c骨折");
                    KossetuPlugin.debuffList.replace(player.getUniqueId(), debuffData);
                }

                KossetuPlugin.damageList.replace(event.getEntity().getUniqueId(), KossetuPlugin.damageList.get(event.getEntity().getUniqueId()) + event.getDamage());
            }
        }
    }

    @EventHandler
    private void onWorldChange(PlayerChangedWorldEvent event) {
        clear(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        clear(event.getEntity().getUniqueId());
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        KossetuPlugin.debuffList.put(event.getPlayer().getUniqueId(), new DebuffData());
        KossetuPlugin.damageList.put(event.getPlayer().getUniqueId(), 0D);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        KossetuPlugin.damageList.remove(event.getPlayer().getUniqueId());
        clear(event.getPlayer().getUniqueId());
    }

    private boolean jumpDetect = false;
    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if (KossetuPlugin.analgesiaList.containsKey(event.getPlayer().getUniqueId())) return;

        DebuffData debuffData = KossetuPlugin.debuffList.get(event.getPlayer().getUniqueId());
        if (debuffData.debuffList.contains(DebuffType.PAIN) || debuffData.debuffList.contains(DebuffType.FRACTURE)) {
            Vector multiply = event.getPlayer().getVelocity().multiply(0.9);
            multiply.setY(event.getPlayer().getVelocity().getY());
            event.getPlayer().setVelocity(multiply);

            if (event.getFrom().getY() < event.getTo().getY() && !jumpDetect) {
                if (debuffData.debuffList.contains(DebuffType.FRACTURE)) event.getPlayer().damage(1);
                jumpDetect = true;
            } else {
                jumpDetect = false;
            }
        }
    }

    private void clear(UUID uuid) {
        DebuffData debuffData = KossetuPlugin.debuffList.get(uuid);
        debuffData.debuffTime.clear();
        debuffData.debuffList.clear();
        KossetuPlugin.debuffList.replace(uuid, debuffData);
        KossetuPlugin.analgesiaList.remove(uuid);
        KossetuPlugin.curHealingList.remove(uuid);
        KossetuPlugin.damageList.replace(uuid, 0D);
    }
}
