package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathIdSpec : Spek({
    describe("valueOf") {
        listOf(-1, 0, 999, 1000).forEach { value ->
            it("should return MovePathId with value $value") {
                val movePathId = MovePathId.valueOf(value)

                assertThat(movePathId.value)
                        .isEqualTo(value)
            }
        }

        listOf(0, 999).forEach { value ->
            it("should used cached MovePathId for value $value") {
                val movePathId = MovePathId.valueOf(value)

                assertThat(movePathId)
                        .isSameAs(MovePathId.valueOf(value))
            }
        }

        listOf(-1, 1000).forEach { value ->
            it("should create new MovePathId for value $value") {
                val movePathId = MovePathId.valueOf(value)

                assertThat(movePathId)
                        .isNotSameAs(MovePathId.valueOf(value))
            }
        }
    }
})