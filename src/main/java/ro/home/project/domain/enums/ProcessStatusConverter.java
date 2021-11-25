package ro.home.project.domain.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ProcessStatusConverter implements AttributeConverter<ProcessStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProcessStatus entityEnum) {
        return (entityEnum == null) ? null : entityEnum.getCode();
    }

    @Override
    public ProcessStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ProcessStatus.values())
                .filter(c -> code.equals(c.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
