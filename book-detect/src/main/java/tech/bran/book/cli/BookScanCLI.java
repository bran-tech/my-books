package tech.bran.book.cli;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import picocli.CommandLine;
import tech.bran.book.api.C;
import tech.bran.book.detector.BookAnalysis;
import tech.bran.book.detector.DetectISBN;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Utility that tries to detect the ISBNs for all the files found in a folder.
 * Uses <a href="http://picocli.info/">picocli</a>.
 *
 * @author Teodor Bran
 * @since 17.09.17
 */
@CommandLine.Command(name = "bookscan", description = "scans a directory for books and tries to detect their ISBNs")
public class BookScanCLI implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", arity = "0..1", type = File.class, description = "File or directory to scan. Defaults to current folder.")
    private File fileToScan = new File(".");

    @CommandLine.Option(names = {"-r", "--recursive"}, description = "Search in all the subdirectories. Only used if a directory is scanned.")
    private boolean recursive;

    @CommandLine.Option(names = {"-e", "--export-result"}, description = "Export the results to results-{timestamp}.csv")
    private boolean export;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message and exit.")
    private boolean helpRequested;

    public static void main(String[] args) throws Exception {
        CommandLine.call(new BookScanCLI(), System.err, args);
    }

    @Override
    public Integer call() throws Exception {
        if (helpRequested) {
            CommandLine.usage(this, System.err);
            return 0;
        }

        final DetectISBN detector = new DetectISBN();
        int total = 0, detected = 0;

        final CSVPrinter printer = !export ? null :
                CSVFormat.DEFAULT.withDelimiter(';').withEscape('"')
                        .withHeader("File", "ISBN", "ContentType", "Corrupt", "Path")
                        .print(new File("result_" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".csv"), C.UTF8);

        try {
            if (fileToScan.isFile()) {
                final BookAnalysis analysis = detector.detect(fileToScan);
                if (printResult(fileToScan, analysis, printer)) {
                    detected++;
                }
                total++;
            } else {
                final String[] extensions = new String[]{"pdf", "doc", "txt", "epub", "mobi"};

                for (File file : FileUtils.listFiles(fileToScan, extensions, recursive)) {

                    final BookAnalysis analysis = detector.detect(file);
                    if (printResult(file, analysis, printer)) {
                        detected++;
                    }
                    total++;
                }
            }
        } finally {
            if (printer != null) printer.flush();
            IOUtils.closeQuietly(printer);
        }

        System.out.println("Detected " + detected + "/" + total);

        return detected;
    }

    private static boolean printResult(File file, BookAnalysis analysis, CSVPrinter p) throws IOException {
        if (p != null) {
            p.printRecord(file.getName(), analysis.getProbableISBNCode(), analysis.getContentType(),
                    analysis.isFileCorrupt(), file.getPath());
        }

        if (analysis.isFileCorrupt()) {
            System.out.println(file.getPath() + " : corrupt");
        } else {
            System.out.println(file.getPath() + " : " + analysis.getProbableISBNCode());
        }

        return analysis.getProbableISBN() != null;
    }
}
