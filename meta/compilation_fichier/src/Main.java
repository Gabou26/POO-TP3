import pack.StringSource;

import javax.tools.*;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.OutputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        //compilationFichier();
        compilationPackage();
    }

    // À partir d'un fichier provenant d'un emplacement quelconque
    private static void compilationFichier() {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        // Un fichier de classe Java, le source pourrait être n'importe quoi
        Iterable<? extends JavaFileObject> sources = fileManager.getJavaFileObjectsFromStrings(
                List.of("C:\\projets\\reflexion\\Test1.java",
                        "C:\\projets\\reflexion\\Test2.java"));

        // pour avoir un suivi de la compilation
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();

        // Définir la tâche de compilation et lier la source
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, null,
                null, sources);
        // Compiler
        task.call();

        // Pour journaliser les erreurs (très utile pour débogger)
        for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics())
            System.out.println(d);
    }

    // À partir d'un fichier provenant du projet (dans le package pack)
    // Utilisation d'un StringSource, un conteneur avec les infos de la classe à compiler
    private static void compilationPackage() {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StringSource s1 = new StringSource("pack.Test");

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        List<StringSource> sources = List.of(s1);

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, null,
                null, sources);
        task.call();

        for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics())
            System.out.println(d);
    }
}