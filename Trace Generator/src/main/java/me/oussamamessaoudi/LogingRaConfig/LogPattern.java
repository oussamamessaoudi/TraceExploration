package me.oussamamessaoudi.LogingRaConfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogPattern {
    @Builder.Default
    private long time = System.nanoTime();
    private String signature;
    private String input;
    private String output;
    private boolean isError;
}
