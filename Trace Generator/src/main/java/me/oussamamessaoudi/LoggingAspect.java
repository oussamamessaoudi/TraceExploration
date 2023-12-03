package me.oussamamessaoudi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {
    private Tracer tracer;
    private ObjectMapper objectMapper;


    @Before("execution(* me.oussamamessaoudi.RA*.exec(..))")
    public void logBefore(JoinPoint joinPoint) throws JsonProcessingException {
        Logger log = getLogger(joinPoint.getTarget().getClass());
        tracer.startScopedSpan(joinPoint.getTarget().getClass().getSimpleName()).context();
        LogPattern logPattern = LogPattern.builder()
                .inputs(joinPoint.getArgs())
                .build();
        log.info(objectMapper.writeValueAsString(logPattern));

    }

    @AfterReturning(value = "execution(* me.oussamamessaoudi.RA*.exec(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        Logger log = getLogger(joinPoint.getTarget().getClass());

        LogPattern logPattern = LogPattern.builder()
                .output(result)
                .build();
        log.info(objectMapper.writeValueAsString(logPattern));
    }

    @AfterThrowing(value = "execution(* me.oussamamessaoudi.RA*.exec(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) throws JsonProcessingException {
        Logger log = getLogger(joinPoint.getTarget().getClass());

        LogPattern logPattern = LogPattern.builder()
                .exception(Arrays.stream(exception.getStackTrace()).limit(3).collect(Collectors.toList()))
                .build();
        log.error(objectMapper.writeValueAsString(logPattern));
    }

}
