package models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import core.IConfigPOBase;
import models.po.DbCacheTableColPO;
import models.po.DbCacheTablePO;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by buns on 12/24/15.
 */
@XStreamAlias("db")
public final class DbCacheConfigModel implements IConfigPOBase<DbCacheTablePO> {

    @XStreamConverter(MapFieldConverter.class)
    private Map<String, DbCacheTablePO> tables = new HashMap<>();

    /**
     * 添加查询逻辑项
     */
    public void addItem(String key, String name, String sqlKey, String getNewSqlKey) {
        tables.put(key, new DbCacheTablePO(key, name, sqlKey, getNewSqlKey));
    }

    @Override
    public DbCacheTablePO find(String key) {
        return tables.get(key);
    }

    public Map<String, DbCacheTablePO> getAll() {
        return tables;
    }

    /**
     * 针对Map对象的xml转换容器
     */
    public static class MapFieldConverter extends MapConverter {

        public MapFieldConverter(Mapper mapper) {
            super(mapper);
        }

        @Override
        public boolean canConvert(Class aClass) {
            boolean result = aClass.equals(HashMap.class)
                    || aClass.equals(Hashtable.class)
                    || aClass.getName().equals("java.util.LinkedHashMap")
                    || aClass.getName().equals("sun.font.AttributeMap");
            return result;
        }

        @Override
        public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
            Map map = (Map) o;
            DbCacheTablePO value;
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, DbCacheTablePO> entry = (Map.Entry<String, DbCacheTablePO>) iterator.next();
                value = entry.getValue();
                ExtendedHierarchicalStreamWriterHelper.startNode(writer, "table", value.getClass());
                writer.addAttribute("key", value.getKey());
                writer.addAttribute("name", value.getName());
                writer.addAttribute("sqlKey", value.getSqlKey());
                writer.addAttribute("getNewSqlKey", value.getGetNewSqlKey());

                writer.startNode("cols");

                for (DbCacheTableColPO col : value.getCols()) {
                    writer.startNode("col");

                    writer.addAttribute("name", col.getName());
                    writer.addAttribute("index", col.getIndex().toString());
                    writer.addAttribute("length", col.getLength().toString());
                    writer.addAttribute("type", col.getType());
                    if (!col.getFieldType().isEmpty())
                        writer.addAttribute("fieldType", col.getFieldType());
                    if (!col.getFromTb().isEmpty())
                        writer.addAttribute("fromTb", col.getFromTb());

                    writer.endNode();
                }
                writer.endNode();
                writer.endNode();
            }
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            HashMap map = (HashMap) createCollection(context.getRequiredType());
            populateMap(reader, context, map);
            return map;
        }

        protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map) {
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                String key = reader.getAttribute("key");
                DbCacheTablePO queryLogicItemPO = new DbCacheTablePO(key,
                        reader.getAttribute("name"),
                        reader.getAttribute("sqlKey"),
                        reader.getAttribute("getNewSqlKey"));
                map.put(key, queryLogicItemPO);
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    if (reader.getNodeName() == "col") {
                        queryLogicItemPO.getCols().add(new DbCacheTableColPO(
                                reader.getAttribute("name"),
                                reader.getAttribute("type"),
                                Integer.parseInt(reader.getAttribute("length")),
                                Integer.parseInt(reader.getAttribute("index")),
                                reader.getAttribute("fieldType"),
                                reader.getAttribute("fromTb")
                        ));
                    }
                    reader.moveUp();
                }
                reader.moveUp();
            }
        }
    }
}
