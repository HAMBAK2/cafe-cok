package com.sideproject.hororok.aop.aspect;


import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.utils.logger.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogTraceAspect {

    private final com.sideproject.hororok.utils.logger.LogTrace logTrace;

    @Around("@annotation(logTrace)")
    public Object doTrace(ProceedingJoinPoint joinPoint, LogTrace logTrace) throws Throwable{

        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();
            status = this.logTrace.begin(message);

            Object result = joinPoint.proceed();

            this.logTrace.end(status);
            return result;
        } catch (Exception e) {

            this.logTrace.exception(status, e);
            throw e;
        }
    }
}
