-- 테스트 계정
-- TODO: 테스트용 회원 추가하기
insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (1, 'chan', '{noop}chan', '김철수', 'chan@gmail.com', 'USER', 25, now(), now())
;
insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (2, 'admin', '{noop}admin', '김철수', 'admin@mail.com', 'ADMIN', 25,  now(), now())
;

insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (3, 'chan1', '{noop}chan1', '이민호', 'chan@gmail.com', 'USER', 25, now(), now())
;

insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (4, 'chan2', '{noop}chan2', '이순자', 'chan@gmail.com', 'USER', 25, now(), now())
;

insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (5, 'chan3', '{noop}chan3', '김순자', 'chan@gmail.com', 'USER', 25, now(), now())
;


--work_schedule
insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (1, 1, 'DUTY', now(), now(), 1, now());