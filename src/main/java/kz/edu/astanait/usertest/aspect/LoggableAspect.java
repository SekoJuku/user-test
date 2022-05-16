package kz.edu.astanait.usertest.aspect;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Log4j2
public class LoggableAspect {

    @Pointcut("execution(public * ((@kz.edu.astanait.usertest.annotation.Loggable *)+).*(..)) && within(@kz.edu.astanait.usertest.annotation.Loggable *))")
    public void executionOfAnyPublicMethodInAtLoggableType() { }

    @Pointcut("@annotation(kz.edu.astanait.usertest.annotation.Loggable)")
    public void executionOfLoggableMethod() { }

    @SneakyThrows
    @Around("executionOfAnyPublicMethodInAtLoggableType() || executionOfLoggableMethod()")
    public Object aroundCall(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        log.info("-----" + "Loggable method/class is invoked" + "-----");
        log.info("Class Name: " + signature.getDeclaringTypeName());
        log.info("Method Name: " + signature.getName());
        log.info("Args: " + Arrays.toString(joinPoint.getArgs()));
        Object object = joinPoint.proceed(joinPoint.getArgs());
        log.info("Result: " + object);
        return object;
    }
}
