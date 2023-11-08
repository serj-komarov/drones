drop table if exists audit;
create table audit
(
    id               bigint generated by default as identity primary key,
    serial_number    varchar,
    battery_capacity integer not null,
    "timestamp"      TIMESTAMPTZ default now()
);