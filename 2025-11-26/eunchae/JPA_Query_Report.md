# JPA 파생 쿼리 메서드(Derived Query Methods) 규칙 정리
## 1. 개요 정리

파생 쿼리 메서드는 Spring Data JPA가 메서드 이름을 분석하여 자동으로 쿼리를 생성하는 기능이다.  
개발자가 SQL작성 없이도 Repository 계층의 생산성을 극대화하며 메서드 시그니처가 엔티티 필드명과 일치하지 않으면 컴파일 시점에 오류를 잡아내는 타입 안전성을 확보한다는 장점이 있다.  
하지만 복잡한 쿼리에서는 메서드 이름이 길어지고 가독성이 떨어지며 유지보수가 어려워진다는 한계가 있다.  
`JOIN FETCH`나 Subquery, 동적 쿼리 조건, 특정 DB 함수 사용이 필요한 경우, 파생 쿼리만으로는 한계가 있어 `@Query`(JPQL 또는 HGL), `@NativeQuery`(Native SQL) 어노테이션이 필요하다.

## 2. 규칙 표 작성

| 유형 | 키워드 | 설명 | Item 예시 메서드  | 
 | ----- | ----- | ----- | ----- | 
| **접두사** | `find...By`, `read...By`, `get...By` | 쿼리 시작을 알리는 접두사, `By` + 속성 | `Item findById(Long id)` | 
| **비교 (범위)** | `Between` | 두 값 사이에 속하는 레코드를 찾음| `List findByPriceBetween(Integer min, Integer max)` | 
| **비교 (부등호)** | `GreaterThanEqual`, `LessThan` | 주어진 값보다 크거나 같거나 / 작은 레코드를 찾음 | `List findByStockGreaterThanEqual(Integer stock)` | 
| **비교 (문자열)** | `Containing`, `Like` | 문자열 패턴 매칭을 수행 | `List findByDescriptionContaining(String keyword)` | 
| **비교 (옵션)** | `IgnoreCase` | 문자열 비교 시 대소문자를 무시 | `List findByNameIgnoreCase(String name)` | 
| **논리 연산** | `And`, `Or` | 여러 조건을 논리적으로 결합 | `List findByNameAndPrice(String name, Integer price)` | 
| **집합** | `In`, `Not` | 주어진 컬렉션 내에 포함 / 조건 부정 | `List findByNameIn(List<String> names)` | 
| **정렬** | `OrderBy...Asc/Desc` | 특정 속성 기준으로 결과를 오름차순/내림차순 정렬 | `List findByNameOrderByPriceDesc(String name)` | 
| **페이징/정렬** | `Pageable`, `Sort` | 런타임에 페이징 및 정렬 조건을 인자로 받음 | `Page<Item> findAll(Pageable pageable)` |

## 3. Item 예시 메서드 8개 선정

| 시그니처 | 의미 | 
 | ----- | ----- | 
| `List findByNameContainingIgnoreCase(String q)` | 이름에 q 문자열이 포함된 모든 상품을 대소문자 구분 없이 찾음 | 
| `List findByPriceBetween(Integer min, Integer max)` | 가격이 min값과 max값 사이에 있는 모든 상품을 찾음 | 
| `List findByStockGreaterThanEqualOrderByPriceDesc(Integer stock)` | 재고가 stock 이상인 상품을 찾고 결과를 가격 내림차순으로 정렬 | 
| `Optional<Item> findFirstByNameOrderByIdDesc(String name)` | 이름이 name인 상품 중 ID 기준 가장 최근에 등록된 첫 번째 상품을 찾음 | 
| `boolean existsByName(String name)` | 이름이 name과 일치하는 상품이 데이터베이스에 존재하는지 여부를 반환 | 
| `long countByNameContaining(String q)` | 이름에 q 문자열이 포함된 상품이 총 몇 개인지 개수를 반환 | 
| `List findByDescriptionIsNull()` | `description` 필드 값이 NULL인 모든 상품 목록을 찾음 | 
| `List findByNameIn(List<String> names, Sort sort)` | 이름이 names 목록 중 하나와 일치하는 상품을 찾고 sort 객체 기준으로 정렬 |

## 4. 페이징/정렬 요약

페이징(Paging)과 정렬(Sorting)은 대량의 데이터를 효율적으로 조회하는 방법이다.  
Spring Data JPA에서는 `Pageable` 객체를 사용하여 이를 처리하며, 이 객체는 '몇 번째 페이지', '페이지당 항목 개수', '정렬 기준' 세 가지 정보를 담아 repository에 전달한다.  
  
<사용법>
```
// 리포지토리 호출부 예시
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

// 1. Pageable 객체 생성: PageRequest.of(페이지 번호, 페이지 크기, 정렬 기준)
Pageable pageable = PageRequest.of(
    0,                                      // 첫 번째 페이지 (페이지 번호는 0부터 시작)
    5,                                      // 페이지당 항목 개수 (5개)
    Sort.by(Sort.Direction.DESC, "price")   // 정렬 기준: 'price' 필드를 내림차순(DESC)으로 정렬
);

// 2. Repository 호출: 10000원 이상의 상품을 위 Pageable 조건에 따라 조회
// 이 호출은 "가격이 10,000원보다 높은 Item 중 첫 페이지의 5개를 가격 순으로" 요청
Page<Item> itemPage = itemRepository.findByPriceGreaterThan(10000, pageable); // itemRepository는 가정된 리포지토리

// 3. 결과 사용: Page 객체에서 실제 데이터 목록과 메타 정보를 추출
List<Item> items = itemPage.getContent();       // 실제 Item 객체 리스트 (5개 이하)
long totalElements = itemPage.getTotalElements(); // 전체 데이터 개수 (페이징과 무관한 총 개수)
int totalPages = itemPage.getTotalPages();      // 전체 페이지 수
```

## 5. 함정/베스트프랙티스 5가지

| 구분 | 함정 | 베스트 프랙티스 |
| :--- | :--- | :--- |
| **속성명 불일치** | 메서드명 속성명(예: `findByPric`)과 엔티티 필드명(`price`)이 다르면 런타임 시 오류가 발생 | IDE의 자동 완성 기능을 사용하여 속성명을 정확히 입력, 오타를 방지 |
| **긴 메서드명 복잡도↑** | 조건이 3개 이상 복합되면 메서드명이 길어져 가독성 및 유지보수성이 크게 떨어짐| 복잡한 쿼리는 `@Query`(JPQL) 또는 QueryDSL을 사용하여 쿼리 로직을 명확하게 분리 |
| **관계경로 네이밍** | 연관된 엔티티의 필드에 접근할 때 엔티티명+속성명 (`findByMemberName`) 규칙을 사용 | 복잡한 관계경로 쿼리는 `@Query`를 사용하여 명시적인 `JOIN FETCH`를 작성하는게 좋음 |
| **숫자/문자 비교 오해** | `Containing`이나 `Like` 키워드는 문자열(`String`) 타입에만 유효, 숫자 필드에 적용하면 오류 발생 | 데이터 타입에 맞는 키워드(`Between`, `GreaterThan`)를 사용 |
| **성능 이슈 시 @Query/인덱스 고려** | `Containing`으로 생성된 쿼리는 인덱스를 타기 어려워 대용량 데이터에서 성능이 저하됨 | 성능 분석 후, DB 인덱스를 설정하거나 `@Query`를 사용하여 인덱스를 활용할 수 있는 쿼리로 직접 튜닝 |