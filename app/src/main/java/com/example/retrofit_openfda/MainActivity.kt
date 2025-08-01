package com.example.retrofit_openfda

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.retrofit_openfda.retrofit.OpenFda
import com.example.retrofit_openfda.retrofit.OpenFdaApi
import com.example.retrofit_openfda.retrofit.repository.DrugRepository
import com.example.retrofit_openfda.room.DrugDatabase
import com.example.retrofit_openfda.ui.theme.Retrofit_openfdaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)
        val b = findViewById<Button>(R.id.b)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.fda.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OpenFdaApi::class.java)
        val dao = DrugDatabase.getDatabase(this).drugDao()
        val repository = DrugRepository(api, dao)

        b.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val drug = repository.getDrugByBrandName("Nyquil")
                runOnUiThread {
                    tv.text = "Brand: ${drug.brandName}\nGeneric: ${drug.genericName}"

                }
            }
        }
    }
}
