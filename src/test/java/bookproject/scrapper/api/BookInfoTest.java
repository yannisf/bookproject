package bookproject.scrapper.api;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BookInfoTest {

    @Test
    public void buildTest() {
        BookInfo bookInfo = BookInfo.builder().isbn("X").author("Y").publisher("Z").title("A").build();
        assertThat(bookInfo.getIsbn()).isEqualToIgnoringCase("X");
        assertThat(bookInfo.getAuthor()).isEqualToIgnoringCase("Y");
        assertThat(bookInfo.getPublisher()).isEqualToIgnoringCase("Z");
        assertThat(bookInfo.getTitle()).isEqualToIgnoringCase("A");
    }

}
