package kz.edu.astanait.usertest.spock

import spock.lang.Specification
import spock.lang.Unroll

class MathTest extends Specification {
    @Unroll
    def "#a + #b equals #c"() {
        expect:
            Math.addExact(a,b) == c
        where:
            a | b | c
            1 | 2 | 3
            2 | 2 | 4
            3 | 3 | 6
    }
}
