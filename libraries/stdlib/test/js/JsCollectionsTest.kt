package test.collections.js

import java.util.*

import kotlin.test.*
import org.junit.Test as test
import kotlin.comparisons.*

fun <T> List<T>.toArrayList() = this.toCollection(ArrayList<T>())

class JsCollectionsTest {
    val TEST_LIST = arrayOf(2, 0, 9, 7, 1).toList()
    val SORTED_TEST_LIST = arrayOf(0, 1, 2, 7, 9).toList()
    val MAX_ELEMENT = 9
    val COMPARATOR = Comparator { x: Int, y: Int ->  if (x > y) 1 else if (x < y) -1 else 0 }

    @test fun maxWithComparator() {
        assertEquals(MAX_ELEMENT, Collections.max(TEST_LIST, COMPARATOR))
    }

    @test fun sort() {
        val list = TEST_LIST.toArrayList()
        Collections.sort(list)
        assertEquals(SORTED_TEST_LIST, list)
    }

    @test fun sortWithComparator() {
        val list = TEST_LIST.toArrayList()
        Collections.sort(list, COMPARATOR)
        assertEquals(SORTED_TEST_LIST, list)
    }

    @test fun collectionToArray() {
        val array = TEST_LIST.toTypedArray()
        assertEquals(array.toList(), TEST_LIST)
    }

    @test fun toListDoesNotCreateArrayView() {
        snapshotDoesNotCreateView(arrayOf("first", "last"), { it.toList() })
        snapshotDoesNotCreateView(arrayOf<Any>("item", 1), { it.toList() })
    }

    @test fun toMutableListDoesNotCreateArrayView() {
        snapshotDoesNotCreateView(arrayOf("first", "last"), { it.toMutableList() })
        snapshotDoesNotCreateView(arrayOf<Any>("item", 2), { it.toMutableList() })
    }

    @test fun listOfDoesNotCreateView() {
        snapshotDoesNotCreateView(arrayOf("first", "last"), { listOf(*it) })
        snapshotDoesNotCreateView(arrayOf<Any>("item", 3), { listOf(*it) })
    }

    @test fun mutableListOfDoesNotCreateView() {
        snapshotDoesNotCreateView(arrayOf("first", "last"), { mutableListOf(*it) })
        snapshotDoesNotCreateView(arrayOf<Any>("item", 4), { mutableListOf(*it) })
    }

    @test fun arrayListDoesNotCreateArrayView() {
        snapshotDoesNotCreateView(arrayOf(1, 2), { arrayListOf(*it) })
        snapshotDoesNotCreateView(arrayOf<Any>("first", "last"), { arrayListOf(*it) })
    }

    @test fun arrayListCapacity() {
        val list = ArrayList<Any>(20)
        list.ensureCapacity(100)
        list.trimToSize()
        assertTrue(list.isEmpty())
    }

    @test fun listEqualsOperatesOnAny() {
        assertFalse(listOf(1, 2, 3).equals(object {}))
    }

    @test fun arrayListValidatesIndexRange() {
        val list = mutableListOf(1)
        for (index in listOf(-1, 1, 3)) {
            if (index != list.size) { // size is a valid position index
                assertFailsWith<IndexOutOfBoundsException> { list.add(index, 2) }
                assertFailsWith<IndexOutOfBoundsException> { list.addAll(index, listOf(3, 0)) }
                assertFailsWith<IndexOutOfBoundsException> { list.listIterator(index) }
            }
            assertFailsWith<IndexOutOfBoundsException> { list.removeAt(index) }
            assertFailsWith<IndexOutOfBoundsException> { list[index] }
            assertFailsWith<IndexOutOfBoundsException> { list.subList(index, index + 2) }  // tests ranges [-1, 1), [1, 3) and [3, 5)
        }
        assertEquals(listOf(1), list)
    }

    @test fun mutableIteratorRemove() {
        val a = mutableListOf(1, 2, 3)
        val it = a.iterator()
        assertFailsWith<IllegalStateException> { it.remove() }
    }

    private fun <T> snapshotDoesNotCreateView(array: Array<T>, snapshot: (Array<T>) -> List<T>) {
        val first = array.first()
        val last = array.last()

        val list = snapshot(array)
        assertEquals(first, list[0])
        array[0] = last
        assertEquals(first, list[0])
    }
}
