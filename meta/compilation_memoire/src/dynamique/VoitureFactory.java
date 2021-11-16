package dynamique;

import pack.StringSource;
import voiture.Voiture;
import voiture.VoitureSport;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.Class.forName;

public class VoitureFactory {
    public enum ModeConstruction { INSTANCIATION, META, REFLEXION }

    public static Voiture buildVoiture(ModeConstruction mode, boolean sport, int vitesse) {
        switch (mode) {
            case META:
                return VoitureFactoryMetaHelper.constructMeta("MetaVoiture", sport, vitesse);
            case INSTANCIATION:
                return constructInstance(sport, vitesse);
            case REFLEXION:
                return constructReflexion(sport,vitesse);
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

    private static Voiture constructReflexion(boolean sport, int vitesse) {
        Voiture voiture = null;

        //Voiture de sport
        if (sport) {

            Class classVoitureSport = null;

            try {
                classVoitureSport = forName("voiture.VoitureSport");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {

                assert classVoitureSport != null;
                voiture = (VoitureSport) classVoitureSport.getDeclaredConstructor().newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        //Voiture normale
        else {

            Class classVoiture = null;

            try {
                classVoiture = forName("voiture.Voiture");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                voiture = (Voiture) classVoiture.getDeclaredConstructor(int.class).newInstance(vitesse);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return voiture;
    }
}
