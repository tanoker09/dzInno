package entity;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Sex {
    MALE("male"),
    FEMALE("female");

    private final String sex;

    Sex(final String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return sex;
    }

    public String getSex() {
        return this.sex;
    }

    private static final Map<String,Sex> ENUM_MAP;
    static {
        Map<String,Sex> map = new ConcurrentHashMap<String, Sex>();
        for (Sex instance : Sex.values()) {
            map.put(instance.getSex(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static Sex fromString(String name) {
        return ENUM_MAP.get(name);
    }
}
