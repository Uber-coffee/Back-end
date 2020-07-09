package auth.menu;

import auth.menu.entity.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class MeasureConverter implements AttributeConverter<Component.Measure, String> {

    @Override
    public String convertToDatabaseColumn(Component.Measure measure) {
        if (measure == null) {
            return null;
        }
        return measure.getCode();
    }

    @Override
    public Component.Measure convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Component.Measure.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
