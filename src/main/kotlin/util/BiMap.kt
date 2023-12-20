package util

class BiMap<K,V> {
    private val aMap = mutableMapOf<K, V>()
    private val bMap = mutableMapOf<V, K>()

    fun put(k: K, v: V) {
        aMap[k] = v
        bMap[v] = k
    }

    fun keys(): Set<K> {
        return aMap.keys.toSet()
    }

    fun getValueFromKey(k: K) = aMap[k]

    fun getKeyFromValue(v: V) = bMap[v]

    fun containsKey(k: K) = aMap.containsKey(k)

    fun containsValue(v: V) = bMap.containsKey(v)

    override fun toString(): String {
        return aMap.toString()
    }
}