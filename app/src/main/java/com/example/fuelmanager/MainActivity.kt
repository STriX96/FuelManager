package com.example.fuelmanager

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.main_navigation_drawer_open, R.string.main_navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        btn_fillup.setOnClickListener{
            startActivity(Intent(this, FillUpsActivity::class.java))
        }
        btn_stats.setOnClickListener{
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(getString(R.string.sign_out))
            builder.setMessage(getString(R.string.sign_out_message))
            builder.setPositiveButton(getString(R.string.yes)){dialog, which ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginRegisterActivity::class.java))
                dialog.dismiss()
                finish()
            }

            builder.setNegativeButton(getString(R.string.cancel)){dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginRegisterActivity::class.java))
                finish()
            }
            R.id.nav_fillups -> {
                val fillUpIntent = Intent(this, FillUpsActivity::class.java)
                startActivity(fillUpIntent)
            }
            R.id.nav_stats -> {
                val statIntent = Intent(this, StatisticsActivity::class.java)
                startActivity(statIntent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
