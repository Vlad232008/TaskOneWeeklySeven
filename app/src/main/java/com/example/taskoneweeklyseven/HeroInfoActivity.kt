package com.example.taskoneweeklyseven

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.taskoneweeklyseven.databinding.ActivityHeroInfoBinding

class HeroInfoActivity: AppCompatActivity() {
    lateinit var binding: ActivityHeroInfoBinding
    var heroInfo = MainActivity.heroInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        refreshActivity()

    }
    companion object{
        const val KEY_ID  = "id"
    }

    private fun refreshActivity() = with(binding){
        val count = intent.getIntExtra(KEY_ID,0)
        val base_URl = "https://api.opendota.com${heroInfo[count].img}"
        ivImage.load(base_URl)
        tvName.text = heroInfo[count].localized_name
        tvAgi.text = ""//heroInfo[count].agi_gain.toString()
        tvStr.text = ""//heroInfo[count].str_gain.toString()
        tvInt.text = ""//heroInfo[count].int_gain.toString()
        tvSpeed.text = heroInfo[count].move_speed.toString()
        tvRange.text = heroInfo[count].attack_range.toString()
        tvHealth.text = heroInfo[count].base_health.toString()
        tvMana.text = heroInfo[count].base_mana.toString()
        val attack = (heroInfo[count].base_attack_min + heroInfo[count].base_attack_max)/2
        tvBaseAttack.text = attack.toString()
        tvPrimaryAttr.text = when(heroInfo[count].primary_attr) {
            "str" -> "Сила"
            "agi" -> "Ловкость"
            "int" -> "Интеллект"
            else -> {
                ""
            }
        }
    }
}