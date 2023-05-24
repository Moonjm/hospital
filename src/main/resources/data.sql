insert into code_group(cd, name, description)
values ('성별코드', '성별코드', '성별을 표시');
insert into code_group(cd, name, description)
values ('방문상태코드', '방문상태코드', '환자방문의 상태(방문중, 종료, 취소)');
insert into code(cd, name, code_group_id)
values ('M', '남', 1);
insert into code(cd, name, code_group_id)
values ('F', '여', 1);
insert into code(cd, name, code_group_id)
values ('1', '방문중', 2);
insert into code(cd, name, code_group_id)
values ('2', '종료', 2);
insert into code(cd, name, code_group_id)
values ('3', '취소', 2);
insert into hospital(name, institution_number, director_name)
values ('기본병원', 'hos_000000001', '기본');

insert into patient(hospital_id, name, registration_number, gender_code, birth_date, mobile_number, gender_id)
values (1, '환자1', '2023052010001', 'M', '1971-01-05', '010-1234-5678', 1),
       (1, '환자2', '2023052010002', 'M', '1971-01-06', '010-1234-5677', 1),
       (1, '환자3', '2023052010003', 'F', '1971-01-05', '010-1234-5678', 2),
       (1, '환자5', '2023052010005', 'M', '1971-03-05', '010-1234-5678', 1),
       (1, '환자7', '2023052010007', 'M', '1981-02-05', '010-1234-5678', 1),
       (1, '환자8', '2023052010008', 'M', '1976-05-15', '010-1234-5678', 1),
       (1, '환자9', '2023052010009', 'F', '1973-06-05', '010-1234-5678', 2),
       (1, '환자10', '2023052010010', 'M', '1991-11-04', '010-1234-5678', 1),
       (1, '환자11', '2023052010011', 'F', '1999-04-05', '010-1234-5678', 2);
insert into patient(hospital_id, name, registration_number)
values (1, '환자4', '2023052010004'),
       (1, '환자6', '2023052010006');

insert into patient_visit (hospital_id, patient_id, reg_date, status_id, status_code)
values (1, 2, '2023-05-23 13:00:00', '1', 3)