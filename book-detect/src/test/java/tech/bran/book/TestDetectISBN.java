package tech.bran.book;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.bran.book.detector.BookAnalysis;
import tech.bran.book.detector.DetectISBN;

import java.io.File;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
@RunWith(JUnitParamsRunner.class)
public class TestDetectISBN {

    final DetectISBN detector = new DetectISBN();

    @Test
    @Parameters({
            "978-0486275437, /books/planetebook.com/1984.pdf",
            // TODO find some good free stuff
    })
    @Ignore
    public void test1_happyPath(String code, String testFile) throws Exception {
        final File file = resourceToFile(testFile);
        final BookAnalysis analysis = detector.detect(file);
        Assert.assertEquals(code, analysis.getProbableISBNCode());
    }

    @Test
    public void test4_corruptPDF() throws Exception {
        final File file = resourceToFile("/books/corrupt.pdf");
        final BookAnalysis analysis = detector.detect(file);
        Assert.assertTrue(analysis.isFileCorrupt());
    }

    @Test
    @Parameters({
            "/books/planetebook.com/1984.pdf",
    })
    public void test6_notDetected(String testFile) throws Exception {
        final File file = resourceToFile(testFile);
        final BookAnalysis analysis = detector.detect(file);
        Assert.assertFalse(analysis.isFileCorrupt());
        Assert.assertNull(analysis.getProbableISBNCode());
    }

    private static File resourceToFile(String res) {
        return new File(TestDetectISBN.class.getResource(res).getFile());
    }
}
