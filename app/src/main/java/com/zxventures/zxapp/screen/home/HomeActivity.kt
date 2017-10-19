package com.zxventures.zxapp.screen.home

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.zxventures.zxapp.R
import com.zxventures.zxapp.base.mvp.BaseMVPActivity
import com.zxventures.zxapp.extensions.afterTextChanged
import com.zxventures.zxapp.extensions.hideKeyboard
import com.zxventures.zxapp.extensions.isNetworkConnected
import com.zxventures.zxapp.model.PointOfContactData
import com.zxventures.zxapp.presenter.home.contract.HomeContract
import com.zxventures.zxapp.presenter.home.impl.HomePresenterImpl
import com.zxventures.zxapp.screen.home.adapter.AddressAutoCompleteAdapter
import com.zxventures.zxapp.screen.home.adapter.PointOfContactAdapter
import com.zxventures.zxapp.screen.product.ProductActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_content.*
import java.util.*

/**
 * Created by rodrigosimoesrosa
 */
class HomeActivity : BaseMVPActivity<HomeContract.HomeView, HomeContract.HomePresenter>(),
        GoogleApiClient.OnConnectionFailedListener, HomeContract.HomeView {

    override var presenter: HomeContract.HomePresenter = HomePresenterImpl()

    private val RECYCLER_STATE = "RECYCLER_STATE"

    override fun getLayout(): Int = R.layout.activity_home

    private val pointsOfContact: MutableList<PointOfContactData.PointOfContact> = mutableListOf()

    private lateinit var adapter: PointOfContactAdapter
    private lateinit var addressAutoCompleteAdapter: AddressAutoCompleteAdapter

    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.home_title)

        build()
        buildPointsOfContact()
        buildInputAddress()
    }

    private var recyclerState: Parcelable? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            pointsOfContact.clear()
            val list = (savedInstanceState.getParcelableArrayList<PointOfContactData.PointOfContact>(
                    PointOfContactData.PointOfContact.POINT_OF_CONTACT))
            pointsOfContact.addAll(list)
            adapter.lists = pointsOfContact
            recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE)
        }
    }

    override fun onResume() {
        super.onResume()
        if (recyclerState != null) {
            pointOfContactRecycler.layoutManager.onRestoreInstanceState(recyclerState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val recyclerState = pointOfContactRecycler.layoutManager.onSaveInstanceState()
        outState?.putParcelable(RECYCLER_STATE, recyclerState)

        outState?.putParcelableArrayList(PointOfContactData.PointOfContact.POINT_OF_CONTACT,
                pointsOfContact as ArrayList<PointOfContactData.PointOfContact>)

        super.onSaveInstanceState(outState)
    }

    private fun build() {
        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .build()

        btnClose.setOnClickListener {
            presenter.cancel()
            progressBarFromAutoComplete.visibility = View.GONE
            progressBar.visibility = View.GONE

            clearPointsOfContact()
            inputAddress.text.clear()
            inputAddress.requestFocus()
        }
    }

    private fun clearPointsOfContact(){
        pointsOfContact.clear()
        adapter.notifyDataSetChanged()
    }

    private fun buildPointsOfContact() {
        pointOfContactRecycler.layoutManager = LinearLayoutManager(this)
        pointOfContactRecycler.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        adapter = PointOfContactAdapter(this, pointsOfContact, {
            startActivity(ProductActivity::class.java,
                    ActivityData(PointOfContactData.PointOfContact.POINT_OF_CONTACT, it))
        })

        pointOfContactRecycler.adapter = adapter
    }

    private fun buildInputAddress(){

        inputAddress.afterTextChanged {
            presenter.cancel()
            clearPointsOfContact()
            btnClose.visibility = if(inputAddress.text.isEmpty()) View.GONE else View.VISIBLE
            progressBarFromAutoComplete.visibility = View.GONE
            progressBar.visibility = View.GONE
            txtAlert.visibility = View.GONE
        }

        addressAutoCompleteAdapter = AddressAutoCompleteAdapter(this, googleApiClient, { _ ->
            showGoogleApiError()
        })

        inputAddress.setOnItemClickListener { _, _, i, _ ->
            val item = addressAutoCompleteAdapter.getItem(i)
            val placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, item?.placeId)
            placeResult.setResultCallback {
                if (!it.status.isSuccess) {
                    showGoogleApiError()
                }else{
                    onGetAddress(it[0])
                }
                it.release()
            }
        }
        inputAddress.setAdapter(addressAutoCompleteAdapter)
    }

    override fun onDestroy() {
        presenter.cancel()
        super.onDestroy()
    }

    private fun onGetAddress(place: Place) {
        hideKeyboard()
        inputAddress.setSelection(0)

        progressBar.visibility = View.VISIBLE
        progressBarFromAutoComplete.visibility = View.VISIBLE
        presenter.loadPocsByAddress(place.latLng?.latitude.toString(),
                place.latLng?.longitude.toString())
    }

    private fun showGoogleApiError(){
        if(isNetworkConnected()){
            showSnack(view, getString(R.string.address_contacting_api_failed))
        }else{
            showSnack(view, getString(R.string.no_internet_connection))
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if(isNetworkConnected()){
            showSnack(container, getString(R.string.address_connect_api_failed))
        }else{
            showSnack(container, getString(R.string.no_internet_connection))
        }
    }

    override fun showPointsOfContact(points: MutableList<PointOfContactData.PointOfContact>) {
        progressBarFromAutoComplete.visibility = View.GONE
        progressBar.visibility = View.GONE

        txtAlert.visibility = View.GONE

        pointsOfContact.addAll(points)
        adapter.notifyDataSetChanged()
        pointOfContactRecycler.requestFocus()
    }

    override fun showError(e: Exception) {
        if(isNetworkConnected()){
            val msg = when(e){
                is ApolloNetworkException -> getString(R.string.problem_connection)
                is ApolloHttpException -> getString(R.string.problem_connection)
                else -> getString(R.string.generic_error)
            }
            showSnack(container, msg)
            showEmptyState(getString(R.string.home_empty_data))
        }else{
            showEmptyState(getString(R.string.no_internet_connection))
            showSnack(container, getString(R.string.no_internet_connection))
        }
    }

    override fun showEmptyState(msg: String) {
        progressBarFromAutoComplete.visibility = View.GONE
        progressBar.visibility = View.GONE

        txtAlert.visibility = View.VISIBLE
        txtAlert.text = msg
    }
}