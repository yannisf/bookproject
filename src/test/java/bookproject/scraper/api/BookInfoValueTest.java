package bookproject.scraper.api;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BookInfoValueTest {

    @Test
    public void buildTest() {
        BookInfoValue bookInfoValue = BookInfoValue.builder().isbn("X").author("Y").publisher("Z").title("A").build();
        assertThat(bookInfoValue.getIsbn()).isEqualToIgnoringCase("X");
        assertThat(bookInfoValue.getAuthor()).isEqualToIgnoringCase("Y");
        assertThat(bookInfoValue.getPublisher()).isEqualToIgnoringCase("Z");
        assertThat(bookInfoValue.getTitle()).isEqualToIgnoringCase("A");
    }

}
