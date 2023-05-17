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