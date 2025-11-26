package com.example.h2_practice.controller;

import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
  Spring Boot 3.4.0 부터 @MockBean Deprecated 되었음
  이유 :
    스프링 6.2 부터 @MockitoBean을 추가함
    그러므로 Spring Boot의 중복된 기능인 @MockBean을 제거함
*/
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item1;
    private Item item2;

    public ItemControllerTest() {
        item1 = Item.builder().id(1L).name("테스트 상품 1").description("테스트 설명 1").price(10000).stock(10).build();
        item2 = Item.builder().id(2L).name("테스트 상품 2").description("테스트 설명 2").price(20000).stock(20).build();
    }

    @Test
    @DisplayName("전체 상품 조회 테스트")
    void getAllItemsTest() throws Exception {
        // given
        List<Item> items = Arrays.asList(item1, item2);
        when(itemService.getAllItems()).thenReturn(items);

        // when & then
        mockMvc.perform(get("/api/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("테스트 상품 1"))
                .andExpect(jsonPath("$[1].name").value("테스트 상품 2"));
    }

    @Test
    @DisplayName("단일 상품 조회 테스트 - 성공")
    void getItemById_Success() throws Exception {
        // given
        given(itemService.getItemById(1L)).willReturn(Optional.of(item1));

        // when & then
        mockMvc.perform(get("/api/items/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("테스트 상품 1"));
    }

    @Test
    @DisplayName("단일 상품 조회 테스트 - 실패 (404 Not Found)")
    void getItemById_NotFound() throws Exception {
        // given
        when(itemService.getItemById(99L)).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/api/items/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void createItemTest() throws Exception {
        // given
        Item newItem = Item.builder().name("새 상품").price(15000).stock(5).build();
        Item createdItem = Item.builder().id(3L).name("새 상품").price(15000).stock(5).build();

        //MvcMock
        when(itemService.createItem(any(Item.class))).thenReturn(createdItem);

        // when & then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("새 상품"));
    }

    @Test
    @DisplayName("상품 삭제 테스트 - 성공")
    void deleteItem_Success() throws Exception {
        // given
        when(itemService.deleteItem(1L)).thenReturn(true);

        // when & then
        mockMvc.perform(delete("/api/items/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 삭제 테스트 - 실패 (404 Not Found)")
    void deleteItem_NotFound() throws Exception {
        // given
        when(itemService.deleteItem(99L)).thenReturn(false);

        // when & then
        mockMvc.perform(delete("/api/items/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("ID로 상품 조회 실패 - 잘못된 ID 형식으로 400 반환")
    void getItemById_InvalidId_Returns400() throws Exception {
        // when & then
        mockMvc.perform(get("/api/items/invalid-id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("상품 등록 실패 - 서비스 레이어 예외로 500 반환")
    void createItem_ServiceError_Returns500() throws Exception {
        // given
        Item newItem = Item.builder().name("새 상품").price(15000).stock(5).build();
        // DataAccessException은 Spring에서 자동으로 500으로 처리됨
        when(itemService.createItem(any(Item.class)))
                .thenThrow(new RuntimeException("Database operation failed") {});

        // when & then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
    @Test
    @DisplayName("상품 등록 실패 - 잘못된 요청 데이터로 400 반환")
    void createItem_InvalidRequest_Returns400() throws Exception {
        // given
        // price를 잘못된 타입으로 설정
        String invalidItemJson = "{\"name\":\"잘못된 상품\"," +
                                    "\"price\":\"invalid-price\"," +
                                    "\"stock\":-1}";


        // when & then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidItemJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("상품 삭제 실패 - 잘못된 ID 형식으로 400 반환")
    void deleteItem_InvalidId_Returns400() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/items/invalid-id"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



}