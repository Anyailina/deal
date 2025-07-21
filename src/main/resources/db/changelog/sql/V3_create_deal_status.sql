--liquibase formatted sql
--changeset annill:create_table_deal_status


create table if not exists deal_status
(
    id        varchar(30) not null primary key,
    name      text       not null,
    is_active boolean    not null default true
)