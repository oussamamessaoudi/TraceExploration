package me.oussamamessaoudi.loging_config;


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
        ScopedSpan span = tracer.startScopedSpan(aClass.getSimpleName());
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        LogPattern.LogPatternBuilder logPatternBuilder = LogPattern.builder()
                .input(logElementHelper.logElement(signature.getMethod(), proceedingJoinPoint.getArgs()))
                .signature(signature.toShortString());
        try {
            Object proceed = proceedingJoinPoint.proceed();
            Class<?> returnType = signature.getReturnType();
            var data = (returnType == void.class) ? "void" : (returnType.getSimpleName() + logElementHelper.logObject(proceed));
            logPatternBuilder.output(data).isError(false);
            return proceed;
        } catch (Throwable throwable) {
            logPatternBuilder.output(throwable.toString()).isError(true);
            throw throwable;
        } finally {
            Logger log = loggers.computeIfAbsent(aClass, LoggerFactory::getLogger);
            LogPattern logPattern = logPatternBuilder.build();
            String msg = objectMapper.writeValueAsString(logPattern);
            if (logPattern.isError()) {
                log.error(msg);
            } else {
                log.info(msg);
            }
            span.end();
        }
    }

}
