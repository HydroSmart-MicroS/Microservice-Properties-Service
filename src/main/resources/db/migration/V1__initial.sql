CREATE TABLE properties (
    id UUID PRIMARY KEY,
    owner_user_id TEXT NOT NULL,
    name VARCHAR(120) NOT NULL,
    address VARCHAR(500) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX properties_owner_idx ON properties (owner_user_id);
CREATE TABLE units (
    id UUID PRIMARY KEY,
    property_id UUID NOT NULL REFERENCES properties(id),
    name VARCHAR(80) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (property_id, name)
);
CREATE TABLE tenancies (
    id UUID PRIMARY KEY,
    unit_id UUID NOT NULL REFERENCES units(id),
    tenant_user_id TEXT NOT NULL,
    starts_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ends_at TIMESTAMPTZ,
    CHECK (ends_at IS NULL OR ends_at > starts_at)
);
CREATE UNIQUE INDEX one_active_tenant_per_unit ON tenancies (unit_id) WHERE ends_at IS NULL;
CREATE INDEX tenancies_tenant_idx ON tenancies (tenant_user_id, starts_at);
CREATE TABLE devices (
    id UUID PRIMARY KEY,
    unit_id UUID NOT NULL REFERENCES units(id),
    serial_number VARCHAR(120) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    category VARCHAR(80) NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'registered' CHECK (state IN ('registered','active','disabled')),
    registered_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    disabled_at TIMESTAMPTZ
);
CREATE INDEX devices_unit_idx ON devices (unit_id);
-- Consumo y última lectura se calculan en telemetría; no son campos editables aquí.


CREATE TABLE outbox_events (
    event_id UUID PRIMARY KEY,
    event_type TEXT NOT NULL,
    aggregate_id TEXT NOT NULL,
    payload JSONB NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at TIMESTAMPTZ,
    attempts INTEGER NOT NULL DEFAULT 0 CHECK (attempts >= 0)
);
CREATE INDEX outbox_pending_idx ON outbox_events (occurred_at) WHERE published_at IS NULL;
CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
