package dev.bti.achieverfarm.models.res;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Response {
    Boolean success;
    String message;
}
