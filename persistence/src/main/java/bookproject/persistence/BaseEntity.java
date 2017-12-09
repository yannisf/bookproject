package bookproject.persistence;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 6069325986206529213L;

    @Id
    @GeneratedValue
    private Long id;

    private Date createdDate;

    @PrePersist
    void initCreatedDate() {
        this.setCreatedDate(new Date());
    }

}
