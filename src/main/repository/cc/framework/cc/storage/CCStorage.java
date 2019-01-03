package repository.cc.framework.cc.storage;

import java.util.HashMap;

public class CCStorage {

    private HashMap<String, Object> storage;

    public CCStorage() {
        this.storage = new HashMap<>();
    }

    public Object store(String key, Object value) {
        this.storage.put(key, value);
        return value;
    }

    public Object get(String key) {
        Object o = this.storage.get(key);
        if (o != null) {
            return o;
        } else {
            System.out.println("NOT STORED OR MISSPELLED: " + key);
            return null;
        }
    }
}
