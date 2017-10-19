package com.zxventures.zxapp.base

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
abstract class BaseFragment : Fragment() {

    abstract fun getName(): String
    abstract fun getLayout(): Int

    companion object {
        val PARCELABLE:String = "parcelable"
        val SERIALIZABLE:String = "serializable"
        val PARCELABLE_LIST:String = "parcelable_list"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(getLayout(), container, false)
    }

    fun <A:BaseActivity> getBaseActivity(): A {
        return activity as A
    }

    fun setData(value:Any, key: String? = null) {
        val bundle = arguments ?: Bundle()
        when(value) {
            is Serializable -> bundle.putSerializable(key?: SERIALIZABLE,value)
            is Parcelable -> bundle.putParcelable(key?: PARCELABLE,value)
            is ArrayList<*> -> bundle.putParcelableArrayList(key?: PARCELABLE_LIST,
                        value as ArrayList<Parcelable>)
        }
        arguments = bundle
    }

    protected fun <S: Serializable> getSerializable(key: String? = null): S {
        return arguments.getSerializable(key?: SERIALIZABLE) as S
    }

    protected fun <P: Parcelable> getParcelable(key: String? = null) : P {
        return arguments.getParcelable(key?:PARCELABLE)
    }

    protected fun <P: Parcelable> getParcelableList(key: String? = null) : ArrayList<P> {
        return arguments.getParcelableArrayList<P>(key?: PARCELABLE_LIST)
    }
}
