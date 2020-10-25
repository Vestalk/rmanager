package rmanager.adminapi.dto;

import lombok.*;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.entity.other.PaymentMethod;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;
    private TelegramUserDTO client;
    private OrderStatus orderStatus;
    private Long dateCreate;
    private Long dateCooking;
    private Long dateExecute;
    private PaymentMethod paymentMethod;
    private List<OrderItemDTO> orderItemList = new ArrayList<>();

}
