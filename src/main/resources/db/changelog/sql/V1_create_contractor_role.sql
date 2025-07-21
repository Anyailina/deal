--liquibase formatted sql
--changeset annill:create_table_contractor_role


create table if not exists contractor_role
(
    id        varchar(30) not null primary key,
    name      text        not null,
    category  varchar(30) not null,
    is_active boolean     not null default true
)