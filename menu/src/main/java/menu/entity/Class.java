package auth.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Class {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_single", nullable = false)
    private Boolean isSingle;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Component component;
}