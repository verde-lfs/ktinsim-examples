import lfs.ktinsim.packets.*
import lfs.ktinsim.KtInSim
import lfs.ktinsim.DebugProcessor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

suspend fun main(args: Array<String>) {
    var port : Int? = null
    var host: String? = null
    if (args.size == 2) {
        host = args[0]
        try {
            port = args[1].toInt()
        } catch (_: NumberFormatException) { }
    }
    print("Provide password: ")
    val password = readln()
    val processor = DebugProcessor::class
    val ktInSim =
        if (port == null) {
            KtInSim(processorClass = processor)
        } else {
            KtInSim(host!!, port, processorClass = processor)
        }
    val ktInSimDeferred = GlobalScope.async {
        ktInSim.run(
            InitPacket(
                password = password,
                receiveAXMOnLoad = true,
                receiveAXMOnEdit = true,
            )
        )
    }

    ktInSimDeferred.await()
}