package me.oussamamessaoudi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;


@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {
    private Tracer tracer;
    private ObjectMapper objectMapper;
    private LogElementHelper logElementHelper;

    @Around("within(@RA *) && execution(public * *(..))")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Logger log = getLogger(proceedingJoinPoint.getTarget().getClass());
        ScopedSpan span = tracer.startScopedSpan(proceedingJoinPoint.getTarget().getClass().getSimpleName());
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        log.info(buildMessage(logElementHelper.logElement(signature.getMethod(), proceedingJoinPoint.getArgs()), TypeLog.INPUT, proceedingJoinPoint.getSignature().toShortString()));
        try {
            Object proceed = proceedingJoinPoint.proceed();
            Class<?> returnType = signature.getReturnType();
            var data = (returnType == void.class) ? "void" : (returnType.getSimpleName() + logElementHelper.logObject(proceed));
            log.info(buildMessage(data, TypeLog.OUTPUT, null));
            return proceed;
        } catch (Throwable throwable) {
            log.error(buildMessage(throwable.toString(), TypeLog.ERROR, null));
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
