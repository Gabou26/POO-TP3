package voiture;

public class MetaVoiture extends Voiture implements Surveillable{

    public MetaVoiture(int vitesse) {
        super(vitesse);
    }

    @Override
    public int surveiller(int limite) {
        return getVitesse() - limite;
    }
}
