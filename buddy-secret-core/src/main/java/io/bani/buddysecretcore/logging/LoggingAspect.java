package io.bani.buddysecretcore.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // io.bani 하위의 모든 Controller와 Service를 타겟으로 잡습니다.
    @Around("execution(* io.bani..*Controller.*(..)) || execution(* io.bani..*Service.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // 실행 시간 측정을 위한 스톱워치 시작
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.debug("▶ [START] {}.{}()", className, methodName);

        try {
            Object result = joinPoint.proceed(); // 실제 로직 수행

            stopWatch.stop();
            log.info("◀ [END] {}.{}() - 소요시간: {}ms", className, methodName, stopWatch.getTotalTimeMillis());

            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.error("✘ [ERROR] {}.{}() - 메시지: {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}