import kotlin.serialization.KInput
import kotlin.serialization.KOutput
import kotlin.serialization.KSerializable
import kotlin.serialization.KSerializer
import kotlin.serialization.KSerialClassDesc

// Custom serializer

@KSerializable(BoxSerializer::class)
data class Box(
        val _value1: String,
        val _value2: Int
)

object BoxSerializer : KSerializer<Box> {
    val desc = object : KSerialClassDesc {
        override val name = "Box"
        override val isArray: Boolean = false
        override fun getElementCount(value: Any?) = 2
        override fun getElementName(index: Int) = when(index) {
            0 -> "value1"
            1 -> "value2"
            else -> ""
        }
        override fun getElementIndex(name: String) = when(name) {
            "value1" -> 0
            "value2" -> 1
            else -> -1
        }
    }

    override fun save(output: KOutput, obj : Box) {
        output.writeBegin(desc)
        output.writeStringElementValue(desc, 0, obj._value1)
        output.writeIntElementValue(desc, 1, obj._value2)
        output.writeEnd(desc)
    }

    override fun load(input: KInput): Box {
        input.readBegin(desc)
        if (input.readElement(desc) != 0) throw java.lang.IllegalStateException()
        val value1 = input.readStringElementValue(desc, 0)
        if (input.readElement(desc) != 1) throw java.lang.IllegalStateException()
        val value2 = input.readIntElementValue(desc, 1)
        if (input.readElement(desc) != KInput.READ_DONE) throw java.lang.IllegalStateException()
        input.readEnd(desc)
        return Box(value1, value2)
    }
}

fun box() : String {
    val out = Out()
    //out.write(Box, Box("s1", 42)) // NO_COMPANION_OBJECT
    out.write(BoxSerializer, Box("s1", 42))
    out.done()

    val inp = Inp()
    //val box = inp.read(Box) // NO_COMPANION_OBJECT
    val box = inp.read(BoxSerializer)
    inp.done()

    return "OK"
}

class Failure(s: String) : Throwable(s)

open class State {
    var step = 0

    fun fail(msg: String): Nothing = throw Failure("@$step: $msg")

    fun checkBoxDesc(desc: KSerialClassDesc) {
        if (desc.name != "Box") fail("checkBoxDesc name $desc")
        if (desc.getElementName(0) != "value1") fail("checkBoxDesc0 $desc")
        if (desc.getElementName(1) != "value2") fail("checkBoxDesc1 $desc")
    }
}

class Out() : State(), KOutput {
    override fun writeBegin(desc: KSerialClassDesc) {
        checkBoxDesc(desc)
        if (step == 0) step++ else fail("writeBegin($desc)")
    }

    override fun writeStringElementValue(desc: KSerialClassDesc, index: Int, value: String) {
        checkBoxDesc(desc)
        when (step) {
            1 -> if (index == 0 && value == "s1") { step++; return }
        }
        fail("writeStringElementValue($desc, $index, $value)")
    }

    override fun writeIntElementValue(desc: KSerialClassDesc, index: Int, value: Int) {
        checkBoxDesc(desc)
        when (step) {
            2 -> if (index == 1 && value == 42) { step++; return }
        }
        fail("writeIntElementValue($desc, $index, $value)")
    }

    override fun writeEnd(desc: KSerialClassDesc) {
        checkBoxDesc(desc)
        if (step == 3) step++ else fail("writeEnd($desc)")
    }

    fun done() {
        if (step != 4) fail("OUT FAIL")
    }
}

class Inp() : State(), KInput {
    override fun readBegin(desc: KSerialClassDesc) {
        checkBoxDesc(desc)
        if (step == 0) step++ else fail("readBegin($desc)")
    }

    override fun readElement(desc: KSerialClassDesc): Int {
        checkBoxDesc(desc)
        when (step) {
            1 -> { step++; return 0 }
            3 -> { step++; return 1 }
            5 -> { step++; return -1 }
        }
        fail("readElement($desc)")
    }

    override fun readStringElementValue(desc: KSerialClassDesc, index: Int): String {
        checkBoxDesc(desc)
        when (step) {
            2 -> if (index == 0) { step++; return "s1" }
        }
        fail("readStringlementValue($desc, $index)")
    }

    override fun readIntElementValue(desc: KSerialClassDesc, index: Int): Int {
        checkBoxDesc(desc)
        when (step) {
            4 -> if (index == 1) { step++; return 42 }
        }
        fail("readIntElementValue($desc, $index)")
    }

    override fun readEnd(desc: KSerialClassDesc) {
        checkBoxDesc(desc)
        if (step == 6) step++ else fail("readEnd($desc)")
    }

    fun done() {
        if (step != 7) fail("INP FAIL")
    }
}
