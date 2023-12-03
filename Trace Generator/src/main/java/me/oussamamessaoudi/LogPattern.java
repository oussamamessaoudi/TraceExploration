package me.oussamamessaoudi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogPattern {
    private Object[] inputs;
    private Object output;
    private List<StackTraceElement> exception;

}
