package me.io2.kossetuplugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class KossetuHealingRunnable extends BukkitRunnable {
    private final Player player;
    private final int healAmount;
    private int healed = 0;
    public KossetuHealingRunnable(Player player, int healAmount) {
        this.player = player;
        this.healAmount = healAmount;
    }
    @Override
    public void run() {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (!player.isOnline() || player.getInventory().getItemInMainHand().getType() != Material.DIAMOND_HOE || !KossetuPlugin.curHealingList.containsKey(player.getUniqueId()) || attribute == null || healed == 3) {
            KossetuPlugin.curHealingList.remove(player.getUniqueId());
            cancel();
            return;
        }

        double health = player.getHealth();
        player.setHealth(Math.min(attribute.getValue(), health + healAmount));
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInMainHand.getItemMeta();
        if (itemMeta instanceof Damageable) {
            Damageable damageable = (Damageable) itemMeta;
            if (itemMeta.getCustomModelData() == 1) {
                int stackPercent = DamageUtils.getOnceGage(220) * 23;
                if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                damageable.setDamage(damageable.getDamage() + stackPercent);
            } else if (itemMeta.getCustomModelData() == 2) {
                int stackPercent = DamageUtils.getOnceGage(400) * 25;
                if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                damageable.setDamage(damageable.getDamage() + stackPercent);
            }
            itemInMainHand.setItemMeta((ItemMeta) damageable);
            if (damageable.getDamage() >= 1561) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                cancel();
                return;
            }
        }

        switch (healed) {
            case 0:
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c■□□"));
                break;
            case 1:
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6■■□"));
                break;
            case 2:
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§a■■■"));
                player.sendMessage("§aHeal Completed");
                DebuffData debuffData = KossetuPlugin.debuffList.get(player.getUniqueId());
                if (debuffData.debuffList.contains(DebuffType.LIGHT_BLEEDING)) {
                    debuffData.debuffTime.remove(DebuffType.LIGHT_BLEEDING);
                    debuffData.debuffList.remove(DebuffType.LIGHT_BLEEDING);
                    player.sendMessage("解消したデバフ: §c軽い出血");

                    itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
                    if (itemMeta instanceof Damageable) {
                        Damageable damageable = (Damageable) itemMeta;
                        if (itemMeta.getCustomModelData() == 1) {
                            int stackPercent = DamageUtils.getOnceGage(220) * 12;
                            if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                            damageable.setDamage(damageable.getDamage() + stackPercent);
                        } else if (itemMeta.getCustomModelData() == 2) {
                            int stackPercent = DamageUtils.getOnceGage(400) * 17;
                            if (KossetuPlugin.debug) Bukkit.broadcastMessage(String.valueOf(stackPercent));
                            damageable.setDamage(damageable.getDamage() + stackPercent);
                        }

                        player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
                        if (damageable.getDamage() >= 1561) {
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            cancel();
                            return;
                        }
                    }
                }
                break;
        }

        healed++;
    }
}
