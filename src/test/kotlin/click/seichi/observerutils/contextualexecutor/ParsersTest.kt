package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.CommandBuildException
import click.seichi.observerutils.contextualexecutor.Parsers.listedInt
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.random.Random

class ParsersTest : BehaviorSpec({
    Given("Parsers#listedIntで") {
        val base = listOf(0, 1, 2)
        val failureMessage = ""

        When("空文字列ならば") {
            Then("Error") {
                val actual = listedInt(base, failureMessage)("")

                actual.isErr.shouldBeTrue()
                actual.error.shouldBeInstanceOf<CommandBuildException>()
            }
        }
        When("splitできない文字列が含まれているならば") {
            Then("Error") {
                setOf("a", "aiueo", "aiu123", "1/2/3", "1,2/aaaa")
                    .forAll {
                        val actual = listedInt(base, failureMessage)(it)

                        actual.isErr.shouldBeTrue()
                        actual.error.shouldBeInstanceOf<CommandBuildException>()
                    }
            }
        }
        When("splitできるがIntでない文字列が含まれているならば") {
            Then("Error") {
                setOf("a,i,u", "q,w,r", "aaa,iii,u", "qsaa,w,r,1", "1,2,a")
                    .forAll {
                        val actual = listedInt(base, failureMessage)(it)

                        actual.isErr.shouldBeTrue()
                        actual.error.shouldBeInstanceOf<CommandBuildException>()
                    }
            }
        }
        When("splitもできるしIntのみの文字列であって") {
            When("基準に含まれているIntならば") {
                Then("Ok") {
                    val actual = listedInt(base, failureMessage)(base.joinToString(","))

                    actual.isOk.shouldBeTrue()
                    actual.value.shouldBeInstanceOf<List<String>>()

                }
            }
            When("基準に含まれていないIntが含まれているならば") {
                Then("Error") {
                    (1..5).map {
                        (1..10).map { Random.nextInt() }.filterNot { base.contains(it) }.joinToString(",")
                    }.forAll {
                        val actual = listedInt(base, failureMessage)(it)

                        actual.isErr.shouldBeTrue()
                        actual.error.shouldBeInstanceOf<CommandBuildException>()

                    }
                }
            }
        }
    }
})
