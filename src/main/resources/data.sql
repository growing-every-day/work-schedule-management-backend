-- 테스트 계정
-- TODO: 테스트용 회원 추가하기
insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (1, 'chan', '{noop}chan', 'chan', 'chan@gmail.com', 'USER', 25, now(), now())
;
insert into user_account (id, username, password, name, email, role, remained_vacation_count, created_at,
                          modified_at)
values (2, 'admin', '{noop}admin', 'admin', 'admin@mail.com', 'ADMIN', 25,  now(), now())
;