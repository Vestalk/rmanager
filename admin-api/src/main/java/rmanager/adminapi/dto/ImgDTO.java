package rmanager.adminapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImgDTO {

    private Long imgId;
    private String imageContent;

}
