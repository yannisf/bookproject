package bookproject.scrapper.api;


/**
 * Encapsulates the scraped book information.
 */
public class BookInfo {

    private String isbn;
    private String title;
    private String author;
    private String publisher;

    @java.beans.ConstructorProperties({"isbn", "title", "author", "publisher"})
    BookInfo(String isbn, String title, String author, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public static BookInfoBuilder builder() {
        return new BookInfoBuilder();
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BookInfo)) return false;
        final BookInfo other = (BookInfo) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$isbn = this.getIsbn();
        final Object other$isbn = other.getIsbn();
        if (this$isbn == null ? other$isbn != null : !this$isbn.equals(other$isbn)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        final Object this$publisher = this.getPublisher();
        final Object other$publisher = other.getPublisher();
        if (this$publisher == null ? other$publisher != null : !this$publisher.equals(other$publisher)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $isbn = this.getIsbn();
        result = result * PRIME + ($isbn == null ? 43 : $isbn.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $author = this.getAuthor();
        result = result * PRIME + ($author == null ? 43 : $author.hashCode());
        final Object $publisher = this.getPublisher();
        result = result * PRIME + ($publisher == null ? 43 : $publisher.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BookInfo;
    }

    public String toString() {
        return "BookInfo(isbn=" + this.getIsbn() + ", title=" + this.getTitle() + ", author=" + this.getAuthor() + ", publisher=" + this.getPublisher() + ")";
    }

    public static class BookInfoBuilder {
        private String isbn;
        private String title;
        private String author;
        private String publisher;

        BookInfoBuilder() {
        }

        public BookInfo.BookInfoBuilder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookInfo.BookInfoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookInfo.BookInfoBuilder author(String author) {
            this.author = author;
            return this;
        }

        public BookInfo.BookInfoBuilder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookInfo build() {
            return new BookInfo(isbn, title, author, publisher);
        }

        public String toString() {
            return "BookInfo.BookInfoBuilder(isbn=" + this.isbn + ", title=" + this.title + ", author=" + this.author + ", publisher=" + this.publisher + ")";
        }
    }
}
