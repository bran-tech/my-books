package tech.bran.book.detector.impl;

import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bran.book.api.ISBN;
import tech.bran.book.detector.BookAnalysis;
import tech.bran.book.detector.ISBNDetector;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class DetectISBNByName implements ISBNDetector {

    private static final Logger L = LoggerFactory.getLogger(DetectISBNByName.class);

    @Override
    public void scan(File file, BookAnalysis collector) {
        final String name = file.getName();
        final Pattern extractor = Pattern.compile("\\d[\\d -]{8,}\\d");
        final Matcher m = extractor.matcher(name);

        while (m.find()) {
            final String match = m.group();
            L.trace("match: {}", match);
            if (ISBNValidator.getInstance().isValid(match)) {
                L.debug("detectByName({}) : {}", name, match);
                collector.addISBN(new ISBN(match));
            }
        }
        L.trace("detectByName({}) done", name, "-");
    }
}
