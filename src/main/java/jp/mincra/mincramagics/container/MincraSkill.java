package jp.mincra.mincramagics.container;

public class MincraSkill {

    private String id = "Unknown";
    private String name = "";
    private String lore = "";
    private float cooltime = 0f;
    private int exp_lv = 0;
    private int exp = 0;
    private float break_rate = 0;
    private int skill_point = 0;

    public float getBreak_rate() {
        return break_rate;
    }

    public void setBreak_rate(float break_rate) {
        this.break_rate = break_rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getExp_lv() {
        return exp_lv;
    }

    public void setExp_lv(int exp_lv) {
        this.exp_lv = exp_lv;
    }

    public float getCooltime() {
        return cooltime;
    }

    public void setCooltime(float cooltime) {
        this.cooltime = cooltime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSkill_point() {
        return skill_point;
    }

    public void setSkill_point(int skill_point) {
        this.skill_point = skill_point;
    }
}
