package com.example.pract24_var2_var2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class Fragment1 : Fragment() {


    private lateinit var spinner: Spinner
    private lateinit var navController: NavController
    private lateinit var typeEditText: EditText
    private lateinit var moneyEditText: EditText
    private var selectedItem: String = "null"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)

        spinner = view.findViewById(R.id.my_spinner)
        typeEditText = view.findViewById(R.id.type_salary_ET)
        moneyEditText = view.findViewById(R.id.money_ET)

        val options = arrayOf("₽", "$", "€")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(requireContext(), "Не выбран тип валюты", Toast.LENGTH_SHORT).show()
            }
        }


        view.findViewById<Button>(R.id.button_f1).setOnClickListener {
            val bank = typeEditText.text.toString()
            val money = moneyEditText.text.toString()
            if (bank.isNotEmpty() && money.isNotEmpty()) {

                saveData(bank, money, selectedItem)
                navController.navigate(R.id.fragment2)
            } else {

                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, заполните все поля",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        view.findViewById<Button>(R.id.button_f1_2).setOnClickListener {
            navController.navigate(R.id.fragment3)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    private fun saveData(type: String, money: String, selectedItem: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("type", type)
        editor.putString("money", money)
        editor.putString("selectedItem", selectedItem)
        editor.apply()
    }
}