package com.marketplace.api.dto.label;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabelDto {

       @JsonProperty(access = JsonProperty.Access.READ_ONLY)
       private Long id;

       @NotBlank(message = "Le nom du label est obligatoire")
       private String name;

       private String description;
}

