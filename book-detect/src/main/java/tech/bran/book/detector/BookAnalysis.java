package tech.bran.book.detector;

import org.apache.commons.lang3.ObjectUtils;
import tech.bran.book.api.ISBN;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class BookAnalysis {

    /**
     * detected ISBNs
     */
    private Map<ISBN, Integer> ISBNs = new LinkedHashMap<>();
    private boolean corruptFile;

    public BookAnalysis addISBN(ISBN isbn) {
        final int i = ObjectUtils.defaultIfNull(ISBNs.get(isbn), 0);
        ISBNs.put(isbn, i + 1);
        return this;
    }

    public BookAnalysis setFileCorrupt(boolean c) {
        this.corruptFile = c;
        return this;
    }

    public Map<ISBN, Integer> getISBNs() {
        return ISBNs;
    }

    public ISBN getProbableISBN() {
        if (ISBNs.size() == 0) {
            return null;
        }
        return ISBNs.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElse(null).getKey(); //TODO
    }

    public String getProbableISBNCode() {
        final ISBN isbn = getProbableISBN();
        return isbn == null ? null : isbn.getISBN();
    }

    public boolean isFileCorrupt() {
        return corruptFile;
    }
}
