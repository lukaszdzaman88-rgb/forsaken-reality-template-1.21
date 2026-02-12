package hehex.forsaken.item;

public interface IWeaponStats {
    // Bazowe obrażenia broni (np. 8 dla łuku)
    float getWeaponDamage();

    // Prędkość pocisku (Zasięg). Vanilla łuk ma ok. 3.0f przy pełnym naciągu.
    float getProjectileVelocity();

    // Szybkość naciągania (w tickach). Vanilla łuk ma 20.
    float getDrawSpeed();
}