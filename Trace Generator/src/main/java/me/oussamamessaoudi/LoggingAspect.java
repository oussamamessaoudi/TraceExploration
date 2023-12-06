package me.oussamamessaoudi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final Tracer tracer;
    private final ObjectMapper objectMapper;
    private final LogElementHelper logElementHelper;
    private Map<Class<?>, Logger> loggers;

    @PostConstruct
    void init() {
        loggers = new ConcurrentHashMap<>();
    }


    @Around("within(@RA *) && execution(public * *(..))")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
        Logger log = loggers.computeIfAbsent(aClass, LoggerFactory::getLogger);
        ScopedSpan span = tracer.startScopedSpan(aClass.getSimpleName());
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        log.info(buildMessage(logElementHelper.logElement(signature.getMethod(), proceedingJoinPoint.getArgs()), TypeLog.INPUT, signature.toShortString()));
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
