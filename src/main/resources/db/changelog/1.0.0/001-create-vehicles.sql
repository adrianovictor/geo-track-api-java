--liquibase formatted sql

--changeset adrianovictor:001-create-vehicles context:main
create sequence vehicles_seq start with 1 increment by 50;

create table vehicles (
    id               bigint       not null,
    unique_id        uuid         not null,
    renavam          varchar(11)  not null,
    plate            varchar(7)   not null,
    model            varchar(50)  not null,
    brand            varchar(50)  not null,
    manufacture_year integer      not null,
    status           smallint     not null,
    version          bigint       not null default 0,
    created_at       timestamptz  not null,
    updated_at       timestamptz,
    constraint pk_vehicles primary key (id),
    constraint uk_vehicles_unique_id unique (unique_id),
    constraint ck_vehicles_status check (status between 1 and 4)
);

-- Unicidade apenas entre veículos não excluídos (status 4 = DELETED): permite recadastrar
-- um veículo que foi excluído logicamente.
create unique index ux_vehicles_renavam_not_deleted on vehicles (renavam) where status <> 4;
create unique index ux_vehicles_plate_not_deleted on vehicles (plate) where status <> 4;

--rollback drop table vehicles;
--rollback drop sequence vehicles_seq;