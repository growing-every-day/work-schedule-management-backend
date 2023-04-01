-- 테스트 계정
-- ADMIN: 2
-- id: admin, password: @chan123, name: 운영자
-- id: admin1, password: @chan123, name: 운영자
-- USER: 7
-- id: chan1, password: @chan123, name: 김철수
-- id: chan2, password: @chan123, name: 이영희
-- id: chan3, password: @chan123, name: 김철수
-- id: chan4, password: @chan123, name: 이진주
-- id: chan5, password: @chan123, name: 김민재
-- id: chan6, password: @chan123, name: 이진해
-- id: chan7, password: @chan123, name: 김민지

INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (1, now(), now(), '0123456789012345O/l0MjSmiyrDf8m8AMKbgQ==', '01234567890123452HJJ+rirUMNCNPazSmEs6w==', '{bcrypt}$2a$10$Zihk9rPYtEYKWELldzr0wOu8BsGfbHVYpdii65JhZXyO6dgncuF6a', null, 25, 'ADMIN', 'admin');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (2, now(), now(), '01234567890123453NT2wiHgKa8N91jmqKpHWw==', '01234567890123452HJJ+rirUMNCNPazSmEs6w==', '{bcrypt}$2a$10$Zihk9rPYtEYKWELldzr0wOu8BsGfbHVYpdii65JhZXyO6dgncuF6a', null, 25, 'ADMIN', 'admin1');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (3, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345UxQRJCeE8z/ITPJlDOD68Q==', '{bcrypt}$2a$10$Zihk9rPYtEYKWELldzr0wOu8BsGfbHVYpdii65JhZXyO6dgncuF6a', null, 25, 'USER', 'chan1');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (4, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345Q1ZoPEGnNGtau/WgVR70jA==', '{bcrypt}$2a$10$tAjqnOTnMeNgQw6UWcB2kew6qYhC0P8VnEWiZ/yiuGL.kkdSBbHDy', null, 25, 'USER', 'chan2');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (5, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345UxQRJCeE8z/ITPJlDOD68Q==', '{bcrypt}$2a$10$wqTfFwU2iFgCJyU73I3qHuaf9e431vprMu9yCAPEKGRkVOyuyQ3C2', null, 25, 'USER', 'chan3');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (6, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345dX12feR2HaoWDmyls9NAQA==', '{bcrypt}$2a$10$SQj4onoXz.IOl976JoNUrOiCXAxbEPmJFZAcgdoJ1D4KxlkNDGFOy', null, 25, 'USER', 'chan4');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (7, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345XHtN5TiGKouzUgbDIRcN2Q==', '{bcrypt}$2a$10$TlSrlYjwsOyVnF4J9PaH0unvWOVVCHLOVUbRCWRfxXSfE7nS4bpX.', null, 25, 'USER', 'chan5');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (8, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345nP6WbW7XqNhLWwEsTC2rAg==', '{bcrypt}$2a$10$SKr4xG3yVUMzgtTqGUaG3OUfYqcS5oFBYhOxYn0cQooxuBaAXHumK', null, 25, 'USER', 'chan6');
INSERT INTO user_account (id, created_at, modified_at, email, name, password, refresh_token, remained_vacation_count, role, username) VALUES (9, now(), now(), '0123456789012345l8nS7sHT77LvQ5cuVIN+Sw==', '0123456789012345uaLlvr0Xl9icFE5AM1iq3A==', '{bcrypt}$2a$10$gRaD2DqFdR8CCJ5o6CfKrubYWI8leABk1WFk60kUlbIyvFSeqwEm.', null, 25, 'USER', 'chan7');


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