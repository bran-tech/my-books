package tech.bran.book.util;

import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static void scanFileForISBN(Readable input, BookAnalysis collector) {
        final Pattern extractor = Pattern.compile("\\d[\\d -]{8,}\\d");
        final Scanner s = new Scanner(input);

        String match;
        do {
            match = s.findWithinHorizon(extractor, 0);
            //L.trace("possible match: {}", match);
            if (match != null && ISBNValidator.getInstance().isValid(match)) {
                L.debug("scanFileForISBN() found: {}", match);
                collector.addISBN(new ISBN(match));
            }
        } while (match != null);

        L.trace("scanFileForISBN() done");
    }
}
