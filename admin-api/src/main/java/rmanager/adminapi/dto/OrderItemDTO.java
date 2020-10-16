package rmanager.adminapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long itemId;
    private Integer number;
    private ProductDTO product;

}
