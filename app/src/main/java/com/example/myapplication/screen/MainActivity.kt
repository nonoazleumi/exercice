package com.example.myapplication.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.data.ShowCountryData
import com.example.myapplication.screen.base.BaseFragment
import com.example.myapplication.screen.base.NavigationContent

class MainActivity : AppCompatActivity() {

    var activityViewModel:MainViewModel?=null
    lateinit var loadingView:ProgressBar
    lateinit var fragmentContent:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.addOnBackStackChangedListener {
            supportFragmentManager.findFragmentById(R.id.fragment_content)?.let {
                setTitleToShow((supportFragmentManager.findFragmentById(R.id.fragment_content) as BaseFragment).getNavigationContent())
            }?:finish()
        }
        loadingView = findViewById<ProgressBar>(R.id.loadingView)
        fragmentContent = findViewById<FrameLayout>(R.id.fragment_content)


        activityViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activityViewModel?.startFlow(getResourcesFlow())
        activityViewModel?.stepInProcess?.observe(this, Observer {flowStep->
            when(flowStep){
                is MainViewModel.FlowStep.StepShowCountry->{
                    startNewFragment(ShowCountryFragment.newInstance(flowStep.showCountryData, flowStep.titleTxt))
                }
                is MainViewModel.FlowStep.StepShowCountryDetails->{
                    replaceFragment(ShowCountryDetailsFragment.newInstance(flowStep.countryDetails, flowStep?.titleTxt))
                }
            }

        })

        activityViewModel?.screenState?.observe(this, Observer { screenState->
            when(screenState){
                is MainViewModel.ScreenState.Waiting->{
                    loadingView.visibility = View.VISIBLE
                    fragmentContent.visibility = View.GONE
                }
                is MainViewModel.ScreenState.IDLE->{
                    loadingView.visibility = View.GONE
                    fragmentContent.visibility = View.VISIBLE
                }
            }
        })

    }


    fun getResourcesFlow():HashMap<String, String>?{
        val resourcesstring = HashMap<String, String>()
        resourcesstring.put("titleShowCountry", resources.getString(R.string.show_country_title))
        resourcesstring.put("titleDetailsCountry", resources.getString(R.string.show_country_details_title))
        resourcesstring.put("countrySubtitle", resources.getString(R.string.show_country_subttitle))
        resourcesstring.put("countryExplain", resources.getString(R.string.show_country_explain))
        resourcesstring.put("orderByEnTxtAZ", resources.getString(R.string.order_by_english_az))
        resourcesstring.put("orderByEnTxtZA", resources.getString(R.string.order_by_english_za))
        resourcesstring.put("orderByAreaTxt1", resources.getString(R.string.order_by_area_1))
        resourcesstring.put("orderByAreaTxt9", resources.getString(R.string.order_by_area_9))
        resourcesstring.put("noBorders", resources.getString(R.string.there_is_no_border))
        return resourcesstring
    }

    //Init Fragment
    fun startNewFragment(fragment: BaseFragment) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fragment_content, fragment)
        beginTransaction.addToBackStack(fragment.tag).commit()
        setTitleToShow(fragment.getNavigationContent())
    }

    fun replaceFragment(fragment: BaseFragment, removeAllPrevious: Boolean=false) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.fragment_content, fragment)
            .addToBackStack(fragment.tag)
        beginTransaction.commit()
    }

    private fun setTitleToShow(navigationContent: NavigationContent) {
        findViewById<TextView>(R.id.title_text).text = navigationContent.titleText
    }

}
