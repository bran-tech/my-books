package tech.bran.book.detector.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bran.book.detector.BookAnalysis;
import tech.bran.book.detector.ISBNDetector;
import tech.bran.book.util.ScanUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class DetectISBNinPDF implements ISBNDetector {

    private static final Logger L = LoggerFactory.getLogger(DetectISBNinPDF.class);

    final int searchLimitBegin = 5;

    @Override
    public void scan(File file, BookAnalysis collector) throws IOException {

        try (PDDocument document = PDDocument.load(file)) {

            final PDDocumentInformation info = document.getDocumentInformation();
            L.debug("pdf.info\n  author: {}\n  title: {}\n  creator: {}\n producer: {}\n  metadata: {}",
                    info.getAuthor(), info.getTitle(), info.getCreator(), info.getProducer(), info.getMetadataKeys());

            int pages = document.getNumberOfPages();
            for (int p = 0; p < pages && p < searchLimitBegin; p++) {
                L.trace("page {}", p);
                PDPage page = document.getPage(p);

                ScanUtil.scanFileForISBN(new InputStreamReader(page.getContents()), collector);
            }
        }
    }
}
