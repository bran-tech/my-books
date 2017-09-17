package tech.bran.book.detector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bran.book.api.ISBN;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class BookAnalysis {

    final Logger L = LoggerFactory.getLogger(BookAnalysis.class);

    /**
     * detected ISBNs
     */
    private Map<ISBN, Integer> ISBNs = new LinkedHashMap<>();
    private boolean corruptFile;
    private String contentType;

    // most probable ISBN
    private ISBN probable;

    public BookAnalysis addISBN(ISBN isbn) {
        for (Map.Entry<ISBN, Integer> item : ISBNs.entrySet()) {
            if (item.equals(isbn)) {
                // we try to keep the version with dashes
                if (StringUtils.contains(item.getKey().getISBN(), "-")) {
                    item.setValue(item.getValue() + 1);// increment nr of apparitions
                } else {
                    ISBNs.put(isbn, item.getValue() + 1); // increment nr of apparitions
                }
                return this;
            }
        }

        // add new item
        ISBNs.put(isbn, 1);

        probable = null;

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
        if (probable == null) {
            if (ISBNs.size() != 0) {
                //TODO better strategy

                final Map.Entry<ISBN, Integer> max = ISBNs.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElse(null);

                if (max != null) {
                    probable = max.getKey();

                    final int total = ISBNs.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
                    final double probability = (double) max.getValue() / (total + 1);
                    L.debug("ISBN: {}  probability: {}", probable, probability);
                }
            }
        }
        return probable;
    }

    public String getProbableISBNCode() {
        final ISBN isbn = getProbableISBN();
        return isbn == null ? null : isbn.getISBN();
    }

    public boolean isFileCorrupt() {
        return corruptFile;
    }

    public String getContentType() {
        return contentType;
    }

    public BookAnalysis setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
