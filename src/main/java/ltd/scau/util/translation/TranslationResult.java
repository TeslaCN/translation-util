package ltd.scau.util.translation;

import java.util.List;

/**
 * @author Weijie Wu
 */
public class TranslationResult {

    private String source;

    private String target;

    private String query;

    private String translated;

    private List<WordMean> wordMeans;

    private List<Phonetic> phonetics;

    private String imageUrl;

    public TranslationResult() {
    }

    @Override
    public String toString() {
        return "TranslationResult{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", query='" + query + '\'' +
                ", translated='" + translated + '\'' +
                ", wordMeans=" + wordMeans +
                ", phonetics=" + phonetics +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<WordMean> getWordMeans() {
        return wordMeans;
    }

    public void setWordMeans(List<WordMean> wordMeans) {
        this.wordMeans = wordMeans;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<Phonetic> phonetics) {
        this.phonetics = phonetics;
    }
}
