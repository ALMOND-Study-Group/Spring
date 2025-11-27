package com.example.h2_practice.controller;

import com.example.h2_practice.dto.ItemRequestDto;
import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class) // Controller 계층만 로드
public class ItemControllerTest {

    // (1) MockMvc & MockBean 설정
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // JSON <-> java 객체 간 매핑

    @MockitoBean
    private ItemService itemService;

    // 더미 데이터 생성 메서드
    private Item dummyItem(Long id, String name, int price, int stock) {
        return Item.builder()
                .id(id)
                .name(name)
                .price(price)
                .stock(stock)
                .build();
    }

    /*
    @Test
    @DisplayName("GET 전체 상품 조회 : JSON 배열 응답")
    void getAllItems_Success() throws Exception{
        // Mock 데이터 준비
        List<Item> mockItems = Arrays.asList(
                Item.builder().id(1L).name("Laptop").price(9990000).stock(10).build(),
                Item.builder().id(2L).name("Mouse").price(29900).stock(50).build());

        given(itemService.getAllItems()).willReturn(mockItems);

        // GET 요청 수행 및 응답 검증
        mockMvc.perform(get("/api/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }
    */

    // (3) 전체 조회 테스트
    @Test
    @DisplayName("GET /api/items - 전체 상품 조회 성공")
    void getAllItemsTest() throws Exception {

        List<Item> items = Arrays.asList(
                dummyItem(1L, "Apple", 1000, 10),
                dummyItem(2L, "Banana", 2000, 20),
                dummyItem(3L, "Orange", 3000, 30)
        );

        given(itemService.getAllItems()).willReturn(items);

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].price").value(2000));
    }

    // (4-1) 단일 조회 성공
    @Test
    @DisplayName("GET /api/items/{id} - 단일 상품 조회 성공")
    void getItemByIdSuccess() throws Exception {

        Item item = dummyItem(1L, "Apple", 1000, 10);

        given(itemService.getItemById(1L)).willReturn(Optional.of(item));

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    // (4-2) 단일 조회 실패
    @Test
    @DisplayName("GET /api/items/{id} - 단일 상품 조회 실패 (404)")
    void getItemByIdNotFound() throws Exception {

        given(itemService.getItemById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/items/99"))
                .andExpect(status().isNotFound());
    }

    // (5) 상품 등록 테스트
    @Test
    @DisplayName("POST /api/items - 상품 등록 성공")
    void createItemTest() throws Exception {

        Item requestItem = dummyItem(null, "Mango", 5000, 5);
        Item savedItem = dummyItem(10L, "Mango", 5000, 5);

        given(itemService.createItem(any(Item.class))).willReturn(savedItem);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Mango"));
    }

    @Test
    @DisplayName("POST /api/items - 상품 등록 실패 (유효성 검증)")
    void createItemValidationFailTest() throws Exception {

        // 잘못된 요청 DTO (name이 비어 있음)
        ItemRequestDto invalidRequest = new ItemRequestDto("","", 5000, 5);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("상품 등록 실패 - 서비스 레이어 예외로 500 반환")
    void createItem_ServiceError_Returns500() throws Exception {
        // given
        Item newItem = Item.builder().name("새 상품").price(15000).stock(5).build();
        when(itemService.createItem(any(Item.class))).thenThrow(new RuntimeException("Database connection failed"));

        // 글로벌 예외 처리 시
        // when & then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andDo(print())
                .andExpect(status().isInternalServerError());


        // Controller에서 예외를 처리하지 않는 설계일 때
        /*
        assertThrows(Exception.class, () ->
                mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
        );
         */
    }

    // (6-1) 삭제 성공
    @Test
    @DisplayName("DELETE /api/items/{id} - 상품 삭제 성공")
    void deleteItemSuccess() throws Exception {

        given(itemService.deleteItem(1L)).willReturn(true);

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());
    }

    // (6-2) 삭제 실패
    @Test
    @DisplayName("DELETE /api/items/{id} - 상품 삭제 실패 (404)")
    void deleteItemNotFound() throws Exception {

        given(itemService.deleteItem(77L)).willReturn(false);

        mockMvc.perform(delete("/api/items/77"))
                .andExpect(status().isNotFound());
    }
}