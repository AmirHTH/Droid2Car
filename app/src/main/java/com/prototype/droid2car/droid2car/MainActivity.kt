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


import java.util.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files.size
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val REQUEST_ENABLE_BT = 1
    val RESULT_SPEECH =1
    val TAG = "MAIN MAIN"


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
                    val btj =  BTJ();
                    btj.sendData("0")

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
        when (item.itemId) {
            R.id.nav_ordenes -> {
                // Handle the camera action
            }
            R.id.nav_estancias -> {

            }
            R.id.nav_historico -> {

            }
            R.id.nav_configuracion -> {

            }
            R.id.nav_share -> {

            }

        }

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
                            val btj =  BTJ();
                            var destino="";
                            val frase = text.get(0).toLowerCase();

                            if (frase.contains("baño"))
                            {destino += "1"}

                            if (frase.contains("cocina"))
                            {destino += "2"}

                            if (frase.contains("uno") || frase.contains("1") )
                            {destino += "3"}

                            if (frase.contains("dos") || frase.contains("2"))
                            {destino += "4"}

                            if (frase.contains("salón")|| frase.contains("salon"))
                            {destino += "5"}

                            if (frase.contains("pasillo"))
                            {destino += "6"}

                            if (destino.equals("")) {
                                destino = "9";
                                Log.i(TAG, "NO HAY destino valido")
                            }
                            Log.i(TAG, "Destinos seleccionados: " +destino )
                            btj.sendData(destino)
                            Log.i(TAG, "fin voz" )
                        }
                    }

                }
            }
        }
    }
}
