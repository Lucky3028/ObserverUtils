package click.seichi.observerutils.contextualexecutor

import click.seichi.observerutils.CommandBuildException
import click.seichi.observerutils.contextualexecutor.Parsers.listedInt
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.random.Random

private typealias ParseError = Err<CommandBuildException>

class ParsersTest : BehaviorSpec({
    Given("Parsers#listedIntで") {
        val base = listOf(0, 1, 2)
        val failureMessage = ""

        When("空文字列ならば") {
            Then("Error") { listedInt(base, failureMessage)("").shouldBeInstanceOf<ParseError>() }
        }
        When("splitできない文字列が含まれているならば") {
            Then("Error") {
                setOf("a", "aiueo", "aiu123", "1/2/3", "1,2/aaaa")
                    .forAll { listedInt(base, failureMessage)(it).shouldBeInstanceOf<ParseError>() }
            }
        }
        When("splitできるがIntでない文字列が含まれているならば") {
            Then("Error") {
                setOf("a,i,u", "q,w,r", "aaa,iii,u", "qsaa,w,r,1", "1,2,a")
                    .forAll { listedInt(base, failureMessage)(it).shouldBeInstanceOf<ParseError>() }
            }
        }
        When("splitもできるしIntのみの文字列であって") {
            When("基準に含まれているIntならば") {
                Then("Ok") {
                    listedInt(base, failureMessage)(base.joinToString(",")).shouldBeInstanceOf<Ok<List<String>>>()
                }
            }
            When("基準に含まれていないIntが含まれているならば") {
                Then("Error") {
                    (1..5).map {
                        (1..10).map { Random.nextInt() }.filterNot { base.contains(it) }.joinToString(",")
                    }.forAll {
                        listedInt(base, failureMessage)(it).shouldBeInstanceOf<ParseError>()
                    }
                }
            }
        }
    }
})
