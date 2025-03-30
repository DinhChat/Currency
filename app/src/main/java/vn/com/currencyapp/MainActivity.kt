package vn.com.currencyapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerFrom = findViewById<Spinner>(R.id.chooseCur1)
        val spinnerTo = findViewById<Spinner>(R.id.chooseCur2)
        val firstCurrent = findViewById<EditText>(R.id.firstCur);
        val secondCurrent = findViewById<EditText>(R.id.secondCur);

        val currencies : Array<String> = arrayOf("United States - Dollar", "Europe - Euro", "China - Yuan", "Japan - Yen", "VietNam - Dong");
        val exchangeRates = mapOf(
            "United States - Dollar" to 1.0,
            "Europe - Euro" to 0.85,
            "China - Yuan" to 6.45,
            "Japan - Yen" to 110.0,
            "VietNam - Dong" to 25000.050
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        val convertCurrency =
            convertCurrency@{ fromValue: EditText, toValue: EditText, fromCurrency: String, toCurrency: String ->
                if (isUpdating) return@convertCurrency

                val amount = fromValue.text.toString().toDoubleOrNull()
                if (amount != null && exchangeRates.containsKey(fromCurrency) && exchangeRates.containsKey(toCurrency)) {
                    isUpdating = true  // Đánh dấu đang cập nhật
                    val result = amount / exchangeRates[fromCurrency]!! * exchangeRates[toCurrency]!!
                    toValue.setText("%.2f".format(result))
                    isUpdating = false
                }
            }

        val textWatcher1 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency(firstCurrent, secondCurrent, spinnerFrom.selectedItem.toString(), spinnerTo.selectedItem.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        val textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency(secondCurrent, firstCurrent, spinnerTo.selectedItem.toString(), spinnerFrom.selectedItem.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        firstCurrent.addTextChangedListener(textWatcher1)
        secondCurrent.addTextChangedListener(textWatcher2)
    }
}