package com.ecommerceapp.ui.fragments.home

import com.ecommerceapp.databinding.FragmentRecommendedBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ecommerceapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecommendedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecommendedFragment : Fragment() {
    private var _binding: FragmentRecommendedBinding? = null
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
        _binding = FragmentRecommendedBinding.inflate(inflater, container, false)
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