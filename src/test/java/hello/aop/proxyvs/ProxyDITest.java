package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@SpringBootTest(properties = "spring.aop.proxy-target-class=false")  //JDK 동적 프록시
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")  //JDK 동적 프록시
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;


    //실행
    //먼저 spring.aop.proxy-target-class=false 설정을 사용해서 스프링 AOP가 JDK 동적 프록시를
    //사용하도록 했다. 이렇게 실행하면 다음과 같이 오류가 발생한다.
    //실행 결과
    //BeanNotOfRequiredTypeException: Bean named 'memberServiceImpl' is expected to
    //be of type 'hello.aop.member.MemberServiceImpl' but was actually of type
    //'com.sun.proxy.$Proxy54'
    //타입과 관련된 예외가 발생한다. 자세히 읽어보면 memberServiceImpl 에 주입되길 기대하는 타입은
    //hello.aop.member.MemberServiceImpl 이지만 실제 넘어온 타입은 com.sun.proxy.$Proxy54 이다.
    //따라서 타입 예외가 발생한다고 한다.

    //@Autowired MemberService memberService : 이 부분은 문제가 없다. JDK Proxy는
    //MemberService 인터페이스를 기반으로 만들어진다. 따라서 해당 타입으로 캐스팅 할 수 있다.
    //MemberService = JDK Proxy 가 성립한다.

    //@Autowired MemberServiceImpl memberServiceImpl : 문제는 여기다. JDK Proxy는
    //MemberService 인터페이스를 기반으로 만들어진다. 따라서 MemberServiceImpl 타입이 뭔지 전혀
    //모른다. 그래서 해당 타입에 주입할 수 없다.
    //MemberServiceImpl = JDK Proxy 가 성립하지 않는다.

    //CGLIB는 문제가 없다.

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}",memberService.getClass());
        log.info("memberServiceImpl class = {}",memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
