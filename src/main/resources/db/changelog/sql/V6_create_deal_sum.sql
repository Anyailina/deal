--liquibase formatted sql
--changeset annill:create_table_deal_sum


create table if not exists deal_sum
(
    id          bigserial       not null primary key,
    deal_id     uuid            not null,
    sum         numeric(100, 2) not null,
    currency_id varchar(3)      not null,
    is_main     boolean         not null default false,
    is_active   boolean         not null default true,

    constraint fk_dealt_sum_deal foreign key (deal_id) references deal (id),
    constraint fk_deal_sum_currency foreign key (currency_id) references currency (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_deal_main_sum
    ON deal_sum (deal_id)
    WHERE is_main IS TRUE;