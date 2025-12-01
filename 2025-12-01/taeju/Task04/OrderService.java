package org.mycompany.test.AOP;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public void createOrder(String name ,int price ) {
        try {
            Thread.sleep((long) (Math.random() * 300 +100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("주문생성 =" + name +" 수량 ="+ price);

    }
    public String getOrderStatus(long id ) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "배송중";
    }

}
