package com.example.agrogestao.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.firebaseclasses.AtividadeFirebase
import com.example.agrogestao.models.firebaseclasses.BalancoFirebase
import com.example.agrogestao.models.firebaseclasses.FarmFirebase
import com.example.agrogestao.models.firebaseclasses.FluxoCaixaFirebase
import com.example.agrogestao.models.realmclasses.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.kotlin.where


class NavigationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var id: String
    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        val bundleRecuperado = intent.extras
        if (bundleRecuperado != null) {
            id = bundleRecuperado.getString("fazenda")!!
            val bundleCriado = Bundle()
            bundleCriado.putString("id", id)
            navController.setGraph(navController.graph, bundleCriado)
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

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
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            val auth = Firebase.auth
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else if (item.itemId == R.id.action_select_farm) {
            val intent = Intent(this, AtividadesActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)

    }

    override fun onStop() {
        if (Usuario.isOnline(applicationContext)) {
            salvarFazenda()
            salvarBalanco()
            salvarFluxoCaixa()
            salvarAtividades()
        }

        super.onStop()
    }

    private fun salvarFazenda() {
        val farm: Farm = realm.where<Farm>().contains("id", id).findFirst()!!
        if (farm.atualizado) {
            Log.e("Fazenda", "salvou ")
            realm.beginTransaction()
            farm.attModificacao()
            val farmAux = FarmFirebase(farm)
            val db =
                Firebase.database.reference.child("fazendas").child(farm.programa).child(farm.id)
            db.setValue(farmAux)
            farm.atualizado = false
            realm.commitTransaction()
        }

    }

    private fun salvarAtividades() {
        val results = realm.where<AtividadesEconomicas>().contains("fazendaID", id).findAll()
        results.forEach {
            realm.beginTransaction()
            if (it.atualizado) {
                Log.e("Atividade", "salvou ")
                it.attModificacao()
                val atividade = AtividadeFirebase(it)
                val db = Firebase.database.reference.child("atividadesEconomicas").child(id)
                    .child(atividade.nome)
                db.setValue(atividade)
                it.atualizado = false
            }
            realm.commitTransaction()
        }
    }

    private fun salvarFluxoCaixa() {


        val fluxoCaixa: FluxoCaixa = realm.where<FluxoCaixa>().contains("farmID", id).findFirst()!!
        if (fluxoCaixa.atualizado) {
            Log.e("Fluxo", "salvou ")
            realm.beginTransaction()
            fluxoCaixa.attModificacao()
            val fluxoaux = FluxoCaixaFirebase(fluxoCaixa)
            val dbfluxo = Firebase.database.reference.child("fluxoCaixa").child(fluxoaux.farmID)
            dbfluxo.setValue(fluxoaux)
            fluxoCaixa.atualizado = false
            realm.commitTransaction()
        }
    }

    private fun salvarBalanco() {

        val balancoPatrimonial =
            realm.where<BalancoPatrimonial>().contains("farmID", id).findFirst()!!
        if (balancoPatrimonial.atualizado) {
            Log.e("Balanco", "salvou ")
            realm.beginTransaction()
            balancoPatrimonial.attModificacao()
            val balancoAux = BalancoFirebase(balancoPatrimonial)
            val dbBalanco =
                Firebase.database.reference.child("balancoPatrimonial").child(balancoAux.farmID)
            dbBalanco.setValue(balancoAux)
            balancoPatrimonial.atualizado = false
            realm.commitTransaction()
        }
    }


}

