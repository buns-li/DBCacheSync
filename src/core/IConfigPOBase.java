package core;


import java.util.Map;

/**
 * Created by buns on 12/24/15.
 */
public interface IConfigPOBase<T extends POBase> {
    T find(String key);

    T findAll(Map<String, String> conditions);
}
