package com.zxventures.zxapp.screen.home.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Places
import com.zxventures.zxapp.R
import java.util.concurrent.TimeUnit

/**
 * Created by rodrigosimoesrosa
 */
class AddressAutoCompleteAdapter(context: Context?,
                                 private val googleApiClient: GoogleApiClient,
                                 val listener: (msg: String) -> Unit) :
        ArrayAdapter<AutocompletePrediction>(
                context,
                R.layout.auto_complete_item,
                android.R.id.text1), Filterable {

    companion object {
        private val STYLE_BOLD = StyleSpan(Typeface.BOLD)
    }

    private var resultList: ArrayList<AutocompletePrediction>? = null

    override fun getCount(): Int {
        return resultList!!.size
    }

    override fun getItem(position: Int): AutocompletePrediction? {
        return resultList!![position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = super.getView(position, convertView, parent)

        val item = getItem(position)

        val textView1 = row.findViewById<View>(android.R.id.text1) as TextView
        val textView2 = row.findViewById<View>(android.R.id.text2) as TextView
        textView1.text = item!!.getPrimaryText(STYLE_BOLD)
        textView2.text = item.getSecondaryText(STYLE_BOLD)

        return row
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                val results = Filter.FilterResults()

                var filterData: ArrayList<AutocompletePrediction>? = ArrayList()

                if (constraint != null) {
                    filterData = getAutocomplete(constraint)
                }

                results.values = filterData
                if (filterData != null) {
                    results.count = filterData.size
                } else {
                    results.count = 0
                }

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
                if (results != null && results.count > 0) {
                    resultList = results.values as ArrayList<AutocompletePrediction>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun convertResultToString(resultValue: Any): CharSequence {
                return if (resultValue is AutocompletePrediction) {
                    resultValue.getFullText(null)
                } else {
                    super.convertResultToString(resultValue)
                }
            }
        }
    }

    private fun getAutocomplete(constraint: CharSequence?): ArrayList<AutocompletePrediction>? {
        if (googleApiClient.isConnected) {

            val results = Places.GeoDataApi
                    .getAutocompletePredictions(googleApiClient, constraint.toString(), null, null)

            val autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS)

            val status = autocompletePredictions.status

            if (!status.isSuccess) {
                listener(status.toString())
                autocompletePredictions.release()
                return null
            }

            return DataBufferUtils.freezeAndClose(autocompletePredictions)
        }
        return null
    }
}