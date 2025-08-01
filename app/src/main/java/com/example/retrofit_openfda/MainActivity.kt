package com.example.retrofit_openfda

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        setContent {
            Retrofit_openfdaTheme {
                DrugSearchScreen()
            }
        }
    }
}

@Composable
fun DrugSearchScreen() {

    var brandName by remember { mutableStateOf(" ") }
    var drugInfo by remember { mutableStateOf(" ") }


    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.fda.gov/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(OpenFdaApi::class.java)
    val dao = DrugDatabase.getDatabase(LocalContext.current).drugDao()
    val repository = DrugRepository(api, dao)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = brandName,
            onValueChange = { brandName = it },
            label = { Text("Brand Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val drug = repository.getDrugByBrandName(brandName)
                    drugInfo = "Brand: ${drug.brandName}\nGeneric: ${drug.genericName}"

                    CoroutineScope(Dispatchers.Main).launch {
                        drugInfo = "Brand: ${drug.brandName}\nGeneric: ${drug.genericName}"
                    }
                }
            }
        ) {
            Text("Search Drug\uD83D\uDC8A")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = drugInfo,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrugSearchScreen() {
    Retrofit_openfdaTheme {
        DrugSearchScreen()
    }
}
