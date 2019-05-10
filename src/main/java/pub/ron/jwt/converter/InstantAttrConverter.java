package pub.ron.jwt.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * 属性转换器
 * @author ron
 * 2019.05.09
 */
@Converter(autoApply = true)
public class InstantAttrConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        return (instant == null ? null : Timestamp.from(instant));
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp) {
        return (timestamp == null ? null : timestamp.toInstant());
    }
}
