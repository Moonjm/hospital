# 프로젝트 소개

* 환자는 병원에 속하는것을 기준으로해서 API endpoint를 구성했습니다.
* 요양 기관 번호를 병원아이디로, 환자 등록 번호를 환자 아이디로, 방문아이디는 자동증가 key값으로 설정했습니다.

# DB 구성

* H2 인메모리 DB 사용
* data.sql 파일을 이용해 샘플 데이터 자동생성

# Querydsl4RepositorySupport

* 스프링 데이터가 제공하는 QuerydslRepositorySupport 가 지닌 한계를 극복하기 위해 구현된 Querydsl 지원 클래스

# ControllerAdviceConfig

* 예외를 공통으로 처리하기 위한 클래스

# API 문서

* RestDocs를 활용해서 생성
* 프로젝트 실행 후 [http://localhost:8080/api.html](http://localhost:8080/api.html) 접속