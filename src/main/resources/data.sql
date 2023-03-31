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

insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (2, 2, 'LEAVE', date_format('2023/02/08', '%Y/%m/%d'), date_format('2023/02/10', '%Y/%m/%d'), 2, now());
insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (3, 2, 'LEAVE', date_format('2023/02/15', '%Y/%m/%d'), date_format('2023/03/15', '%Y/%m/%d'), 2, now());

insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (4, 2, 'LEAVE', date_format('2023/03/10', '%Y/%m/%d'), date_format('2023/03/15', '%Y/%m/%d'), 2, now());

insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (5, 2, 'LEAVE', date_format('2023/03/30', '%Y/%m/%d'), date_format('2023/04/02', '%Y/%m/%d'), 2, now());
insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (6, 2, 'LEAVE', date_format('2023/04/10', '%Y/%m/%d'), date_format('2023/04/11', '%Y/%m/%d'), 2, now());
insert into work_schedule (id, user_account_id, category, start_date, end_date, created_by, created_at)
values (7, 2, 'LEAVE', date_format('2023/04/29', '%Y/%m/%d'), date_format('2023/05/01', '%Y/%m/%d'), 2, now());