--liquibase formatted sql
--changeset annill:create_table_deal


create table if not exists deal
(
    id uuid primary key not null ,
    description text,
    agreement_number text,
    agreement_date date,
    agreement_start_dt timestamp,
    availability_date date,
    type_id varchar(30),
    status_id varchar(30) not null,
    close_dt timestamp,
    create_date timestamp not null default NOW(),
    modify_date timestamp,
    create_user_id text,
    modify_user_id text,
    is_active boolean not null default true,

    constraint fk_deal_deal_type foreign key (type_id) references deal_type(id),
    constraint fk_deal_deal_status foreign key (status_id) references deal_status(id)

)