package com.example.hospital.controller;

import com.example.hospital.dto.PatientDto;
import com.example.hospital.dto.PatientDto.InsertPatientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PatientControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void mockMvcSetup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가
                .alwaysDo(print()).build();
    }

    @Test
    @DisplayName("환자 등록")
    void insertPatientTest() {
        InsertPatientRequest dto = new InsertPatientRequest();
        dto.setName("이름");
        dto.setGender("F");
        dto.setBirthDate("2000-01-01");
        dto.setMobileNumber("010-1234-5678");

        try {
            String content = objectMapper.writeValueAsString(dto);
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.post("/v1/hospitals/{hospitalId}/patients", "hos_000000001")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8")
                                    .content(content)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("insert-patient",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호")
                            ),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                                    fieldWithPath("gender").type(JsonFieldType.STRING).description("성별(M:남성, F:여성)"),
                                    fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일(ex. yyyy-MM-dd)"),
                                    fieldWithPath("mobileNumber").type(JsonFieldType.STRING).description("휴대전화번호(ex. 010-xxxx-xxxx)")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("등록된 환자 등록번호")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("환자 수정")
    void modifyPatientTest() {
        PatientDto.ModifyPatientRequest dto = new PatientDto.ModifyPatientRequest();
        dto.setPatientId("2023052010002");
        dto.setName("이름1");
        dto.setGender("F");
        dto.setBirthDate("2001-01-01");
        dto.setMobileNumber("010-1234-5678");

        try {
            String content = objectMapper.writeValueAsString(dto);
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.put("/v1/hospitals/{hospitalId}/patients", "hos_000000001")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8")
                                    .content(content)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("modify-patient",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호")
                            ),
                            requestFields(
                                    fieldWithPath("patientId").type(JsonFieldType.STRING).description("환자 등록번호"),
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                                    fieldWithPath("gender").type(JsonFieldType.STRING).description("성별(M:남성, F:여성)"),
                                    fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일(ex. yyyy-MM-dd)"),
                                    fieldWithPath("mobileNumber").type(JsonFieldType.STRING).description("휴대전화번호(ex. 010-xxxx-xxxx)")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("결과메시지")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("환자 삭제")
    void deletePatientTest() {
        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.delete("/v1/hospitals/{hospitalId}/patients/{patientId}", "hos_000000001", "2023052010002")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("delete-patient",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호"),
                                    parameterWithName("patientId").attributes(new Attributes.Attribute("type", "String")).description("환자 등록번호")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("결과메시지")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("환자 상세 조회")
    void getPatientDetailTest() {
        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.get("/v1/hospitals/{hospitalId}/patients/{patientId}", "hos_000000001", "2023052010002")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("get-patient-detail",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호"),
                                    parameterWithName("patientId").attributes(new Attributes.Attribute("type", "String")).description("환자 등록번호")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                    fieldWithPath("resultData").type(JsonFieldType.OBJECT).description("결과데이터"),
                                    fieldWithPath("resultData.patientId").type(JsonFieldType.STRING).description("환자 등록번호"),
                                    fieldWithPath("resultData.name").type(JsonFieldType.STRING).description("환자명"),
                                    fieldWithPath("resultData.gender").type(JsonFieldType.STRING).description("성별(M:남성, F:여성)"),
                                    fieldWithPath("resultData.birthDate").type(JsonFieldType.STRING).description("생년월일(ex. yyyy-MM-dd)"),
                                    fieldWithPath("resultData.mobileNumber").type(JsonFieldType.STRING).description("휴대전화번호(ex. 010-xxxx-xxxx)"),
                                    fieldWithPath("resultData.visits").type(JsonFieldType.ARRAY).description("방문기록").optional(),
                                    fieldWithPath("resultData.visits.[].visitId").type(JsonFieldType.NUMBER).description("방문기록 아이디").optional(),
                                    fieldWithPath("resultData.visits.[].regDate").type(JsonFieldType.STRING).description("방문일").optional(),
                                    fieldWithPath("resultData.visits.[].status").type(JsonFieldType.STRING).description("방문상태").optional()
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("환자 목록 조회")
    void getPatientsTest() {
        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.get("/v1/hospitals/{hospitalId}/patients", "hos_000000001")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("get-patients",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("pageNo").attributes(new Attributes.Attribute("type", "Integer")).description("페이지번호, 1부터 시작, 기본값 1").optional(),
                                    parameterWithName("pageSize").attributes(new Attributes.Attribute("type", "Integer")).description("페이지 사이즈, 기본값 10").optional(),
                                    parameterWithName("type").attributes(new Attributes.Attribute("type", "String")).description("검색 타입(name: 환자명, " +
                                            "registrationNumber: 환자등록번호, birthDate: 생년월일)").optional(),
                                    parameterWithName("value").attributes(new Attributes.Attribute("type", "String")).description("검색어").optional()
                            ),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                    fieldWithPath("resultData").type(JsonFieldType.OBJECT).description("결과데이터"),
                                    fieldWithPath("resultData.content").type(JsonFieldType.ARRAY).description("화자 목록"),
                                    fieldWithPath("resultData.content.[].patientId").type(JsonFieldType.STRING).description("환자 등록번호"),
                                    fieldWithPath("resultData.content.[].name").type(JsonFieldType.STRING).description("환자명"),
                                    fieldWithPath("resultData.content.[].gender").type(JsonFieldType.STRING).description("성별(M:남성, F:여성)"),
                                    fieldWithPath("resultData.content.[].birthDate").type(JsonFieldType.STRING).description("생년월일(ex. yyyy-MM-dd)"),
                                    fieldWithPath("resultData.content.[].mobileNumber").type(JsonFieldType.STRING).description("휴대전화번호(ex. 010-xxxx-xxxx)"),
                                    fieldWithPath("resultData.content.[].lastVisit").type(JsonFieldType.STRING).description("최근 방문일").optional(),
                                    fieldWithPath("resultData.size").type(JsonFieldType.NUMBER).description("현재 조회 요청 개수"),
                                    fieldWithPath("resultData.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                    fieldWithPath("resultData.first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지 인지 여부"),
                                    fieldWithPath("resultData.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 인지 여부"),
                                    fieldWithPath("resultData.numberOfElements").type(JsonFieldType.NUMBER).description("조회된 엘리먼트 개수"),
                                    fieldWithPath("resultData.empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부"),
                                    fieldWithPath("resultData.totalElements").type(JsonFieldType.NUMBER).description("총 조회 개수")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OperationRequestPreprocessor customPreProcessorRequest() {
        return preprocessRequest(
                modifyUris()
                        .host("localhost")
                        .port(8080),
                prettyPrint());
    }
}