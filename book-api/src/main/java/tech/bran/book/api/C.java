package tech.bran.book.api;

import java.nio.charset.Charset;

/**
 * @author Teodor Bran
 * @since 17.09.17
 */
public final class C {

    public final static Charset UTF8 = Charset.forName("UTF-8");

    /**
     * should match both isbn10 & isbn13
     */
    public final static String SIMPLE_ISBN_MATCHER = "\\d[\\d -]{8,17}\\d";
}
