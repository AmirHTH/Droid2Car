package com.prototype.droid2car.droid2car

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.bluetooth.BluetoothAdapter
import android.content.Intent

import android.speech.RecognizerIntent
import android.util.Log
import android.view.View

import android.widget.TextView
import android.R.attr.fragment
import android.support.v4.app.Fragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val REQUEST_ENABLE_BT = 1
    val RESULT_SPEECH =1
    val TAG = "MAIN MAIN"
    var zonas = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Parada de emergencia", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter == null) {
                Snackbar.make(view, "NO SOPORTA BLUETOOTH", Snackbar.LENGTH_LONG)
                    .setAction("Action ", null).show()
            } else {
                if (!mBluetoothAdapter.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                } else {

                    BTJ.getInstance().sendData("0")
                }
            }

        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
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
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val fragmentManager = getSupportFragmentManager()
        var fragment = Fragment()
        var tag = ""
        when (item.itemId) {
            R.id.nav_ordenes -> {
                tag = fragmentOrdenes.tag
                fragment = fragmentManager.findFragmentByTag(tag) ?: fragmentOrdenes()
                fragment.arguments
            }
            R.id.nav_estancias -> {

                tag = fragmentPlano.tag
                 fragment = fragmentManager.findFragmentByTag(tag) ?: fragmentPlano()
                fragment.arguments



                //   setContentView(R.layout.plano)


                 Log.i("Content main main "," Main layout ");

            }
            R.id.nav_historico -> {

            }
            R.id.nav_configuracion -> {

            }
            R.id.nav_share -> {

            }

        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentArea, fragment, tag)
                .addToBackStack(tag)
                .commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    fun recVoz(view: View) {
      //  setContentView(R.layout.activity_second)
        val enableVoice = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        startActivityForResult(enableVoice, RESULT_SPEECH);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SPEECH -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    Log.i(TAG, "datos long 1 "+text.get(0))


                    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    if (mBluetoothAdapter == null) {
                        Log.i(TAG, "NO HAY BLUETOOTH  ")
                    } else {
                        if (!mBluetoothAdapter.isEnabled) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                        } else {

                            var destino="";
                            val frase = text.get(0).toLowerCase();

                            if (frase.contains("baño"))
                            {
                                destino += "1"
                                zonas += "baño, "
                            }

                            if (frase.contains("cocina"))
                            {
                                destino += "2"
                                zonas +="cocina, "
                            }

                            if (frase.contains("uno") || frase.contains("1") )
                            {
                                destino += "3"
                                zonas += "cuarto 1, "
                            }

                            if (frase.contains("dos") || frase.contains("2"))
                            {
                                destino += "4"
                                zonas += "cuarto 2, "
                            }

                            if (frase.contains("salón")|| frase.contains("salon"))
                            {
                                destino += "5"
                                zonas += "salón, "
                            }

                            if (frase.contains("pasillo"))
                            {
                                destino += "6"
                                zonas += "pasillo, "
                            }

                            if (frase.contains("todo"))
                            {
                                destino += "123456"
                                zonas += "todo, "
                            }

                            if (frase.contains("todas"))
                            {
                                destino += "123456"
                                zonas += "todo, "
                            }

                            if (frase.contains("dormitorio"))
                            {
                                destino += "34"
                                zonas += "dormitorios, "
                            }

                            if (frase.contains("cuartos"))
                            {
                                destino += "34"
                                zonas += "dormitorios, "
                            }

                            if (destino.equals("")) {
                                destino = "9";
                                Log.i(TAG, "NO HAY destino valido")
                            }
                            Log.i(TAG, "Destinos seleccionados: " +destino )


                            var textoZonas = zonas.subSequence(0, zonas.lastIndex -1)
                            val textView: TextView = findViewById(R.id.zonaLimpia) as TextView
                            textView.text = "Limpiando "+textoZonas
                            zonas = ""


                            BTJ.getInstance().sendData(destino)
                            Log.i(TAG, "fin voz" )
                        }
                    }

                }
            }
        }
    }



}
