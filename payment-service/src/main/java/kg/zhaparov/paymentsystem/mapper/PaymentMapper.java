package kg.zhaparov.paymentsystem.mapper;

import kg.zhaparov.paymentsystem.api.dto.PaymentDto;
import kg.zhaparov.paymentsystem.domain.db.PaymentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto entityToDto(PaymentEntity paymentEntity);
    List<PaymentDto> mapListToDto(List<PaymentEntity> paymentEntities);
}
