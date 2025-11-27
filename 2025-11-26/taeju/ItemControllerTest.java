package com.example.h2_practice.controller;

import com.example.h2_practice.dto.ItemRequestDto;
import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class) // Controller 계층만 로드
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // JSON <-> java 객체 간 매핑

    @MockitoBean //이건 최신 스프링 부트 3.0에 나온거래,
    private ItemService itemService;
    // 테스트용 목 데이터 (Item 엔티티 직접 사용)
    private Item laptop;
    private Item mouse;

    /*
     * 실행전에 아이템들 넣어주고 서비스 메서드 들을 넣어주고
     *
     * */
    @BeforeEach
    void setUp() {
        laptop = Item.builder()
                .id(1L).name("Laptop").description("laptop").price(100).stock(10).build();
        mouse = Item.builder()
                .id(2L).name("Mouse").description("mouse").price(500).stock(30).build();

        when(itemService.getAllItems()).thenReturn(Arrays.asList(laptop, mouse));
        when(itemService.getItemById(1L)).thenReturn(Optional.of(laptop));
        when(itemService.getItemById(2L)).thenReturn(Optional.of(mouse));

    }


    @Test
    @DisplayName("GET 전체 상품 조회 : JSON 배열 응답")
    void getAllItems_Success() throws Exception {
        // Mock 데이터 준비
        // GET 요청 수행 및 응답 검증
        mockMvc.perform(get("/api/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));
    }

    @Test
    @DisplayName("단일 상품 조회 테스트_성공")
    void getIDItems_Success() throws Exception { //성공
        mockMvc.perform(get("/api/items/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    @DisplayName("단일 상품 조회 테스트_실패")
    void getIDItems_Failure() throws Exception {
        mockMvc.perform(get("/api/items/999")) //id를 999로 했음
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 등록 테스트_성공")
    void createItem_Success() throws Exception {

        //요청문 만들기. 클라이언트 입장인거지
        //가자 http 요청
        ItemRequestDto req = new ItemRequestDto();
        req.setName("Gopro");
        req.setDescription("gopro");
        req.setPrice(100);
        req.setStock(10);

        //위에 것들을 저장. 지금 클라이언트 입장에서 서버로 보내기 때문에 dto를 사용하는게 맞음
        //서비스는 엔티티 db에 저장을 해얒하니깐

        Item savedItem = Item.builder()
                .id(3L)
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .build();

        //이건 서비스단에서 crate하니깐 거기에서 item을 받는다.
        // → "만약에 누가 ~~ 물어보면, ~~이라고 대답해!" 라고 미리 가르침
        when(itemService.createItem(any(Item.class))).thenReturn(savedItem);
        // → "야, 서비스야! 나중에 누가 1번 상품 달라고 하면 이거 줘. 알겠지?"
        // 서비스: "네 알겠습니다!" (가짜라서 무조건 복종함)

        //즉 → "만약에 createItem(어떤 Item이든) 이 호출되면, 우리가 만든 savedItem을 리턴해!"
        //그리고 tip! when없으면 정말로 실행됨.



        /*
         * 윗에는 사전 설정에 가까움. 역활지정인거지
         * 아래는 정말 테스트 해보는 거임.
         * */

        //1.요청 보내기
        //이건 json 형태이고 보내는 요청본문이 json문자열로 바꿔주어야되,
        //만약에 응답도 json으로 달라고 할려면 accept하면됨
        mockMvc.perform(post("/api/items").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))) // when에서 이걸 보고 실행하는듯
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Gopro"));

    }

    @Test
    @DisplayName("상품 삭제_성공")
    void deleteItem_Success() throws Exception {
        //Given(준비)
        when(itemService.deleteItem(1L)).thenReturn(true);

        //When(실행)
        mockMvc.perform(delete("/api/items/1"))
                //Then(검증)
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("상푹 삭제_실패")
    void deleteItem_Failure() throws Exception {
        //Given(준비)
        when(itemService.deleteItem(99L)).thenReturn(true);
        //When(실행)
        /*mockMvc.perform(delete("/api/items/999"))
                //Then(검증)
                .andDo(print())
                .andExpect(status().isNotFound()); //Expect = 예상하다. || 그리고 나서 ~를 기대한다
*/
        MvcResult result = mockMvc.perform(delete("/api/items/999"))
                        .andDo(print()).andReturn(); //결과받기

        assertThat(result.getResponse().getStatus())
                .as("실패")
                .isEqualTo(HttpStatus.NOT_FOUND.value());

        assertThat(result.getResponse().getContentAsString())
                .as("비어있는거")
                .isEqualTo("");

    }


}
