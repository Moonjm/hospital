package com.example.hospital.controller;

import com.example.hospital.dto.VisitDto.ModifyVisitRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VisitControllerTest extends CommonTest {

    @Test
    @DisplayName("환자 방문")
    void insertVisitTest() {

        try {
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.post("/v1/hospitals/{hospitalId}/patients/{patientId}/visits", "hos_000000001", "2023052010002")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("insert-visit",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호"),
                                    parameterWithName("patientId").attributes(new Attributes.Attribute("type", "String")).description("환자 등록번호")
                            ),
                            responseFields(
                                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("결과코드"),
                                    fieldWithPath("resultData").type(JsonFieldType.STRING).description("환자 방문 아이디")
                            )
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("환자 방문 상태 변경")
    void modifyVisitTest() {
        ModifyVisitRequest dto = new ModifyVisitRequest();
        dto.setStatus("3");
        try {
            String content = objectMapper.writeValueAsString(dto);
            this.mockMvc.perform(
                            RestDocumentationRequestBuilders.put("/v1/hospitals/{hospitalId}/patients/{patientId}/visits/{visitId}",
                                            "hos_000000001", "2023052010002", "1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8")
                                    .content(content)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value(1))
                    .andDo(document("modify-visit",
                            customPreProcessorRequest(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("hospitalId").attributes(new Attributes.Attribute("type", "String")).description("요양 기관 번호"),
                                    parameterWithName("patientId").attributes(new Attributes.Attribute("type", "String")).description("환자 등록번호"),
                                    parameterWithName("visitId").attributes(new Attributes.Attribute("type", "Long")).description("환자 방문 아이디")
                            ),
                            requestFields(
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("방문상태코드(1:방문중, 2:종료, 3:취소)")
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

}