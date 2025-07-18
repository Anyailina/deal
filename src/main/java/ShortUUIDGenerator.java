package org.annill.deal;
import java.util.UUID;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ShortUUIDGenerator implements IdentifierGenerator {

    @Override
    public String generate(SharedSessionContractImplementor session, Object object) {
        String fullUuid = UUID.randomUUID().toString().replace("-", "");
        return fullUuid.substring(0, 30); // Обрезаем до 30 символов
    }

}
