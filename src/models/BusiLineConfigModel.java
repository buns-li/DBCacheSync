package models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import core.IConfigPOBase;
import models.po.BusiLinePO;

import java.util.*;

/**
 * Created by buns on 12/25/15.
 */
@XStreamAlias("line")
public class BusiLineConfigModel implements IConfigPOBase<BusiLinePO> {

    @XStreamAsAttribute
    public String id;
    @XStreamAsAttribute
    public String root;

    @XStreamAsAttribute
    public String end;
    @XStreamAsAttribute
    public String clientId;

    @XStreamConverter(BusiLineConfigModel.MapFieldConverter.class)
    private Map<String, BusiLinePO.BusiLineParamPO> params;
    @XStreamConverter(BusiLineConfigModel.MapConverter2.class)
    private Map<String, BusiLinePO.BusiNodePO> nodes;

    public BusiLineConfigModel() {
        params = new HashMap<>();
        nodes = new HashMap<>();
    }

    public Map<String, BusiLinePO.BusiLineParamPO> getParams() {
        return params;
    }

    public Map<String, BusiLinePO.BusiNodePO> getNodes() {
        return nodes;
    }

    @Override
    public BusiLinePO find(String key) {
        return null;
    }

    @Override
    public BusiLinePO findAll(Map<String, String> conditions) {
        return null;
    }

    //针对一般性的无嵌套的Map类型的字段进行转换
    public static class MapFieldConverter extends AbstractCollectionConverter {

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
            BusiLinePO.BusiLineParamPO value;
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, BusiLinePO.BusiLineParamPO> entry = (Map.Entry<String, BusiLinePO.BusiLineParamPO>) iterator.next();
                value = entry.getValue();

                ExtendedHierarchicalStreamWriterHelper.startNode(writer, "param", value.getClass());

                writer.addAttribute("name", value.getName());
                writer.addAttribute("type", value.getType());
                writer.addAttribute("in", value.getIn());
                writer.addAttribute("out", value.getOut());

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
            String key;
            BusiLinePO.BusiLineParamPO queryLogicItemPO;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                key = reader.getAttribute("name");

                queryLogicItemPO = new BusiLinePO.BusiLineParamPO(
                        key,
                        reader.getAttribute("in"),
                        reader.getAttribute("out"),
                        reader.getAttribute("type"));
                map.put(key, queryLogicItemPO);

                reader.moveUp();
            }
        }
    }


    //针对有嵌套的Map类型的字段进行转换
    public static class MapConverter2 extends AbstractCollectionConverter {

        public MapConverter2(Mapper mapper) {
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
            BusiLinePO.BusiNodePO value;
            String type;
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, BusiLinePO.BusiNodePO> entry = (Map.Entry<String, BusiLinePO.BusiNodePO>) iterator.next();
                value = entry.getValue();

                ExtendedHierarchicalStreamWriterHelper.startNode(writer, "node", value.getClass());

                writer.addAttribute("name", value.getType());
                writer.addAttribute("id", value.getId());
                writer.addAttribute("next", value.getNext());

                type = value.getType();

                if (!(type == null || type.isEmpty() || type == "normal"))
                    writer.addAttribute("type", type);

                if ((type == null || type.isEmpty() || type == "normal") && value.sqlkeys != null) {
                    writer.startNode("sqlkeys");
                    for (BusiLinePO.NodeSqlKeyPO sql : value.sqlkeys) {
                        writer.startNode("sql");
                        writer.addAttribute("id", sql.getId());
                        for (BusiLinePO.BusiLineParamPO param : sql.getParams()) {
                            writer.startNode("param");
                            writer.addAttribute("name", param.getName());
                            writer.addAttribute("type", param.getType());
                            writer.endNode();
                        }
                        writer.endNode();
                    }
                    writer.endNode();
                }

                if (type == "switch" && value.cases != null) {
                    writer.startNode("cases");

                    for (BusiLinePO.SwitchCasePO switchCase : value.cases) {
                        writer.startNode("case");
                        writer.addAttribute("next", switchCase.next);

                        for (BusiLinePO.CaseConditionPO condition : switchCase.getConditions()) {
                            writer.startNode("condition");
                            writer.addAttribute("name", condition.getName());
                            writer.addAttribute("value", condition.getValue());
                            writer.addAttribute("symbol", condition.getSymbol().isEmpty() ? "eq" : condition.getSymbol());
                            writer.endNode();
                        }

                        writer.endNode();
                    }

                    writer.endNode();
                }

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
            String key, type;
            BusiLinePO.BusiNodePO nodePO;
            BusiLinePO.NodeSqlKeyPO sqlKeyPO;
            BusiLinePO.SwitchCasePO switchCasePO;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                key = reader.getAttribute("id");
                nodePO = new BusiLinePO.BusiNodePO(
                        key,
                        reader.getAttribute("name"),
                        reader.getAttribute("next"),
                        type = reader.getAttribute("type"));

                map.put(key, nodePO);

                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    if ((type == null || type.isEmpty() || type == "normal") && reader.getNodeName() == "sql") {
                        sqlKeyPO = new BusiLinePO.NodeSqlKeyPO(reader.getAttribute("id"));
                        nodePO.sqlkeys = new ArrayList<>();
                        nodePO.sqlkeys.add(sqlKeyPO);
                        while (reader.hasMoreChildren()) {
                            reader.moveDown();
                            sqlKeyPO.getParams().add(new BusiLinePO.BusiLineParamPO(
                                    reader.getAttribute("name"),
                                    reader.getAttribute("type"),
                                    null, null
                            ));
                            reader.moveUp();
                        }
                    } else if (nodePO.getType() == "switch" && reader.getNodeName() == "case") {
                        switchCasePO = new BusiLinePO.SwitchCasePO(reader.getAttribute("next"));
                        nodePO.cases = new ArrayList<>();
                        nodePO.cases.add(switchCasePO);
                        while (reader.hasMoreChildren()) {
                            reader.moveDown();
                            switchCasePO.getConditions().add(new BusiLinePO.CaseConditionPO(
                                    reader.getAttribute("name"),
                                    reader.getAttribute("value"),
                                    reader.getAttribute("symbol")
                            ));
                            reader.moveUp();
                        }
                    }
                    reader.moveUp();
                }

                reader.moveUp();
            }
        }
    }
}
