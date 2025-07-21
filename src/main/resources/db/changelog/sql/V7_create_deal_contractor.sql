--liquibase formatted sql
--changeset annill:create_table_deal_contractor


create table if not exists deal_contractor
(
    id             uuid primary key not null,
    deal_id        uuid             not null,
    contractor_id  varchar(12)      not null,
    name           text             not null,
    inn            text,
    main           boolean          not null default false,
    create_date    timestamp        not null default NOW(),
    modify_date    timestamp,
    create_user_id text,
    modify_user_id text,
    is_active      BOOLEAN          not null default true,

    constraint fk_contractor_to_role_deal foreign key (deal_id) references deal (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_deal_main_contractor
    ON deal_contractor(deal_id)
    WHERE main IS TRUE;