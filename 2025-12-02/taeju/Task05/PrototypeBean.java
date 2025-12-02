package org.mycompany.test.BeanCycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeBean {

    public PrototypeBean() {
        System.out.println("프로토타입 빈 생성됨 → " + this.hashCode());
    }

    @PostConstruct //자바라서 다른 컨테이너 사용해도 사용됨
    public void init() {
        System.out.println("프로토타입 @PostConstruct 실행 → " + this.hashCode());
    }

    public void sayHello() {
        System.out.println("프로토타입 호출 → " + this.hashCode());
    }

    @PreDestroy
    public void destroy() {
        System.out.println("프로토타입 @PreDestroy 실행 → " + this.hashCode());
    }
}
