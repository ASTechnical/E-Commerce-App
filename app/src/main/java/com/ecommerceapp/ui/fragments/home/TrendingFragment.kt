package com.ecommerceapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ecommerceapp.R
import com.ecommerceapp.databinding.FragmentTrendingBinding

class TrendingFragment : Fragment() {
    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setUpUi()
        /* binding.unlockImei.setOnClickListener{
             navController.navigate(R.id.action_homeFragment_to_unlockImeiFragment)
         }
         binding.secretCodes.setOnClickListener{
             val intent = Intent(requireContext(), SecretCodeActivity::class.java)
             startActivity(intent)
             // navController.navigate(R.id.action_homeFragment_to_secretCodeActivity)
         }
         binding.unlockTech.setOnClickListener{
             navController.navigate(R.id.action_homeFragment_to_unlockTechFragment)
         }
         binding.countryCode.setOnClickListener{
             val intent = Intent(requireContext(), CountryCodeActivity::class.java)
             startActivity(intent)

             //  navController.navigate(R.id.action_homeFragment_to_countryCodeFragment)
         }
 */
    }
    private fun setUpUi() {
        binding.all.setOnClickListener { navController.navigate(R.id.allFragment) }
        binding.trending.setOnClickListener { navController.navigate(R.id.trendingFragment) }
        binding.childern.setOnClickListener { navController.navigate(R.id.childrenFragment) }
        binding.recommended.setOnClickListener { navController.navigate(R.id.recommendedFragment) }
        /*  binding?.upcoming?.setOnClickListener { navController.navigate(R.id.upcomingFragment) }
          binding?.recent?.setOnClickListener { navController.navigate(R.id.recentFragment) }
          binding?.home1?.setOnClickListener { navController.navigate(R.id.homeFragment) }*/
    }
    companion object {


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}