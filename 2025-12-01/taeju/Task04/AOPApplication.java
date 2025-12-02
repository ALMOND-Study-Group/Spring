    package org.mycompany.test.AOP;

    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class AOPApplication implements CommandLineRunner {
        public static void main(String[] args) {
            SpringApplication.run(AOPApplication.class, args);
        }

        private final OrderService orderService;
        public AOPApplication(OrderService orderService) {
            this.orderService = orderService;
        }

        @Override
        public void run(String... args) throws Exception {
            System.out.println("=== AOP 실행 시간 측정 데모 시작 ===\n");

            orderService.createOrder("맥북 프로", 1);
            orderService.createOrder("아이폰 16", 2);
            String status = orderService.getOrderStatus(12345L);

            System.out.println("\n주문 상태: " + status);
            System.out.println("\n=== 데모 종료 ===");
        }

    }
