package com.teppichcenter.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class AbstractEntity implements Serializable {
    @Id
    private Long id;
}