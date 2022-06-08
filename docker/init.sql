-- Table: public.user

-- Drop table

-- DROP TABLE IF EXISTS public."user";

CREATE TABLE IF NOT EXISTS public."user"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email character varying(250) NOT NULL,
    username character varying(20) NOT NULL,
    password character varying(250) NOT NULL
);

-- Table: public.user_relationship

-- DROP TABLE IF EXISTS public.user_relationship;

CREATE TABLE IF NOT EXISTS public.user_relationship
(
    email_id bigint NOT NULL,
    friend_id bigint NOT NULL,
    status character varying(50) NOT NULL,
    CONSTRAINT friend_relationship_pk PRIMARY KEY (email_id, friend_id, status),
    CONSTRAINT friend_relationship_email_id_foreign FOREIGN KEY (email_id) REFERENCES public."user" (id),
    CONSTRAINT friend_relationship_friend_id_foreign FOREIGN KEY (friend_id) REFERENCES public."user" (id)

);

-- Table: public.roles

-- DROP TABLE IF EXISTS public.roles;

CREATE TABLE IF NOT EXISTS public.roles
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(20) NOT NULL
);

-- Table: public.user_roles

-- DROP TABLE IF EXISTS public.user_roles;

CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id bigint NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_roles_fk FOREIGN KEY (user_id)
        REFERENCES public."user" (id),
    CONSTRAINT user_roles_fk_1 FOREIGN KEY (role_id)
        REFERENCES public.roles (id)
);

INSERT INTO public."user"(email, username, password)
VALUES('hongson@gmail.com', 'hongson', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('minhthong@gmail.com', 'minhthong', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('saomai@gmail.com', 'saomai', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('nguyenquang@gmail.com', 'nguyenquang', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('kienca@gmail.com', 'kienca', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('ngoctu@gmail.com', 'ngoctu', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('phamquan@gmail.com', 'phamquan', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('huynhkhanh@gmail.com', 'huynhkhanh', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('nguyenvu@gmail.com', 'nguyenvu', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56'),
('nguyenphi@gmail.com', 'nguyenphi', '$2a$10$3szy883gHAKfyGEpKLSyoemHV0GzzlGwlY.lw5XAjaWD2nCWj9y56');


INSERT INTO public."user_relationship"(email_id, friend_id, status)
VALUES(1, 2, 'FRIEND'),
(1, 3, 'FRIEND'),
(1, 4, 'FRIEND'),
(2, 1, 'FRIEND'),
(3, 1, 'FRIEND'),
(4, 1, 'FRIEND'),
(2, 3, 'FRIEND'),
(2, 5, 'FRIEND'),
(2, 4, 'FRIEND'),
(3, 2, 'FRIEND'),
(5, 2, 'FRIEND'),
(4, 2, 'FRIEND');

INSERT INTO public.roles(name)
VALUES ('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN');

INSERT INTO public.user_roles(user_id, role_id)
VALUES (1, 1),
(1, 3),
(2, 1),
(2, 2),
(3, 3);

