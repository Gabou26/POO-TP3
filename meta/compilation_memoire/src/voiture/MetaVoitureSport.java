package voiture;

public class MetaVoitureSport extends VoitureSport implements Surveillable {
    public MetaVoitureSport() { super(); }

    @Override
    public int surveiller(int limite) {
        return getVitesse() - limite;
    }
}
