package auth.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone_number")
})

@ApiModel
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "Provide Customer's First Name", example = "Ivan")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @ApiModelProperty(notes = "Provide Customer's Second Name", example = "Ivanov")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ApiModelProperty(notes = "Provide Customer's Phone Number, formatting +[reg][phone number] or 8[phone number]", example = "+73222281337 or 88005553535")
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "registration_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime registrationDate;

    @Column(name = "payment_approach_id")
    private String paymentApproachId;

    @PrePersist
    public void setRegistrationDate(){
        registrationDate = DateTime.now();
    }

}
