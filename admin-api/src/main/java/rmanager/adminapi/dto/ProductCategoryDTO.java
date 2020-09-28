package rmanager.adminapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {

    private Integer productCategoryId;
    private String name;

}
