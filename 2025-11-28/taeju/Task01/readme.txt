Spring에서 외부 설정 파일(properties, yml)을 이용하여 환경 설정을 관리하는 방법을 학습합니다. 코드와 설정을 분리하여 유지보수를 용이하게 합니다.

\[해야 할 일\]

- application.properties 또는 application.yml 파일에 설정 값 작성 (예: DB 접속 정보, 메시지)
- @Value 또는 @ConfigurationProperties를 사용해 설정 값 주입
- 주입된 값을 출력하여 정상 동작 확인

\[산출물\]

- 설정 파일과 Java 코드
- 실행 결과 요약 (예: “yml 파일의 설정 값이 정상적으로 주입됨 확인”)

application.yml과 application.properties을 만들었다.
자바 파일로 만들었음

