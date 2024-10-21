package com.example.pract24_var2_var2

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.ListFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class Fragment2 : Fragment() {

    private lateinit var navController: NavController

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var spinner: Spinner
    private lateinit var typeEditText: EditText
    private lateinit var moneyEditText: EditText
    private lateinit var bankEditText: EditText
    private lateinit var dateEditText: EditText

    private var selectedItem: String = "null"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)


        bankEditText = view.findViewById(R.id.bankET)
        dateEditText = view.findViewById(R.id.dateET)
        typeEditText = view.findViewById(R.id.type_salary_ET)
        moneyEditText = view.findViewById(R.id.money_ET)

        spinner = view.findViewById(R.id.my_spinner)
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
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(requireContext(), "Не выбран тип валюты", Toast.LENGTH_SHORT).show()
            }
        }
        sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)

        val type = sharedPreferences.getString("type", null)
        val money = sharedPreferences.getString("money", null)
        selectedItem = sharedPreferences.getString("selectedItem", null).toString()

        selectedItem.let {
            val position = options.indexOf(it)
            if (position >= 0) {
                spinner.setSelection(position)
            }
        }
        typeEditText.setText(type)
        moneyEditText.setText(money)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        view.findViewById<Button>(R.id.button_f2).setOnClickListener {
            val bank = bankEditText.text.toString()
            val date = dateEditText.text.toString()
            if (bank.isNotEmpty() && date.isNotEmpty()) {
                if (isValidDate(dateEditText.text.toString())){

                    val his = HistoryItem(
                        typeEditText.text.toString(),
                        moneyEditText.text.toString() + selectedItem,
                        bankEditText.text.toString(),
                        dateEditText.text.toString()
                    )
                    Fragment3.addHis(requireContext(), his)
                    saveData(bank, date)
                    navController.navigate(R.id.fragment3)
                } else {
                Toast.makeText(
                    context,
                    "Введённая дата не является корректной!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(bank: String, date: String) {
        sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("bank", bank)
        editor.putString("date", date)
        editor.apply()
    }

    fun isValidDate(dateString: String, dateFormat: String = "dd.MM.yyyy"): Boolean {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(dateString) != null
        } catch (e: ParseException) {
            false
        }
    }
}