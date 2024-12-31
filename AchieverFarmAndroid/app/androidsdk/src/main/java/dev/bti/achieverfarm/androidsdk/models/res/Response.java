package dev.bti.achieverfarm.androidsdk.models.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Response<T> {
    Boolean success;
    String message;
    Object error;
    T data;
}
