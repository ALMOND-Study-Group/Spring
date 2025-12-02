package org.mycompany.test.BeanCycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component//bean등록
@Scope("singleton")
public class SingletonBean {
    public SingletonBean() {
        System.out.println("싱글톤 빈 생성됨 → " + this.hashCode());
    }

    @PostConstruct
    //빈이 완전히 만들어지고
    //의존성 주입까지 끝난 직후
    public void init() {
        System.out.println("싱글톤 @PostConstruct 실행 → " + this.hashCode());
    }

    public void sayHello() {
        System.out.println("싱글톤 호출 → " + this.hashCode());
    }

    @PreDestroy//스프링 컨테이너가 종료되기 직전
    public void destroy() {
        System.out.println("싱글톤 @PreDestroy 실행 → " + this.hashCode());
    }
}
