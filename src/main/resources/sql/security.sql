--Cleanup
drop table if exists "public".user_role;
drop table if exists "public"."role" ;
drop table if exists "public"."user" ;

--Populate
create table "public"."user" (
	user_id bigserial not null,
	"password" varchar not null,
	email varchar not null,
	username varchar not null,
	"name" varchar not null,
	last_name varchar not null,
	phone varchar(20) not null,
	dni_type varchar not null,
	dni varchar(20) not null,
	address varchar not null,
	city varchar(50) not null,
	active boolean not null default true,
	constraint user_pk primary key (user_id)
);
-- password in plaintext: "password"
insert into "public"."user" (user_id, password, email, username, name, last_name, phone, dni_type, dni, address, city)
values
(1, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'user@mail.com', 'user',  'Name',  'Surname',
'4111111', 'CC', '987654321', 'San Miguel 123', 'Bogota'),
(2, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'johndoe@gmail.com', 'johndoe', 'John', 'Doe',
'3113111111', 'CE', '999999999', 'San Fernando 123', 'Bogota'),
(3, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'name@gmail.com', 'namesurname', 'Name', 'Surname',
'', 'NIT', '8888888888', 'San Carlos 123', 'Bogota')
;

create table "public"."role" (
	role_id bigserial not null,
	"role" varchar not null,
	constraint role_pk primary key (role_id)
);
insert into "public"."role" (role_id, role) values (1, 'ROLE_ADMIN');
insert into "public"."role" (role_id, role) values (2, 'ROLE_USER');


create table "public"."user_role" (
	user_id int8 not null,
	role_id int8 not null,
	constraint user_role_pk primary key (user_id, role_id)
);
alter table "public".user_role ADD CONSTRAINT user_role_user_fk FOREIGN KEY (user_id) REFERENCES public."user"(user_id);
alter table "public".user_role ADD CONSTRAINT user_role_role_fk FOREIGN KEY (role_id) REFERENCES public."role"(role_id);

insert into user_role (user_id, role_id) values (1, 1);
insert into user_role (user_id, role_id) values (1, 2);
insert into user_role (user_id, role_id) values (2, 2);
insert into user_role (user_id, role_id) values (3, 2);
