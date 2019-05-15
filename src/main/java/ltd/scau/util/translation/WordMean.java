package ltd.scau.util.translation;

import java.util.List;

/**
 * @author Weijie Wu
 */
public class WordMean {

    private String part;

    private List<String> means;

    @Override
    public String toString() {
        return "WordMean{" +
                "part='" + part + '\'' +
                ", means=" + means +
                '}';
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public List<String> getMeans() {
        return means;
    }

    public void setMeans(List<String> means) {
        this.means = means;
    }
}
