package com.example.taskoneweeklyseven

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskoneweeklyseven.HeroInfoActivity.Companion.KEY_ID
import com.example.taskoneweeklyseven.databinding.ActivityMainBinding
import com.example.taskoneweeklyseven.json.HeroInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.*
import java.io.IOException

open class MainActivity : AppCompatActivity(), HeroAdapter.Listener {
    lateinit var json:String
    lateinit var binding: ActivityMainBinding
    private val URL_HEROINFO = "https://api.opendota.com/api/heroStats"
    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val projection =
            arrayOf(MyContentProvider._ID, MyContentProvider.VALUE)
        val rs = contentResolver.query(
            MyContentProvider.CONTENT_URI,
            projection, null, null, null
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (rs?.moveToNext() == true) {
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, HeroInfo::class.java)
            val adapter: JsonAdapter<List<HeroInfo>> = moshi.adapter(listType)
            heroInfo = adapter.fromJson(rs.getString(1))!!
            initRcV()
        }
        else {
            getHeroInfo()
            while (heroInfo.isEmpty()) {
                continue
            }
            insertContentProvider(json)
            initRcV()
        }
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

    private fun insertContentProvider(value: String){
        val projection =
            arrayOf(MyContentProvider._ID, MyContentProvider.VALUE)
        val rs = contentResolver.query(
            MyContentProvider.CONTENT_URI,
            projection, null, null, null
        )
        val cv = ContentValues()
        cv.put(MyContentProvider.VALUE, value)
        contentResolver.insert(MyContentProvider.CONTENT_URI, cv)
        rs?.requery()
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