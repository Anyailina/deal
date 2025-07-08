--liquibase formatted sql
--changeset annill:create_table_contractor_to_role


create table if not exists contractor_to_role
(
    contractor_id uuid        not null,
    role_id       varchar(30) not null,
    is_active     boolean     not null default true,

    constraint pk_contractor_to_role primary key (contractor_id, role_id),
    constraint fk_contractor_to_role_contractor foreign key (contractor_id) references deal_contractor (id),
    constraint fk_contractor_to_role_role foreign key (role_id) references contractor_role (id)
)