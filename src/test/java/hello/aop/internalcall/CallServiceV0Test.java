package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callService;

    @Test
    void external() {
        callService.external();

        //실행 결과를 보면 callServiceV0.external() 을 실행할 때는 프록시를 호출한다. 따라서
        //CallLogAspect 어드바이스가 호출된 것을 확인할 수 있다.
        //그리고 AOP Proxy는 target.external() 을 호출한다.
        //그런데 여기서 문제는 callServiceV0.external() 안에서 internal() 을 호출할 때 발생한다. 이때는
        //CallLogAspect 어드바이스가 호출되지 않는다.
        //자바 언어에서 메서드 앞에 별도의 참조가 없으면 this 라는 뜻으로 자기 자신의 인스턴스를 가리킨다.
        //결과적으로 자기 자신의 내부 메서드를 호출하는 this.internal() 이 되는데, 여기서 this 는 실제 대상
        //객체(target)의 인스턴스를 뜻한다. 결과적으로 이러한 내부 호출은 프록시를 거치지 않는다. 따라서
        //어드바이스도 적용할 수 없다.

        //프록시 방식의 AOP 한계
        //스프링은 프록시 방식의 AOP를 사용한다. 프록시 방식의 AOP는 메서드 내부 호출에 프록시를 적용할 수
        //없다. 지금부터 이 문제를 해결하는 방법을 하나씩 알아보자.

        //참고
        //> 실제 코드에 AOP를 직접 적용하는 AspectJ를 사용하면 이런 문제가 발생하지 않는다. 프록시를 통하는
        //것이 아니라 해당 코드에 직접 AOP 적용 코드가 붙어 있기 때문에 내부 호출과 무관하게 AOP를 적용할 수 있다.
        //> 하지만 로드 타임 위빙 등을 사용해야 하는데, 설정이 복잡하고 JVM 옵션을 주어야 하는 부담이 있다.
    }

    @Test
    void internal() {
        callService.internal();
    }
}