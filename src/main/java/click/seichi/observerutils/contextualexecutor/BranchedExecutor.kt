package click.seichi.observerutils.contextualexecutor

import com.github.michaelbull.result.Result

data class BranchedExecutor(
    val branches: Map<String, ContextualExecutor>,
    val whenArgIsInsufficient: ContextualExecutor = PrintUsageExecutor,
    val whenBranchIsNotFound: ContextualExecutor = PrintUsageExecutor
) : ContextualExecutor {
    override fun executeWith(context: RawCommandContext): Result<Any, Throwable> {
        fun execute(executor: ContextualExecutor) = executor.executeWith(context)

        val (head, tail) = context.args.splitFirst()

        return head?.let {
            val branch = branches.getOrElse(it) { return execute(whenBranchIsNotFound) }
            val argShiftedContext = context.copy(args = tail)

            branch.executeWith(argShiftedContext)
        } ?: execute(whenArgIsInsufficient)
    }

    override fun tabCandidatesFor(context: RawCommandContext): List<String> {
        val (head, tail) = context.args.splitFirst()

        return head?.let {
            val childExecutor = branches.getOrElse(it) { return emptyList() }
            childExecutor.tabCandidatesFor(context.copy(args = tail))
        } ?: branches.keys.toList().sorted()
    }
}

fun <T> Collection<T>.splitFirst(): Pair<T?, List<T>> =
    this.firstOrNull()?.let { it to this.drop(1) } ?: (null to emptyList())
