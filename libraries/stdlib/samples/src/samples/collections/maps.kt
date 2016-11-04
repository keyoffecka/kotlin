package samples.collections

import java.util.*

object Maps {

    object Instantiation {

        fun mapFromPairs() {
            val map = mapOf(1 to "x", 2 to "y", -1 to "zz")
            println(map)
        }

        fun mutableMapFromPairs() {
            val map = mutableMapOf(1 to "x", 2 to "y", -1 to "zz")
            println(map)

            map[1] = "a"
            println(map)
        }

        fun hashMapFromPairs() {
            val map: HashMap<Int, String> = hashMapOf(1 to "x", 2 to "y", -1 to "zz")
            println(map)
        }

        fun linkedMapFromPairs() {
            val map: LinkedHashMap<Int, String> = linkedMapOf(1 to "x", 2 to "y", -1 to "zz")
            println(map)
        }

        fun emptyReadOnlyMap() {
            val map = emptyMap<String, Int>()
            println("Map $map is empty: ${map.isEmpty()}")

            val anotherMap = mapOf<String, Int>()
            println("Empty maps are equal: ${map == anotherMap}")
        }

        fun emptyMutableMap() {
            val map = mutableMapOf<Int, Any?>()
            println("Map is empty: ${map.isEmpty()}")

            map[1] = "x"
            map[2] = 1.05
            println("Now map contains something: $map")
        }

    }

}

