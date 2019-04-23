package com.dichotomyllc.tipcalc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.math.RoundingMode
import android.text.Spanned
import android.text.InputFilter
import java.util.regex.Pattern
import android.text.Selection
import android.view.View
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amountText: EditText = findViewById(R.id.editTextAmount) as EditText
        val tipButton: Button = findViewById(R.id.buttonTip) as Button
        amountText.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(10, 2));

        amountText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            // set $ at the beginning of the string.
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().startsWith("$")) {
                    amountText.setText("$")
                    Selection.setSelection(amountText.getText(), amountText.getText().length)

                }
                tipButton.isEnabled = s.toString().length > 2
            }
        })

        tipButton.setOnClickListener(fun(it: View) {
            val amount = amountText.text.removePrefix("$").toString().toFloat()
            Toast.makeText(this, "$" + (amount * 0.15).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString(), Toast.LENGTH_LONG).show()
        })
    }
}

// Borrowed from Stackoverflow to round Decimal to two spaces.
// It uses a regex pattern to limit the decimals,
// and uses a filter to apply the restriction to the input in the field itself.
class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {

    internal var mPattern: Pattern

    init {
        mPattern =
            Pattern.compile("[$][0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val matcher = mPattern.matcher(dest)
        return if (!matcher.matches()) "" else null
    }

}
