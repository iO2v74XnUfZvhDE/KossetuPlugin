package me.io2.kossetuplugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KossetuPluginExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "kossetu";
    }

    @Override
    public @NotNull String getAuthor() {
        return "iO2";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("list")) {
            HashMap<UUID, DebuffData> hashMap = new HashMap<>(KossetuPlugin.debuffList);
            DebuffData debuffData = hashMap.get(player.getUniqueId());
            StringBuilder builder = new StringBuilder();
            for (DebuffType debuffType : debuffData.debuffList) {
                builder.append(debuffType.getJapaneseName() + " ");
            }

            return builder.toString().trim();
        } else {
            String[] s = params.split("_");
            if (s.length > 1) {
                if (s[0].equalsIgnoreCase("time")) {
                    DebuffData debuffData = KossetuPlugin.debuffList.get(player.getUniqueId());
                    List<DebuffType> debuffList = debuffData.debuffList;
                    DebuffType targetType = null;
                    for (DebuffType debuffType : debuffList) {
                        if (debuffType.getJapaneseName().equalsIgnoreCase(s[1]) || debuffType.name().equalsIgnoreCase(s[1]))
                            targetType = debuffType;
                    }
                }
            }
        }
        return null;
    }
}
