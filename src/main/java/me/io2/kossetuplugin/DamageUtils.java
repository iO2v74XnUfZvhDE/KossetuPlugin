package me.io2.kossetuplugin;

public class DamageUtils {
    public static int getOnceGage(int maxUseCount) {
        return (int) ((1.0f / (float) maxUseCount) * 1561.0f);
    }
}
