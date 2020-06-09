package com.example.agrogestao.view.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.Farm
import com.google.android.material.navigation.NavigationView
import io.realm.Realm

class NavigationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        val bundleRecuperado = intent.extras
        if (bundleRecuperado != null) {
            val id = bundleRecuperado.getString("fazenda")
            val bundleCriado = Bundle()
            bundleCriado.putString("id", id)
            navController.setGraph(navController.graph, bundleCriado)
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_fazenda,
                R.id.fluxo_caixa,
                R.id.balanco_patrimonial_frag
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val bundleRecuperado = intent.extras
        if (bundleRecuperado != null) {
            val id = bundleRecuperado.getString("fazenda")
            val bundleCriado = Bundle()
            bundleCriado.putString("id", id)
            navController.setGraph(navController.graph, bundleCriado)
        }
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun loadData(): Farm? {
        val realm = Realm.getDefaultInstance()
        val bundle = intent.extras
        var farm: Farm? = null
        if (bundle != null) {
            val id = bundle.getString("fazenda")
        }
        return null
    }


}

