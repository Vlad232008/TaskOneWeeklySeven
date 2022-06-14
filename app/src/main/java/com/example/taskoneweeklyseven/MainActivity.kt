package com.example.taskoneweeklyseven

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskoneweeklyseven.HeroInfoActivity.Companion.KEY_ID
import com.example.taskoneweeklyseven.databinding.ActivityMainBinding
import com.example.taskoneweeklyseven.json.HeroInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.*
import java.io.*

open class MainActivity : AppCompatActivity(), HeroAdapter.Listener {
    private var json: String = ""
    lateinit var binding: ActivityMainBinding
    private val URL_HEROINFO = "https://api.opendota.com/api/heroStats"
    private val okHttpClient = OkHttpClient()
    private val file: String = "dota"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readJson()
        initRcV()
    }

    private fun getHeroInfo() {
        val request = Request.Builder()
            .url(URL_HEROINFO)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                json = response.body.string()
                val moshi = Moshi.Builder().build()
                val listType = Types.newParameterizedType(List::class.java, HeroInfo::class.java)
                val adapter: JsonAdapter<List<HeroInfo>> = moshi.adapter(listType)
                heroInfo = adapter.fromJson(json)!!
            }
            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }

    private fun saveJson(){
        try {
            // отрываем поток для записи
            val bw = BufferedWriter(
                OutputStreamWriter(
                    openFileOutput(file, MODE_PRIVATE)
                )
            )
            // пишем данные
            bw.write(json)
            // закрываем поток
            bw.close()
            Log.d("MyLog", "Файл записан")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readJson(){
        try {
            // открываем поток для чтения
            val br = BufferedReader(
                InputStreamReader(
                    openFileInput(file)
                )
            )
            var str = ""
            str = br.readLine()
            // читаем содержимое
            if (str != null) {
                val moshi = Moshi.Builder().build()
                val listType = Types.newParameterizedType(List::class.java, HeroInfo::class.java)
                val adapter: JsonAdapter<List<HeroInfo>> = moshi.adapter(listType)
                heroInfo = adapter.fromJson(str)!!
            } else {
                getHeroInfo()
                while (heroInfo.isEmpty()) {
                    continue
                }
                saveJson()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initRcV() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        rcView.adapter = HeroAdapter(this@MainActivity, heroInfo)
    }

    override fun onClickItem(heroInfo: List<HeroInfo>, position: Int) {
        val intentHero = Intent(this, HeroInfoActivity::class.java)
        intentHero.putExtra(KEY_ID,position)
        startActivity(intentHero)
    }

    companion object{
        var heroInfo = listOf<HeroInfo>() // Bundle()
    }
}