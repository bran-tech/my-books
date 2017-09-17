package tech.bran.book.api;

import org.apache.commons.validator.routines.ISBNValidator;

/**
 * @author Teodor Bran
 * @since 11.06.17
 */
public class ISBN {

    String isbn10;
    String isbn13;

    final ISBNValidator v = ISBNValidator.getInstance();

    public ISBN(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("null isbn");
        }

        if (v.isValidISBN10(isbn)) {
            this.isbn10 = isbn;
            this.isbn13 = v.convertToISBN13(isbn.replace("-", ""));

        } else if (v.isValidISBN13(isbn)) {
            this.isbn13 = isbn.replace(" ", "-");

        } else {
            throw new IllegalArgumentException("invalid isbn " + isbn);
        }
    }

    public String getISBN() {
        return isbn13;
    }

    @Override
    public String toString() {
        return isbn13;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ISBN isbn = (ISBN) o;
        return getIsbn13Number().equals(isbn.getIsbn13Number());
    }

    public String getIsbn13Number() {
        return isbn13.replaceAll("-", "");
    }

    @Override
    public int hashCode() {
        return getIsbn13Number().hashCode();
    }

}
