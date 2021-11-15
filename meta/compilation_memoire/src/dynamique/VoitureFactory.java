package dynamique;

import pack.StringSource;
import voiture.Voiture;
import voiture.VoitureSport;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.util.List;

public class VoitureFactory {
    public enum ModeConstruction { INSTANCIATION, META, REFLEXION }

    public static Voiture buildVoiture(ModeConstruction mode, boolean sport, int vitesse) {
        switch (mode) {
            case META:
                return VoitureFactoryMetaHelper.constructMeta("MetaVoituree", sport, vitesse);
            case INSTANCIATION:
                return constructInstance(sport, vitesse);
        }

        return null;
    }

    private static Voiture constructInstance(boolean sport, int vitesse) {
        Voiture voiture = null;

        if (sport)
            voiture = new VoitureSport();
        else
            voiture = new Voiture(vitesse);

        return voiture;
    }
}
