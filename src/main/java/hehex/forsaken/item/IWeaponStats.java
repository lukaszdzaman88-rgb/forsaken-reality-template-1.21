package hehex.forsaken.item;

public interface IWeaponStats {
    float getWeaponDamage();
    int getRange();
    // For bows, this acts as draw speed (lower is faster)
    float getDrawSpeed();
}