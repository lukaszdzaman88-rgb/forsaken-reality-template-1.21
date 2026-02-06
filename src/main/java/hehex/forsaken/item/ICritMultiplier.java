package hehex.forsaken.item;

public interface ICritMultiplier {
    /**
     * Returns the critical hit damage multiplier for this item.
     * Default vanilla behavior is 1.5F.
     */
    float getCritDamageMultiplier();
}