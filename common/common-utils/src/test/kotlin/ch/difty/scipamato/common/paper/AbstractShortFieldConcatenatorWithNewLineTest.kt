package ch.difty.scipamato.common.paper

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class AbstractShortFieldConcatenatorWithNewLineTest {

    private val concatenator = object : AbstractShortFieldConcatenator(true) {
    }

    @Test
    fun method_withMethodPresent_returnsMethod() {
        concatenator.methodsFrom("m", "msd", "mo", "pp", "ep", "ea", "ms", "mc") shouldBeEqualTo "m"
    }

    @Test
    fun method_withMethodNull_returnsConcatenatedShortFields() {
        concatenator.methodsFrom(null, "msd", "mo", "pp", "ep", "ea", "ms", "mc") shouldBeEqualTo
            """Studiendesign: msd
                |Gesundheitliche Zielgrössen: mo
                |Ort/Land: pp
                |Schadstoff: ep
                |Belastungsabschätzung: ea
                |Statistische Methode: ms
                |Störfaktoren: mc""".trimMargin()
    }

    @Test
    fun method_withMethodNullAndSomeShortFieldsNull_returnsConcatenatedShortFields() {
        concatenator.methodsFrom(null, "msd", null, "pp", "ep", "ea", "ms", "mc") shouldBeEqualTo
            """Studiendesign: msd
                |Ort/Land: pp
                |Schadstoff: ep
                |Belastungsabschätzung: ea
                |Statistische Methode: ms
                |Störfaktoren: mc""".trimMargin()
    }

    @Test
    fun method_withMethodNull_withExplicitLabels_returnsConcatenatedShortFields() {
        concatenator.methodsFrom(
            null, newT("msd"), newT("mo"), newT("pp"), newT("ep"), newT("ea"), newT("ms"), newT("mc")
        ) shouldBeEqualTo "msdl: msd\nmol: mo\nppl: pp\nepl: ep\neal: ea\nmsl: ms\nmcl: mc"
    }

    @Test
    fun method_withMethodNullAndTupleLabelNull_withExplicitLabels_returnsConcatenatedShortFields() {
        concatenator.methodsFrom(
            null, newT("msd"), newT("mo"), AbstractShortFieldConcatenator.Tuple(null, "pp"),
            newT("ep"), newT("ea"), newT("ms"), newT("mc")
        ) shouldBeEqualTo "msdl: msd\nmol: mo\npp\nepl: ep\neal: ea\nmsl: ms\nmcl: mc"
    }

    @Test
    fun method_withMethodNullAndEntireTupleNull_withExplicitLabels_returnsConcatenatedShortFields() {
        concatenator.methodsFrom(
            null, newT("msd"), newT("mo"), null, newT("ep"), newT("ea"), newT("ms"), newT("mc")
        ) shouldBeEqualTo "msdl: msd\nmol: mo\nepl: ep\neal: ea\nmsl: ms\nmcl: mc"
    }

    private fun newT(f: String): AbstractShortFieldConcatenator.Tuple = AbstractShortFieldConcatenator.Tuple(f + "l", f)

    @Test
    fun population_withMethodPresent_returnsMethod() {
        concatenator.populationFrom("p", "ppl", "ppa", "pd") shouldBeEqualTo "p"
    }

    @Test
    fun population_withPopulationNull_returnsConcatenatedShortFields() {
        concatenator.populationFrom(null, "ppl", "ppa", "pd") shouldBeEqualTo
            "Ort/Land: ppl\nStudienteilnehmer: ppa\nStudiendauer: pd"
    }

    @Test
    fun population_withPopulationNull_withExplicitLabels_returnsConcatenatedShortFields() {
        concatenator.populationFrom(null, newT("ppl"), newT("ppa"), newT("pd")) shouldBeEqualTo
            "ppll: ppl\nppal: ppa\npdl: pd"
    }

    @Test
    fun result_withResultPresent_returnsMethod() {
        concatenator.resultFrom("r", "rmo", "rer", "ree", "cc") shouldBeEqualTo "r"
    }

    @Test
    fun result_withResultNull_returnsConcatenatedShortFields() {
        concatenator.resultFrom(null, "rmo", "rer", "ree", "cc") shouldBeEqualTo
            "Gemessene Zielgrösse: rmo\nGemessene Belastung (Spanne): rer\nEffektschätzer: ree\nSchlussfolgerung: cc"
    }

    @Test
    fun result_withResultNull_withExplicitLabels_returnsConcatenatedShortFields() {
        concatenator.resultFrom(null, newT("rmo"), newT("rer"), newT("ree"), newT("cc")) shouldBeEqualTo
            "rmol: rmo\nrerl: rer\nreel: ree\nccl: cc"
    }
}
