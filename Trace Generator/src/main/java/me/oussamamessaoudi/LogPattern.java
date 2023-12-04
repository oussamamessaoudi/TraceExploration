package me.oussamamessaoudi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogPattern {
    @Builder.Default
    private long time = System.nanoTime();
    private Object data;
    private TypeLog typeLog;
    private String signature;

}
