package click.seichi.observerutils.contextualexecutor.executors

import click.seichi.observerutils.EffectOrErr
import click.seichi.observerutils.contextualexecutor.ContextualExecutor
import click.seichi.observerutils.contextualexecutor.RawCommandContext
import click.seichi.observerutils.utils.Logger
import click.seichi.observerutils.utils.splitFirst

data class BranchedExecutor(
    val branches: Map<String, ContextualExecutor>,
    val whenArgIsInsufficient: ContextualExecutor = PrintUsageExecutor,
    val whenBranchIsNotFound: ContextualExecutor = PrintUsageExecutor
) : ContextualExecutor {
    override fun executeWith(context: RawCommandContext): EffectOrErr {
        fun execute(executor: ContextualExecutor) = executor.executeWith(context)

        Logger.info(context.args.toString())
        return context.args.splitFirst()?.let { (head, tail) ->
            val branch = branches.getOrElse(head) { return execute(whenBranchIsNotFound) }
            val argShiftedContext = context.copy(args = tail)

            branch.executeWith(argShiftedContext)
        } ?: run { execute(whenArgIsInsufficient) }
    }

    override fun tabCandidatesFor(context: RawCommandContext): List<String> {
        return context.args.splitFirst()?.let { (head, tail) ->
            val childExecutor = branches.getOrElse(head) { return emptyList() }
            childExecutor.tabCandidatesFor(context.copy(args = tail))
        } ?: run { branches.keys.toList().sorted() }
    }
}
