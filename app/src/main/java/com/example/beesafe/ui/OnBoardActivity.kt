package com.example.beesafe.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.beesafe.R
import com.example.beesafe.ui.auth.LoginActivity
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

@Suppress("DEPRECATION")
class OnBoardActivity : AppIntro() {
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroFragment.newInstance(
                title = "Aman",
                description = resources.getString(R.string.onBoard),
                imageDrawable = R.drawable.map,
                titleColor = resources.getColor(R.color.dark_orange),
                descriptionColor = resources.getColor(R.color.dark_orange),
                titleTypefaceFontRes = R.font.lato_bold,
                descriptionTypefaceFontRes = R.font.lato_regular
                )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Berani",
                description = resources.getString(R.string.onBoard2),
                imageDrawable = R.drawable.report,
                titleColor = resources.getColor(R.color.dark_orange),
                descriptionColor = resources.getColor(R.color.dark_orange),
                titleTypefaceFontRes = R.font.lato_bold,
                descriptionTypefaceFontRes = R.font.lato_regular
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Sehat",
                description = resources.getString(R.string.onBoard3),
                imageDrawable = R.drawable.call,
                titleColor = resources.getColor(R.color.dark_orange),
                descriptionColor = resources.getColor(R.color.dark_orange),
                titleTypefaceFontRes = R.font.lato_bold,
                descriptionTypefaceFontRes = R.font.lato_regular
            )
        )
        setTransformer(
            AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))
        // Toggle Indicator Visibility
        isIndicatorEnabled = true

        // Change Indicator Color
        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.dark_orange),
            unselectedIndicatorColor = getColor(R.color.young_orange)
        )

        setNextArrowColor(getColor(R.color.dark_orange))
        setColorDoneText(getColor(R.color.dark_orange))
        isWizardMode = true
        setImmersiveMode()

    }
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val firstTime : SharedPreferences = getSharedPreferences("ONBOARD", MODE_PRIVATE)
        firstTime.edit().putBoolean("FirstTime", false).commit()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val firstTime : SharedPreferences = getSharedPreferences("ONBOARD", MODE_PRIVATE)
        firstTime.edit().putBoolean("FirstTime", false).commit()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}