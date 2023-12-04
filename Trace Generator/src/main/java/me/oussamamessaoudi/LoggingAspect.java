package me.oussamamessaoudi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;


@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {
    private Tracer tracer;
    private ObjectMapper objectMapper;

    @Around("within(@RA *) && execution(public * *(..))")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Logger log = getLogger(proceedingJoinPoint.getTarget().getClass());
        ScopedSpan span = tracer.startScopedSpan(proceedingJoinPoint.getTarget().getClass().getSimpleName());
        log.info(buildMessage(proceedingJoinPoint.getArgs(), TypeLog.INPUT, proceedingJoinPoint.getSignature().toShortString()));
        try {
            Object proceed = proceedingJoinPoint.proceed();
            log.info(buildMessage(proceed, TypeLog.OUTPUT, null));
            return proceed;
        } catch (Throwable throwable) {
            log.error(buildMessage(throwable, TypeLog.ERROR, null));
            throw throwable;
        } finally {
            span.end();
        }
    }

    private String buildMessage(Object data, TypeLog typeLog, String signature) throws JsonProcessingException {
        return objectMapper.writeValueAsString(LogPattern.builder()
                .data(data)
                .typeLog(typeLog)
                .signature(signature)
                .build());

    }
}
