-- audit table
create index concurrently if not exists audit_ts_idx on audit ("timestamp");

--drone
create index concurrently if not exists drone_st_idx on drone (state);
create index concurrently if not exists drone_bc_idx on drone (battery_capacity);