package pojos;

/**
 * Created by masahito on 15/07/17.
 */
public class HaikuResulotPojo {
    private final String haiku;
    private final boolean hasDiscovered;
    public HaikuResulotPojo(final String haiku) {
        this.haiku = haiku;
        this.hasDiscovered = !"".equals(haiku);
    }
    public final String getHaiku() {
        return this.haiku;
    }
    public final boolean hasDiscovered() {
        return this.hasDiscovered;
    }
}
