package com.hydrosmart.properties.infrastructure.persistence.jdbc.repositories;
import java.util.*;
import java.sql.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.hydrosmart.properties.domain.services.AssetRepository;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
import com.hydrosmart.properties.domain.model.valueobjects.*;
@Repository
public class JdbcAssetRepository implements AssetRepository {
    private final JdbcTemplate jdbc;
    public JdbcAssetRepository(JdbcTemplate jdbc) { this.jdbc=jdbc; }
    private static final RowMapper<Property> PROPERTY=(r,n)->new Property(r.getObject("id",UUID.class),r.getString("owner_user_id"),r.getString("name"),r.getString("address"),r.getTimestamp("created_at").toInstant());
    private static final RowMapper<Unit> UNIT=(r,n)->new Unit(r.getObject("id",UUID.class),r.getObject("property_id",UUID.class),r.getString("name"),r.getTimestamp("created_at").toInstant());
    private static final RowMapper<Device> DEVICE=(r,n)->new Device(r.getObject("id",UUID.class),r.getObject("unit_id",UUID.class),r.getString("serial_number"),r.getString("name"),r.getString("category"),DeviceState.parse(r.getString("state")),r.getTimestamp("registered_at").toInstant());
    private static final RowMapper<Tenancy> TENANCY=(r,n)->new Tenancy(r.getObject("id",UUID.class),r.getObject("unit_id",UUID.class),r.getString("tenant_user_id"),r.getTimestamp("starts_at").toInstant(),r.getTimestamp("ends_at")==null?null:r.getTimestamp("ends_at").toInstant());
    private <T> Optional<T> one(String sql, RowMapper<T> mapper, Object... args) { return jdbc.query(sql,mapper,args).stream().findFirst(); }
    public Optional<Property> property(UUID id) { return one("SELECT * FROM properties WHERE id=?",PROPERTY,id); }
    public Optional<Unit> unit(UUID id) { return one("SELECT * FROM units WHERE id=?",UNIT,id); }
    public Optional<Device> device(UUID id) { return one("SELECT * FROM devices WHERE id=?",DEVICE,id); }
    public Optional<Tenancy> tenancy(UUID id) { return one("SELECT * FROM tenancies WHERE id=?",TENANCY,id); }
    public Optional<UnitAccess> access(UUID unitId,String userId) {
        return one("""
            SELECT u.id,p.owner_user_id,CASE WHEN p.owner_user_id=? THEN NULL ELSE t.starts_at END AS visible_from
            FROM units u JOIN properties p ON p.id=u.property_id
            LEFT JOIN tenancies t ON t.unit_id=u.id AND t.tenant_user_id=? AND t.ends_at IS NULL AND t.starts_at<=now()
            WHERE u.id=? AND (p.owner_user_id=? OR t.id IS NOT NULL)
            """,(r,n)->new UnitAccess(r.getObject("id",UUID.class),r.getString("owner_user_id"),r.getTimestamp("visible_from")==null?null:r.getTimestamp("visible_from").toInstant()),userId,userId,unitId,userId);
    }
    public List<Property> properties(String userId) {
        return jdbc.query("""
            SELECT p.* FROM properties p WHERE p.owner_user_id=? OR EXISTS(
                SELECT 1 FROM units u JOIN tenancies t ON t.unit_id=u.id
                WHERE u.property_id=p.id AND t.tenant_user_id=? AND t.ends_at IS NULL AND t.starts_at<=now())
            ORDER BY p.created_at,p.id LIMIT 200
            """,PROPERTY,userId,userId);
    }
    public List<Unit> units(String userId,UUID propertyId) {
        var sql="""
            SELECT u.* FROM units u JOIN properties p ON p.id=u.property_id WHERE
            (p.owner_user_id=? OR EXISTS(SELECT 1 FROM tenancies t WHERE t.unit_id=u.id AND t.tenant_user_id=? AND t.ends_at IS NULL AND t.starts_at<=now()))
            """;
        return propertyId==null ? jdbc.query(sql+" ORDER BY u.created_at,u.id LIMIT 200",UNIT,userId,userId)
            : jdbc.query(sql+" AND u.property_id=? ORDER BY u.created_at,u.id LIMIT 200",UNIT,userId,userId,propertyId);
    }
    public List<Device> devices(String userId,UUID unitId) {
        var sql="""
            SELECT d.* FROM devices d JOIN units u ON u.id=d.unit_id JOIN properties p ON p.id=u.property_id WHERE
            (p.owner_user_id=? OR EXISTS(SELECT 1 FROM tenancies t WHERE t.unit_id=u.id AND t.tenant_user_id=? AND t.ends_at IS NULL AND t.starts_at<=now()))
            """;
        return unitId==null ? jdbc.query(sql+" ORDER BY d.registered_at,d.id LIMIT 200",DEVICE,userId,userId)
            : jdbc.query(sql+" AND d.unit_id=? ORDER BY d.registered_at,d.id LIMIT 200",DEVICE,userId,userId,unitId);
    }
    public List<Tenancy> tenancies(UUID unitId) { return jdbc.query("SELECT * FROM tenancies WHERE unit_id=? ORDER BY starts_at DESC LIMIT 200",TENANCY,unitId); }
    public void insert(Property p) { jdbc.update("INSERT INTO properties(id,owner_user_id,name,address,created_at) VALUES(?,?,?,?,?)",p.id(),p.ownerUserId(),p.name(),p.address(),Timestamp.from(p.createdAt())); }
    public void update(Property p) { jdbc.update("UPDATE properties SET name=?,address=? WHERE id=?",p.name(),p.address(),p.id()); }
    public void deleteProperty(UUID id) { jdbc.update("DELETE FROM properties WHERE id=?",id); }
    public void insert(Unit u) { jdbc.update("INSERT INTO units(id,property_id,name,created_at) VALUES(?,?,?,?)",u.id(),u.propertyId(),u.name(),Timestamp.from(u.createdAt())); }
    public void update(Unit u) { jdbc.update("UPDATE units SET name=? WHERE id=?",u.name(),u.id()); }
    public void deleteUnit(UUID id) { jdbc.update("DELETE FROM units WHERE id=?",id); }
    public void insert(Device d) { jdbc.update("INSERT INTO devices(id,unit_id,serial_number,name,category,state,registered_at) VALUES(?,?,?,?,?,?,?)",d.id(),d.unitId(),d.serialNumber(),d.name(),d.category(),d.state().name(),Timestamp.from(d.registeredAt())); }
    public void update(Device d) { jdbc.update("UPDATE devices SET name=?,category=?,state=?,disabled_at=CASE WHEN ?='disabled' THEN COALESCE(disabled_at,now()) ELSE NULL END WHERE id=?",d.name(),d.category(),d.state().name(),d.state().name(),d.id()); }
    public void insert(Tenancy t) {
        if(Boolean.TRUE.equals(jdbc.queryForObject("SELECT EXISTS(SELECT 1 FROM privacy_receipts WHERE user_hash=encode(sha256(convert_to(?,'UTF8')),'hex') AND expires_at>now())",Boolean.class,t.tenantUserId())))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"La cuenta está en proceso de eliminación."); jdbc.update("INSERT INTO tenancies(id,unit_id,tenant_user_id,starts_at) VALUES(?,?,?,?)",t.id(),t.unitId(),t.tenantUserId(),Timestamp.from(t.startsAt())); }
    public void endTenancy(UUID id) { jdbc.update("UPDATE tenancies SET ends_at=GREATEST(now(),starts_at+interval '1 microsecond') WHERE id=? AND ends_at IS NULL",id); }

    public Map<String,Object> deviceUsage(String userId){
        var rows=jdbc.queryForList("""
            SELECT DISTINCT d.id,d.name,d.unit_id,d.state,p.owner_user_id FROM devices d JOIN units u ON u.id=d.unit_id JOIN properties p ON p.id=u.property_id
            WHERE d.state!='disabled' AND (p.owner_user_id=? OR EXISTS(SELECT 1 FROM tenancies t WHERE t.unit_id=u.id AND t.tenant_user_id=? AND t.ends_at IS NULL AND t.starts_at<=now()))
            ORDER BY d.id
            """,userId,userId);
        var items=rows.stream().map(row->{var item=new HashMap<String,Object>(row);item.put("managed",userId.equals(row.get("owner_user_id")));item.remove("owner_user_id");return item;}).toList();
        return Map.of("count",rows.size(),"items",items);
    }
    public List<String> unitTenants(UUID id){return jdbc.queryForList("SELECT tenant_user_id FROM tenancies WHERE unit_id=? AND ends_at IS NULL AND starts_at<=now()",String.class,id);}
    public int unitDeviceCount(UUID id){return jdbc.queryForObject("SELECT count(*) FROM devices WHERE unit_id=? AND state!='disabled'",Integer.class,id);}
    public void lockQuota(String user){jdbc.queryForObject("SELECT pg_advisory_xact_lock(hashtextextended(?,71))",Object.class,user);}
    public Map<String,Object> unitSnapshot(UUID id){
        var rows=jdbc.queryForList("SELECT u.id,u.name,p.owner_user_id FROM units u JOIN properties p ON p.id=u.property_id WHERE u.id=?",id);
        if(rows.isEmpty())throw DomainException.notFound();
        var row=rows.getFirst();
        var tenants=jdbc.queryForList("SELECT tenant_user_id AS user_id,starts_at FROM tenancies WHERE unit_id=? AND ends_at IS NULL AND starts_at<=now()",id).stream()
            .map(t->Map.of("userId",t.get("user_id"),"visibleFrom",((Timestamp)t.get("starts_at")).toInstant().toString())).toList();
        return Map.of("unitId",id,"name",row.get("name"),"ownerUserId",row.get("owner_user_id"),"tenants",tenants,"devices",jdbc.queryForList("SELECT id,name FROM devices WHERE unit_id=? ORDER BY id",id));
    }
}
