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

    // search limit in pages at the beginning of the file
    final int searchLimitBegining = 5;
    // second limit in pages at the beginning of the file (in case the first limit returns nothing)
    final int searchLimitBegining2 = 20;
    // search limit in pages at the end of the file
    final int searchLimitEnding = 5;

    @Override
    public void scan(File file, BookAnalysis collector) throws IOException {

        L.trace("scanning first {} pages of {} for isbn...", searchLimitBegining, file);

        final PdfReader reader = new PdfReader(file.getPath());

        final int pages = reader.getNumberOfPages();

        int found = 0;
        for (int p = 1; p < pages && p < searchLimitBegining; p++) {
            final String text = PdfTextExtractor.getTextFromPage(reader, p, new SimpleTextExtractionStrategy());
            found += ScanUtil.scanFileForISBN(new StringReader(text), collector);
        }

        if (found == 0) {
            L.trace("extend search up to page {}", searchLimitBegining2);
            for (int p = searchLimitBegining; p < pages && p < searchLimitBegining2; p++) {
                final String text = PdfTextExtractor.getTextFromPage(reader, p, new SimpleTextExtractionStrategy());
                found += ScanUtil.scanFileForISBN(new StringReader(text), collector);
            }
        }

        L.trace("scanning last {} pages of {} for isbn...", searchLimitEnding, file);

        for (int p = Integer.max(pages - searchLimitEnding, searchLimitBegining); p < pages; p++) {
            final String text = PdfTextExtractor.getTextFromPage(reader, p, new SimpleTextExtractionStrategy());
            ScanUtil.scanFileForISBN(new StringReader(text), collector);
        }
    }
}
