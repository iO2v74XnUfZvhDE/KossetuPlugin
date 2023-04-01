package me.io2.kossetuplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebuffData {
    public final List<DebuffType> debuffList = new ArrayList<>();
    public final HashMap<DebuffType, Long> debuffTime = new HashMap<>();

    public void addDebuff(DebuffType type) {
        Long startMilli = System.currentTimeMillis();
        if (!debuffList.contains(type)) {
            debuffList.add(type);
        }

        if (debuffTime.containsKey(type)) {
            debuffTime.replace(type, startMilli);
        } else {
            debuffTime.put(type, startMilli);
        }
    }
}
