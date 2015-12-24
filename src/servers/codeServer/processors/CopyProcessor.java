package servers.codeServer.processors;
import core.Processor;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 */
public class CopyProcessor extends Processor{

    @Override
    public void doProcess(Map<String, String> params) {

    }

    @Override
    public void doProcess(String param) {
        System.out.println(param);
    }

    @Override
    public void doProcess(Object[] param) {

    }
}
