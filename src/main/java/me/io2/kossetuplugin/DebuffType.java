package me.io2.kossetuplugin;

public enum DebuffType {
    LIGHT_BLEEDING("軽出血"), HEAVY_BLEEDING("重出血"), FRACTURE("骨折"), PAIN("痛み");

    private final String japaneseName;
    DebuffType(String japanese) {
        this.japaneseName = japanese;
    }

    public String getJapaneseName() {
        return japaneseName;
    }

    public int debuffTime = -1;
}
