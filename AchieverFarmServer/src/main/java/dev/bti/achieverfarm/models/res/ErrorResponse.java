package dev.bti.achieverfarm.models.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ErrorResponse extends Response {
    Object error;

    public ErrorResponse(Boolean success, String message, Object error) {
        super(success, message);
        this.error = error;
    }
}
