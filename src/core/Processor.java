package core;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 *
 * 逻辑处理基类
 */
public abstract class Processor {

    public abstract void doProcess(Map<String,String> params);

    public abstract void doProcess(String param);

    public abstract void doProcess(Object[] param);
}
