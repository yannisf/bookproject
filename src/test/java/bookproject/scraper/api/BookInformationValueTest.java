package bookproject.scraper.api;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BookInformationValueTest {

    @Test
    public void buildTest() {
        BookInformationValue bookInformationValue = BookInformationValue.builder().isbn("X").author("Y").publisher("Z").title("A").build();
        assertThat(bookInformationValue.getIsbn()).isEqualToIgnoringCase("X");
        assertThat(bookInformationValue.getAuthor()).isEqualToIgnoringCase("Y");
        assertThat(bookInformationValue.getPublisher()).isEqualToIgnoringCase("Z");
        assertThat(bookInformationValue.getTitle()).isEqualToIgnoringCase("A");
    }

}
