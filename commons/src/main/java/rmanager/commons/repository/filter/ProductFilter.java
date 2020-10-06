package rmanager.commons.repository.filter;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilter {

    private Boolean isAvailable;
    private Integer productCategoryId;

}
