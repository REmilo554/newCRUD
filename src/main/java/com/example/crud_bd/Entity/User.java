package com.example.crud_bd.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotBlank
    @Column(name = "second_name", nullable = false)
    private String secondName;
    @Min(14)
    @Max(100)
    @Column(name = "age", nullable = false)
    private Integer age;
    @Length(min = 11, max = 11)
    @NotBlank
    @Column(name = "passport" , nullable = false)
    private String passport;
}
