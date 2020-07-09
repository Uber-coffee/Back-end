package auth.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id", nullable = false)
    private Class myClass;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Measure measure;

    @OneToMany(mappedBy = "component")
    @JsonIgnore
    private List<Recipe> recipe;

    public enum Measure {
        PIECE("P"), GR("G"), ML("M");

        private String code;

        Measure(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
