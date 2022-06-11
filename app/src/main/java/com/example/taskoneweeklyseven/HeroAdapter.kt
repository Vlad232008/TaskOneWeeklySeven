package com.example.taskoneweeklyseven

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.taskoneweeklyseven.databinding.HeroListBinding
import com.example.taskoneweeklyseven.json.HeroInfo

class HeroAdapter(private val listener: Listener, private val names: List<HeroInfo>) :
    RecyclerView.Adapter<HeroAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = HeroListBinding.bind(itemView)
        companion object {
            fun create(parent: ViewGroup): MyViewHolder {
                return MyViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.hero_list, parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val base_URl = "https://api.opendota.com${names[position].icon}"
        holder.binding.tvName.text = names[position].localized_name
        holder.binding.ivIcon.load(base_URl)
        holder.binding.btnHero.setOnClickListener {
            listener.onClickItem(names, position)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }
    interface Listener{
        fun onClickItem(heroInfo: List<HeroInfo>, position: Int)
    }
}