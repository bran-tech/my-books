package tech.bran.book.detector;

import java.io.File;
import java.io.IOException;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public interface ISBNDetector {

    void scan(File file, BookAnalysis collector) throws IOException;

}
