-- member 생성
INSERT INTO member(user_id, password, name, nickname, role, del_status, created_at)
values('test1', '$2a$10$Vrlw6B8ugULkGsQ1Tk/0d.ziwlDPv87BDf1ISLgynKC35KtxcJ3aW', '테스트1', '닉네임1', 'ROLE_USER','N', now());

INSERT INTO member(user_id, password, name, nickname, role, del_status, created_at)
values('test2', '$2a$10$Vrlw6B8ugULkGsQ1Tk/0d.ziwlDPv87BDf1ISLgynKC35KtxcJ3aW', '테스트2', '닉네임2', 'ROLE_USER','N', now());

INSERT INTO member(user_id, password, name, nickname, role, del_status, created_at)
values('test3', '$2a$10$Vrlw6B8ugULkGsQ1Tk/0d.ziwlDPv87BDf1ISLgynKC35KtxcJ3aW', '테스트3', '닉네임3', 'ROLE_USER', 'N', now());

INSERT INTO member(user_id, password, name, nickname, role, del_status, created_at)
values('test4', '$2a$10$Vrlw6B8ugULkGsQ1Tk/0d.ziwlDPv87BDf1ISLgynKC35KtxcJ3aW', '테스트4', '닉네임4', 'ROLE_USER', 'N', now());