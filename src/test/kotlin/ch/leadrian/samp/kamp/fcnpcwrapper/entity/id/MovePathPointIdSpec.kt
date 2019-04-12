package ch.leadrian.samp.kamp.fcnpcwrapper.entity.id

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object MovePathPointIdSpec : Spek({
    describe("valueOf") {
        listOf(-1, 0, 999, 1000).forEach { value ->
            it("should return MovePathPointId with value $value") {
                val movePathPointId = MovePathPointId.valueOf(value)

                assertThat(movePathPointId.value)
                        .isEqualTo(value)
            }
        }

        listOf(0, 999).forEach { value ->
            it("should used cached MovePathPointId for value $value") {
                val movePathPointId = MovePathPointId.valueOf(value)

                assertThat(movePathPointId)
                        .isSameAs(MovePathPointId.valueOf(value))
            }
        }

        listOf(-1, 1000).forEach { value ->
            it("should create new MovePathPointId for value $value") {
                val movePathPointId = MovePathPointId.valueOf(value)

                assertThat(movePathPointId)
                        .isNotSameAs(MovePathPointId.valueOf(value))
            }
        }
    }
})