package click.seichi.observerutils

import click.seichi.observerutils.commands.Command
import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.setSuspendingExecutor

class ObserverUtils : SuspendingJavaPlugin() {
    companion object {
        lateinit var PLUGIN: ObserverUtils
            private set
    }

    override suspend fun onEnableAsync() {
        PLUGIN = this
        getCommand("obs")!!.setSuspendingExecutor(Command.executor())

        Config.init()
    }
}