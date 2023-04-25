package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

//참고
//> 스프링 AOP는 AspectJ의 문법을 차용하고, 프록시 방식의 AOP를 제공한다. AspectJ를 직접 사용하는
//것이 아니다.
//> 스프링 AOP를 사용할 때는 @Aspect 애노테이션을 주로 사용하는데, 이 애노테이션도 AspectJ가
//제공하는 애노테이션이다.
//참고
//> @Aspect 를 포함한 org.aspectj 패키지 관련 기능은 aspectjweaver.jar 라이브러리가 제공하는
//기능이다. 앞서 build.gradle 에 spring-boot-starter-aop 를 포함했는데, 이렇게 하면 스프링의
//AOP 관련 기능과 함께 aspectjweaver.jar 도 함께 사용할 수 있게 의존 관계에 포함된다.
//> 그런데 스프링에서는 AspectJ가 제공하는 애노테이션이나 관련 인터페이스만 사용하는 것이고, 실제
//AspectJ가 제공하는 컴파일, 로드타임 위버 등을 사용하는 것은 아니다. 스프링은 지금까지 우리가 학습한
//것 처럼 프록시 방식의 AOP를 사용한다.

@Slf4j
@Aspect
public class AspectV1 {

    @Around("execution(* hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}",joinPoint.getSignature());  //joinPoint signature
        return joinPoint.proceed();
    }
}
