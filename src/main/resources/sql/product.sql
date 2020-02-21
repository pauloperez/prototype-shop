--Cleanup
drop table if exists public.article ;
drop table if exists public."order" ;
drop table if exists public.product ;

--Populate
create table public.product (
	product_id bigserial not null,
	"name" varchar not null,
	description varchar null,
	quantity int4 not null,
	price numeric not null,
	constraint product_pk primary key (product_id)
);

INSERT INTO public.product (name, description, quantity, price)
VALUES ('Soap', 'Pears baby soap for Kids', 1, 35.75);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Tooth Brush', 'Signal Tooth Brushes Size in (L, M, S)', 5, 34.50);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Shirt', 'Casual Shirt imported from France', 3, 1500.00);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Office Bag', 'Leather bag imported from USA', 40, 1000.00);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Bottle', 'Hot Water Bottles', 80, 450.45);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Wrist Watch', 'Imported wrist watches from swiss', 800, 2500.00);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Mobile Phone', '3G/4G capability', 700, 45000.00);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Shampoo', 'Head and Shoulders Shampoo', 500, 300.00);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Leather Wallets', 'Imported Leather Wallets from AUS', 1000, 500.00);
INSERT INTO public.product (name, description, quantity, price)
VALUES ('Camera', 'Imported Canon camera from USA', 10, 85000.00);


create table public."order" (
	order_id bigserial not null,
	status varchar not null,
	description varchar not null,
	value numeric not null,
	constraint order_pk primary key (order_id)
);

create table public.article (
	article_id bigserial not null,
	"name" varchar not null,
	description varchar null,
	quantity int4 not null,
	price numeric not null,
	order_id int8 not null,
	constraint article_pk primary key (article_id),
	CONSTRAINT article_order_fk FOREIGN KEY (order_id) REFERENCES public."order"(order_id)
);