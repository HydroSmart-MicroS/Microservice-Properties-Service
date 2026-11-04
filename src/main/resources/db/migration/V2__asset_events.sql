CREATE OR REPLACE FUNCTION publish_asset_change() RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE row_data JSONB;event_uuid UUID:=gen_random_uuid();event_name TEXT;BEGIN
 row_data:=CASE WHEN TG_OP='DELETE' THEN to_jsonb(OLD) ELSE to_jsonb(NEW) END;
 event_name:='properties.'||TG_ARGV[0]||'.'||CASE TG_OP WHEN 'INSERT' THEN 'created' WHEN 'UPDATE' THEN 'updated' ELSE 'deleted' END||'.v1';
 INSERT INTO outbox_events(event_id,event_type,aggregate_id,payload) VALUES(event_uuid,event_name,row_data->>'id',
 jsonb_build_object('event_id',event_uuid,'event_type',event_name,'producer','properties','schema_version',1,
 'occurred_at',now(),'data',row_data));
 RETURN CASE WHEN TG_OP='DELETE' THEN OLD ELSE NEW END;
END $$;
CREATE TRIGGER property_events AFTER INSERT OR UPDATE OR DELETE ON properties FOR EACH ROW EXECUTE FUNCTION publish_asset_change('property');
CREATE TRIGGER unit_events AFTER INSERT OR UPDATE OR DELETE ON units FOR EACH ROW EXECUTE FUNCTION publish_asset_change('unit');
CREATE TRIGGER device_events AFTER INSERT OR UPDATE OR DELETE ON devices FOR EACH ROW EXECUTE FUNCTION publish_asset_change('device');
CREATE TRIGGER tenancy_events AFTER INSERT OR UPDATE OR DELETE ON tenancies FOR EACH ROW EXECUTE FUNCTION publish_asset_change('tenancy');

