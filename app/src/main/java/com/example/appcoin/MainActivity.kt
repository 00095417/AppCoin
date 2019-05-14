package com.example.appcoin

import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.example.appcoin.adapter.CoinAdapter
import com.example.appcoin.data.DataBase
import com.example.appcoin.data.DatabaseCoin
import com.example.appcoin.models.Coin
import com.example.appcoin.network.CoinSerializer
import com.example.appcoin.network.NetworkUtils
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var dbHelper = DataBase(this)

    lateinit var viewAdapter: CoinAdapter
    lateinit var viewManager: GridLayoutManager

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.apply{}
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    //--- Manejo de las view y acceso a la api ---------------------------------
        viewManager = GridLayoutManager(this,2)
        viewAdapter = CoinAdapter(listOf<Coin>()) {}

        rv_moneda.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        if (readCoin().isEmpty()){
            FetchCoin().execute()
        }else{
            viewAdapter.setData(readCoin())
        }

    //-----------------------------------------------------------------------
    }
    //--- Accediendo a la api -----------------------------------------------
    private inner class FetchCoin : AsyncTask<Unit, Unit, List<Coin>>(){

        override fun doInBackground(vararg params: Unit?): List<Coin> {

            val url = NetworkUtils.buildtUrl()
            val resultString = NetworkUtils.getHTTPResult(url)

            val resultJSON = JSONObject(resultString)

            val infoApi = if (resultJSON.getBoolean("success")) {
                CoinSerializer.parseCoins(
                    resultJSON.getJSONArray("docs").toString()
                )
            } else {
                listOf<Coin>()
            }

            return writeCoin(infoApi)
        }

        override fun onPostExecute(result: List<Coin>) {
            if (result.isNotEmpty()) {
                viewAdapter.setData(result)
            } else {
                Snackbar.make(rv_moneda,
                    "No se pudo obtener datos",
                    Snackbar.LENGTH_SHORT).show()
            }
        }

    }
    //--------------------------------------------------------------------
    fun writeCoin(listCoin:List<Coin>):List<Coin>{
        val db = dbHelper.writableDatabase
        val values = ContentValues()

        for (i in listCoin){
            values.apply {
                put(DatabaseCoin.CoinEntry.COLUMN_COINNAME,i.name)
                put(DatabaseCoin.CoinEntry.COLUMN_COUNTRY,i.country)
                put(DatabaseCoin.CoinEntry.COLUMN_YEAR,i.year)
            }

            db?.insert(DatabaseCoin.CoinEntry.TABLE_NAME, null, values)
        }
        return readCoin()
    }

    fun readCoin(): List<Coin>{

        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            DatabaseCoin.CoinEntry.COLUMN_COINNAME,
            DatabaseCoin.CoinEntry.COLUMN_COUNTRY,
            DatabaseCoin.CoinEntry.COLUMN_YEAR
        )

        val cursor = db.query(
            DatabaseCoin.CoinEntry.TABLE_NAME, // nombre de la tabla
            projection, // columnas que se devolverán
            null, // Columns where clausule
            null, // values Where clausule
            null, // Do not group rows
            null, // do not filter by row
            null // sort order
        )

        val lista = mutableListOf<Coin>()

        with(cursor) {
            while (moveToNext()) {
                val coin = Coin(
                    getString(getColumnIndexOrThrow(DatabaseCoin.CoinEntry.COLUMN_COINNAME)),
                    getString(getColumnIndexOrThrow(DatabaseCoin.CoinEntry.COLUMN_COUNTRY)),
                    getInt(getColumnIndexOrThrow(DatabaseCoin.CoinEntry.COLUMN_YEAR))
                )
                lista.add(coin)
            }
        }
        return lista
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
