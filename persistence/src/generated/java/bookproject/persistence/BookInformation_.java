package bookproject.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BookInformation.class)
public abstract class BookInformation_ extends bookproject.persistence.BaseEntity_ {

    public static volatile SingularAttribute<BookInformation, String> provider;
    public static volatile SingularAttribute<BookInformation, String> author;
    public static volatile SingularAttribute<BookInformation, String> isbn;
    public static volatile SingularAttribute<BookInformation, String> isbn13;
    public static volatile SingularAttribute<BookInformation, String> publisher;
    public static volatile SingularAttribute<BookInformation, String> title;
    public static volatile SingularAttribute<BookInformation, String> url;

}

