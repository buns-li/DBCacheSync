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
import models.po.QueryLogicItemPO;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by buns on 12/24/15.
 */
@XStreamAlias("querylogic")
public class QueryLogicConfigModel implements IConfigPOBase<QueryLogicItemPO> {

    @XStreamConverter(MapFieldConverter.class)
    private Map<String, QueryLogicItemPO> sqls = new HashMap<String, QueryLogicItemPO>();

    /**
     * 添加查询逻辑项
     *
     * @param key
     * @param cmd
     */
    public void addItem(String key, String cmd) {
        sqls.put(key, new QueryLogicItemPO(key, cmd));
    }

    @Override
    public QueryLogicItemPO find(String key) {
        return sqls.get(key);
    }

    @Override
    public QueryLogicItemPO findAll(Map<String, String> conditions) {
        return null;
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
            QueryLogicItemPO value;
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, QueryLogicItemPO> entry = (Map.Entry<String, QueryLogicItemPO>) iterator.next();
                value = entry.getValue();

                ExtendedHierarchicalStreamWriterHelper.startNode(writer, "sql", value.getClass());

                writer.addAttribute("key", value.getKey());
                writer.startNode("cmd");
                writer.setValue(value.getCmd());
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
                QueryLogicItemPO queryLogicItemPO;
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    queryLogicItemPO = new QueryLogicItemPO(key, reader.getValue());
                    map.put(key, queryLogicItemPO);
                }
                reader.moveUp();
            }
        }
    }
}
