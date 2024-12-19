package com.example.crud_bd.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PassportDTO {
    @Length(min = 11,max = 11)
    @NotBlank
    private String passport;
}
