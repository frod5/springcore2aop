package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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
public class AspectV3 {


    // hello.aop.order 패키지와 하위패키지
    //@Pointcut
    //@Pointcut 에 포인트컷 표현식을 사용한다.
    //메서드 이름과 파라미터를 합쳐서 포인트컷 시그니처(signature)라 한다.
    //메서드의 반환 타입은 void 여야 한다.
    //코드 내용은 비워둔다.
    //포인트컷 시그니처는 allOrder() 이다. 이름 그대로 주문과 관련된 모든 기능을 대상으로 하는 포인트컷이다.
    ////private , public 같은 접근 제어자는 내부에서만 사용하면 private 을 사용해도 되지만, 다른
    //애스팩트에서 참고하려면 public 을 사용해야 한다.
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {}  //Pointcut signature


    //타입(클래스 또는 인터페이스) 이름 패턴 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {}

    //@Around 어드바이스에서는 포인트컷을 직접 지정해도 되지만, 포인트컷 시그니처를 사용해도 된다.
    //여기서는 @Around("allOrder()") 를 사용한다.
    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}",joinPoint.getSignature());  //joinPoint signature
        return joinPoint.proceed();
    }

    //hello.aop.order 패키지와 하위 패키지이면서 타입 이름 패턴이 *Service
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작] {}",joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋] {}",joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] {}",joinPoint.getSignature());
            throw e;
        } finally {
            log.info("[리소스 릴리즈] {}",joinPoint.getSignature());
        }
    }
}
