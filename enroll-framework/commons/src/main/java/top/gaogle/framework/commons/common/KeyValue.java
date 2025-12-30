

package top.gaogle.framework.commons.common;

import java.util.LinkedHashMap;

/**
 * <p>
 *     KV
 * </p>
 *
 * @author gaogle
 * @since 2021-04-19 10:27
 */
public class KeyValue<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1835338328164361493L;

    public static <K, V> KeyValue<K, V> create() {
        return new KeyValue<>();
    }

    public KeyValue<K, V> entry(K key, V value) {
        super.put(key, value);
        return this;
    }

}