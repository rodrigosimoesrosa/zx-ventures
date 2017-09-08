package com.zxventures.zxapp.extensions

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.home_content.*
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rodrigosimoesrosa
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun Activity.isNetworkConnected(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Activity.hideKeyboard(){
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.isNetworkConnected(): Boolean {
    return activity.isNetworkConnected()
}

fun Date.toISO8601UTC(): String {
    val timeZone = TimeZone.getTimeZone("UTC")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    simpleDateFormat.timeZone = timeZone
    return simpleDateFormat.format(this)
}

fun Date.toISO8601(): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(this)
}

@Throws(ParseException::class)
fun String.fromISO8601UTC(): Date? {
    val timeZone = TimeZone.getTimeZone("UTC")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    simpleDateFormat.timeZone = timeZone

    return simpleDateFormat.parse(this)
}

fun Double.toCurrency(l: Locale? = null, c: Currency? = null) : String{
    val locale = l?: Locale.getDefault()
    val format = NumberFormat.getCurrencyInstance(locale)
    val currency = when(c == null){
        true -> Currency.getInstance(locale)
        false -> c
    }

    format.currency = currency
    return format.format(this)
}

