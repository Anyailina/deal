--liquibase formatted sql
--changeset annill:create_table_currency


create table if not exists currency
(
    id        varchar(3) not null primary key,
    name      text       not null,
    is_active boolean    not null default true
)