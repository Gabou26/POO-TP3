package dynamique;

import pack.StringSource;
import voiture.Voiture;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.util.List;

public class VoitureFactory {
    public enum ModeConstruction { INSTANCIATION, META, REFLEXION }

    public static Voiture buildVoiture(ModeConstruction mode, boolean sport, int vitesse) {
        switch (mode) {
            case META:
                return VoitureFactoryMetaHelper.constructMeta("MetaVoituree", sport, vitesse);
        }

        return null;
    }
}
