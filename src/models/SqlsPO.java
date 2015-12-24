package models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

/**
 * Created by buns on 12/24/15.
 */

@XStreamAlias("Sqls")
public class SqlsPO {
    @XStreamImplicit
    private ArrayList<Sql> sqls = new ArrayList<>();

    public ArrayList<Sql> getSqls() {
        return sqls;
    }

    public void addSql(String key, String cmd) {
        sqls.add(new Sql(key, cmd));
    }

    @Override
    public String toString() {
        String s = "";
        for (Sql sql : sqls)
            s += sql.cmd + ",";
        return s;
    }

    @XStreamAlias("Sql")
    public static class Sql {

        @XStreamAsAttribute()
        @XStreamAlias("logic")
        private String key;

        private String cmd;

        public Sql(String key, String cmd) {
            this.cmd = cmd;
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public String getCmd() {
            return cmd;
        }
    }
}
