package tech.bran.book.util;

import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bran.book.api.C;
import tech.bran.book.api.ISBN;
import tech.bran.book.detector.BookAnalysis;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Teodor Bran
 * @since 13.06.17
 */
public class ScanUtil {

    static final Logger L = LoggerFactory.getLogger(ScanUtil.class);

    public static int scanFileForISBN(Readable input, BookAnalysis collector) {
        final Pattern extractor = Pattern.compile(C.SIMPLE_ISBN_MATCHER);
        final Scanner s = new Scanner(input);
        int found = 0;

        String match;
        do {
            match = s.findWithinHorizon(extractor, 0);
            L.trace("testing match: {}", match);
            if (match != null && ISBNValidator.getInstance().isValid(match)) {
                L.debug("scanFileForISBN() found candidate: {}", match);
                try {
                    collector.addISBN(new ISBN(match));
                    found++;
                } catch (Exception e) {
                    L.debug("Invalid ISBN {}. ({})", match, e.getMessage());
                }
            }
        } while (match != null);

        L.trace("scanFileForISBN() done");
        return found;
    }
}
