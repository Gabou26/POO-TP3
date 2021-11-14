package dynamique;

import pack.StringSource;
import voiture.Voiture;

import javax.tools.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class VoitureFactoryMetaHelper {
    public static Voiture constructMeta(String nomClasse, boolean sport, int vitesse) {
        if (sport)
            nomClasse += "Sport";

        // ******** ETAPE #1 : Préparation pour la compilation
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<ByteArrayClass> classes = new ArrayList<>();           // pour mettre les .class   (IMPORTANT)
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
        JavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        // La classe qui se charge de fournir les "conteneurs" au compilateur à la volée, sans accès au disque
        fileManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager){
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
                                                       FileObject sibling) throws IOException {
                if (kind == JavaFileObject.Kind.CLASS){
                    ByteArrayClass outFile = new ByteArrayClass(className);
                    classes.add(outFile);           // ICI IMPORTANT
                    return outFile;
                }
                else
                    return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        };

        // ********** ETAPE #2 : Génération Code Source
        StringSource sourceMeta = VoitureFactoryMetaHelper.buildMeta(nomClasse, sport);

        // *** #3 : Compilation
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, null,null, List.of(sourceMeta));
        Boolean result = task.call();

        for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics())
            System.out.println(d);
        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!result) {
            System.out.println("ECHEC DE LA COMPILATION");
            System.exit(1);
        }

        ByteArrayClasseLoader loader = new ByteArrayClasseLoader(classes);
        Voiture voiture = null;
        try {
            // Recherche la classe dans le contexte "local" sinon il passe par le "loader"
            if (sport)
                voiture = (Voiture)(Class.forName("voiture." + nomClasse, true, loader).getDeclaredConstructor().newInstance());
            else
                voiture = (Voiture)(Class.forName("voiture." + nomClasse, true, loader).getDeclaredConstructor(int.class).newInstance(vitesse));
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return voiture;
    }

    private static StringSource buildMeta(String nomClasse, boolean sport) {
        String classExtend = "Voiture";
        if (sport) {
            classExtend += "Sport";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("package voiture;\n");
        sb.append("public class " + nomClasse + " extends " + classExtend + " implements Surveillable{\n");
        //genAttrMeta(sb, sport);
        genConstrMeta(nomClasse, sport, sb);
        genMethodesMeta(sb);
        sb.append("}\n");

        System.out.println("LA CLASSE META");
        System.out.println(sb.toString());

        return new StringSource(nomClasse, sb.toString()); //Convert en voiture?
    }

    private static void genConstrMeta(String nomClasse, boolean sport, StringBuilder sb) {
        if (sport)
            sb.append("public " + nomClasse + "() { super(); }\n");
        else
            sb.append("public " + nomClasse + "(int vitesse) { super(vitesse); }\n");
    }

    private static void genMethodesMeta(StringBuilder sb) {
        sb.append("@Override\npublic int surveiller(int limite) { return getVitesse() - limite; }\n");
    }

    private static void genAttrMeta(StringBuilder sb, boolean sport) {
        if (sport)
            return;
        sb.append("private int y;\n");
    }
}
