package hehex.forsaken.combat;

public interface ICombatEntity {
    // Parry
    void startParry();
    int getParryTicks();
    void setSuccessfulParry();

    // Stun & Vulnerability
    int getStunTicks();
    void applyStun(int ticks);
    boolean isVulnerable();
    void setVulnerable(boolean vulnerable);

    // Projectiles
    boolean isParriedProjectile();
    void setParriedProjectile(boolean parried);
}