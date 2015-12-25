package models.po;

import core.POBase;

import java.util.ArrayList;

/**
 * Created by buns on 12/25/15.
 * 业务线配置对象PO
 */
public class BusiLinePO extends POBase {

    /**
     * 业务线的参数存储PO
     */
    public static class BusiLineParamPO extends POBase {
        /**
         * 参数名称
         */
        private String name;
        /**
         * 参数类型
         */
        private String type;
        /**
         * 作为输入参数的节点id集合
         */
        private String in;
        /**
         * 作为输出参数的节点id集合
         */
        private String out;

        public BusiLineParamPO(String name, String type, String in, String out) {
            this.name = name;
            this.type = type;
            this.in = in;
            this.out = out;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getIn() {
            return in;
        }

        public String getOut() {
            return out;
        }
    }

    /**
     * 业务点存储PO
     */
    public static class BusiNodePO extends POBase {
        public ArrayList<SwitchCasePO> cases;
        public ArrayList<NodeSqlKeyPO> sqlkeys;
        /**
         * 业务点名称
         */
        private String name;
        /**
         * 业务点ID
         */
        private String id;
        /**
         * 业务点的下一个业务点的id
         */
        private String next;
        /**
         * 业务点的类型（normal,switch,parallel,combine)
         */
        private String type;

        public BusiNodePO(String id, String name, String next, String type) {
            this.id = id;
            this.name = name;
            this.next = next;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getNext() {
            return next;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 节点所属的业务逻辑sql配置PO
     */
    public static class NodeSqlKeyPO extends POBase {
        private String id;
        private ArrayList<BusiLineParamPO> params;

        public NodeSqlKeyPO(String id) {
            this.id = id;
            params = new ArrayList<>();
        }

        public ArrayList<BusiLineParamPO> getParams() {
            return params;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * 针对switch节点的条件配置PO
     */
    public static class SwitchCasePO extends POBase {

        public String next;

        private ArrayList<CaseConditionPO> conditions;

        public SwitchCasePO(String next) {
            this.next = next;
            conditions = new ArrayList<>();
        }

        public ArrayList<CaseConditionPO> getConditions() {
            return conditions;
        }
    }

    /**
     * 针对switch节点的条件具体配置项PO
     */
    public static class CaseConditionPO extends POBase {
        /**
         * 参数名称
         */
        private String name;
        /**
         * 标准值
         */
        private String value;
        /**
         * 参数名称对应的实际值与标准值之间应该做那种比较的符号
         * eq:等于
         * neq:不等于
         * lt:小于
         * let:小于等于
         * gt:大于
         * get:大于等于
         * in:在集合内
         * nin:不在集合内
         * idx:list列表的index索引
         * len: list列表的length长度
         */
        private String symbol;

        public CaseConditionPO(String name, String value, String symbol) {
            this.name = name;
            this.value = value;
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getSymbol() {
            return symbol;
        }
    }

}
