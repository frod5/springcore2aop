package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    // JoinPoint 인터페이스의 주요 기능
    // getArgs() : 메서드 인수를 반환합니다.
    // getThis() : 프록시 객체를 반환합니다.
    // getTarget() : 대상 객체를 반환합니다.
    // getSignature() : 조언되는 메서드에 대한 설명을 반환합니다.
    // toString() : 조언되는 방법에 대한 유용한 설명을 인쇄합니다

    // ProceedingJoinPoint는 proceed() 포함.
    // proceed() : 다음 어드바이스나 타켓을 호출한다.

    // 모든 어드바이스는 org.aspectj.lang.JoinPoint 를 첫번째 파라미터에 사용할 수 있다.
    // 단 @Around 는 ProceedingJoinPoint 을 사용해야 한다.
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            //@Before
            log.info("[트랜잭션 시작] {}",joinPoint.getSignature());
            Object result = joinPoint.proceed();
            //@AfterReturning
            log.info("[트랜잭션 커밋] {}",joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //@AfterThrowing
            log.info("[트랜잭션 롤백] {}",joinPoint.getSignature());
            throw e;
        } finally {
            //@After
            log.info("[리소스 릴리즈] {}",joinPoint.getSignature());
        }
    }

    //조인 포인트 실행 전
    //@Around 와 다르게 작업 흐름을 변경할 수는 없다.
    //@Around 는 ProceedingJoinPoint.proceed() 를 호출해야 다음 대상이 호출된다. 만약 호출하지
    //않으면 다음 대상이 호출되지 않는다. 반면에 @Before 는 ProceedingJoinPoint.proceed() 자체를 사용하지 않는다.
    //메서드 종료시 자동으로 다음 타켓이 호출된다. 물론 예외가 발생하면 다음 코드가 호출되지는 않는다.
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}",joinPoint.getSignature());
    }


    //메서드 실행이 정상적으로 반환될 때 실행
    //returning 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 한다.
    //returning 절에 지정된 타입의 값을 반환하는 메서드만 대상으로 실행한다. (부모 타입을 지정하면 모든 자식 타입은 인정된다.)
    //@Around 와 다르게 반환되는 객체를 변경할 수는 없다. 반환 객체를 변경하려면 @Around 를 사용해야 한다. 참고로 반환 객체를 조작할 수 는 있다.
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return {} return = {}]", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} , message = {}", ex);
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
