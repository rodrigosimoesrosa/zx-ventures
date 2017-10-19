package com.zxventures.zxapp.base

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
abstract class BaseActivity : AppCompatActivity(){

    class ActivityData(val key:String,val data:Any)

    companion object {
        val PARCELABLE:String = "parcelable"
        val SERIALIZABLE:String = "serializable"
        val PARCELABLE_LIST:String = "parcelable_list"
    }

    abstract fun getLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }

    fun <Fragment:BaseFragment> addFragment(fragment:Fragment, container:Int,
        backStack:Boolean = false, animations:Array<Int>? = null){

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        if(fragmentManager.backStackEntryCount == 0){
            if(animations != null && animations.size == 4){
                ft.setCustomAnimations(animations[0],animations[1],animations[2],animations[3])
            }
            ft.add(container,fragment)
            if(backStack) ft.addToBackStack(fragment.getName())
            ft.commit()
        }else{
            replaceFragment(fragment,container,backStack,animations)
        }
    }

    fun <Fragment:BaseFragment> replaceFragment(fragment: Fragment, container: Int,
        backStack: Boolean = false, animations: Array<Int>? = null){

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        if(animations != null && animations.size == 4){
            ft.setCustomAnimations(animations[0],animations[1],animations[2],animations[3])
        }
        ft.replace(container,fragment)
        if(backStack) ft.addToBackStack(fragment.getName())
        ft.commit()
    }

    fun <Fragment:BaseFragment> removeFragment(fragment: Fragment,
                                               animations: Array<Int>? = null){
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        if(animations != null && animations.size == 4){
            ft.setCustomAnimations(animations[0],animations[1],animations[2],animations[3])
        }
        ft.remove(fragment)
    }

    fun <Activity:AppCompatActivity> clearBackStack(activity:Activity? = null){
        val fm: FragmentManager
        if (activity != null) fm = activity.supportFragmentManager
        else fm = supportFragmentManager
        for(i in 0 .. fm.backStackEntryCount) fm.popBackStack()
    }

    fun <Activity: AppCompatActivity> startActivity(classActivity: Class<Activity>,
                                                    data:Any? = null){
        val intent: Intent = when(data == null) {
            true -> Intent(this, classActivity)
            false -> getIntentByData(classActivity, data)
        }
        startActivity(intent)
    }

    private fun <Activity: AppCompatActivity> getIntentByData(classActivity: Class<Activity>,
                                                              data: Any?): Intent {
        val intent = Intent(this, classActivity)
        when(data){
            is ActivityData -> fillIntentByData(intent, data.data, data.key)
            else -> fillIntentByData(intent, data)
        }
        return intent
    }

    private fun fillIntentByData(intent: Intent, data: Any?, key: String? = null){
        when(data){
            is ArrayList<*> -> intent.putParcelableArrayListExtra(key?: PARCELABLE_LIST,
                    data as ArrayList<Parcelable>)
            is Parcelable -> intent.putExtra(key?: PARCELABLE, data)
            is Serializable -> intent.putExtra(key?: SERIALIZABLE, data)
            is Bundle -> intent.putExtras(data)
        }
    }

    fun <Actvity: AppCompatActivity> startActivityForResult(classActivity:Class<Actvity>,
                                                            requestCode:Int, data:Any? = null) {
        val intent: Intent = when(data == null) {
            true -> Intent(this, classActivity)
            false -> getIntentByData(classActivity, data)
        }
        startActivityForResult(intent, requestCode)
    }

    fun <S: Serializable> getSerializable(key: String? = null) : S {
        return intent.getSerializableExtra(key ?: SERIALIZABLE) as S
    }

    fun <P: Parcelable> getParcelable(key: String? = null): P {
        return intent.getParcelableExtra(key ?: PARCELABLE)
    }

    fun <P: Parcelable> getParcelableList(key: String?): java.util.ArrayList<P>? {
        return intent.getParcelableArrayListExtra<P>(key ?: PARCELABLE_LIST)
    }

    fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showSnack(view: View?, msg: String) {
        if(view != null) Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }
}