package tech.bran.book.detector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bran.book.detector.impl.DetectISBNByName;
import tech.bran.book.detector.impl.DetectISBNinPDFiText;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class DetectISBN {

    private static final Logger L = LoggerFactory.getLogger(DetectISBN.class);

    public BookAnalysis detect(File file) {
        L.trace("detect({})", file);

        if (file == null || !file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("'" + file + "' was not found or is not file");
        }

        final BookAnalysis analysis = new BookAnalysis();

        new DetectISBNByName().scan(file, analysis);

        final String contentType = URLConnection.guessContentTypeFromName(file.getName());

        if ("application/pdf".equals(contentType)) {
            try {
                new DetectISBNinPDFiText().scan(file, analysis);
                //new DetectISBNinPDF().scan(file, analysis);
            } catch (IOException e) {
                L.error("error on detect by content in pdf", e);
                analysis.setFileCorrupt(true);
            }

        } else {
            L.debug("content type {} unsupported by detector", contentType);
        }

        return analysis;
    }
}
