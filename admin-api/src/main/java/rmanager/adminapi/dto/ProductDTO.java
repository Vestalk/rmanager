package rmanager.adminapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer productId;
    private String name;
    private String description;
    private Double cost;
    private Boolean isAvailable;
    private ImgDTO img;
    private ProductCategoryDTO category;

}
