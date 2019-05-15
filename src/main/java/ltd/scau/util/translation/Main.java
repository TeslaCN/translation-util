package ltd.scau.util.translation;

import java.util.ServiceLoader;

/**
 * @author Weijie Wu
 */
public class Main {

    public static void main(String[] args) {
        ServiceLoader<TranslationTool> loader = ServiceLoader.load(TranslationTool.class);
        for (TranslationTool tool : loader) {
            String[] queries = new String[]{"tiger", "free", "take down", "Please protect ports used by ChromeDriver and related test frameworks to prevent access by malicious code."};
            for (String query : queries) {
                System.out.println(tool.translate(query));
            }
        }
    }
}
