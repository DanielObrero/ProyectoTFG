package com.example.proyectotfg.Principal.Turista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectotfg.databinding.FragmentHomeBinding
import com.example.proyectotfg.databinding.FragmentHomeTBinding

class HomeFragmentT : Fragment() {
    private lateinit var mBinding: FragmentHomeTBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeTBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


}
