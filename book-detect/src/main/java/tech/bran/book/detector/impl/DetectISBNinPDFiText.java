package tech.bran.book.detector.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bran.book.detector.BookAnalysis;
import tech.bran.book.detector.ISBNDetector;
import tech.bran.book.util.ScanUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class DetectISBNinPDFiText implements ISBNDetector {

    private static final Logger L = LoggerFactory.getLogger(DetectISBNinPDFiText.class);

    final int searchLimitBegin = 5;

    @Override
    public void scan(File file, BookAnalysis collector) throws IOException {

        L.trace("scanning {} pages of {} for isbn...", searchLimitBegin, file);

        final PdfReader reader = new PdfReader(file.getPath());

        int pages = reader.getNumberOfPages();
        for (int p = 1; p < pages && p < searchLimitBegin; p++) {
            final String text = PdfTextExtractor.getTextFromPage(reader, p, new SimpleTextExtractionStrategy());
            ScanUtil.scanFileForISBN(new StringReader(text), collector);
        }
    }
}
