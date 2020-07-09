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

    @OneToOne(mappedBy = "component", cascade = CascadeType.ALL)
    private Class myClass;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Measure measure;

    @OneToMany(mappedBy = "component", cascade = CascadeType.MERGE)
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
