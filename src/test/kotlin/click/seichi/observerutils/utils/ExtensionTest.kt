package click.seichi.observerutils.utils

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ExtensionTest : BehaviorSpec({
    Given("Collection#splitFirstで") {
        When("Collectionの要素が0個であれば") {
            Then("Option.Noneが返る") {
                emptyList<Int>().splitFirst() shouldBe null
            }
        }
        When("Collectionの要素が1個であれば") {
            Then("先頭の要素と空のListが返る") {
                val list = listOf(1)
                list.splitFirst() shouldBe (list[0] to emptyList())
            }
        }
        When("Collectionの要素が2個以上であれば") {
            Then("先頭の要素1個と、残りの要素を含むListが返る") {
                val list = listOf(1, 2)
                list.splitFirst() shouldBe (list[0] to list.drop(1))

                val list2 = listOf("a", "b", "c")
                list2.splitFirst() shouldBe (list2[0] to list2.drop(1))
            }
        }
    }

    Given("Collection#orEmptyで") {
        val default = "default!"
        When("Collectionの要素が0個であれば") {
            Then("defaultの値が返る") {
                emptyList<Int>().orEmpty(default) shouldBe default
            }
            Then("formatterが明示的に指定されても、defaultの値が返る") {
                emptyList<Int>().orEmpty(default) { it.toString() } shouldBe default
            }
        }
        When("Collectionの要素が1個以上であれば") {
            val list = listOf(0, 1, 2)
            Then("formatter関数が適用された値が返る") {
                val formatter: (Collection<Int>) -> String = { it.reversed().toString() }
                list.orEmpty(default, formatter) shouldBe formatter(list)
            }
            Then("formatterが明示的に指定されなければ、デフォルトの関数が適用された値が返る") {
                list.orEmpty(default) shouldBe list.toString()
            }
        }
    }
})
