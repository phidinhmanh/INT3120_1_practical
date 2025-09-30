import com.example.tiptime.calculateTip
import org.junit.Test

class TipCalculatorTest {
    @Test
    fun calculate_20_percent_tip_no_roundup() {
        val amount = 10.00
        val tipPercent = 20.0
        val expectedTip = "$2.00"
        val actualTip = calculateTip(amount, tipPercent, false)
        assert(expectedTip == actualTip)
    }
    @Test
    fun calculate_15_percent_tip_roundup() {
        val amount = 10.00
        val tipPercent = 15.0
        val expectedTip = "$2.00"
        val actualTip = calculateTip(amount, tipPercent, true)
        assert(expectedTip == actualTip)
    }

    @Test
    fun calculate_0_percent_tip_no_roundup() {
        val amount = 10.00
        val tipPercent = 0.0
        val expectedTip = "$0.00"
        val actualTip = calculateTip(amount, tipPercent, false)
        assert(expectedTip == actualTip)
    }

    @Test
    fun calculate_200_percent_tip_roundup() {
       val amount = 10.00
        val tipPercent = 200.0
        val expectedTip = "$20.00"
        val actualTip = calculateTip(amount, tipPercent, true)
        assert(expectedTip == actualTip)
    }
}