package overcooked_industries.better_effect_hud.client.screens.elements;

public enum BetterEffectHudAlignment {
    TOP_LEFT(0, 0),
    TOP_MIDDLE(1, 0),
    TOP_RIGHT(2, 0),
    MIDDLE_LEFT(0, 1),
    MIDDLE_MIDDLE(1, 1),
    MIDDLE_RIGHT(2, 1),
    BOTTOM_LEFT(0, 2),
    BOTTOM_MIDDLE(1, 2),
    BOTTOM_RIGHT(2, 2);
    public final int posX;
    public final int posY;


    BetterEffectHudAlignment(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
