package dynamique;

import pack.StringSource;
import javax.tools.JavaFileObject;

public class TestFactory {

    public static JavaFileObject buildSource(String nomClasse) {

        int x = (int)(Math.random() * 1000);
        StringBuilder sb = new StringBuilder();

        sb.append("package dynamique;\n");
        sb.append("public class " + nomClasse + " extends Test{\n");
        genererAttributs(sb);
        genererConstructeurs(nomClasse, x, sb);
        genererMethodes(sb);
        sb.append("}\n");

        System.out.println("LA CLASSE");
        System.out.println(sb.toString());

        return new StringSource(nomClasse, sb.toString());
    }

    private static void genererConstructeurs(String nomClasse, int x, StringBuilder sb) {

        sb.append("public " + nomClasse + "(){ y = 8; setX(" + x + ");}\n");
    }

    private static void genererMethodes(StringBuilder sb) {

        sb.append("public int getY(){return y;}\n");

    }

    private static void genererAttributs(StringBuilder sb) {

        sb.append("private int y;\n");
    }
}