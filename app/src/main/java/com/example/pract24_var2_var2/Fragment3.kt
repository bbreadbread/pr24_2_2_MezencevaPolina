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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.selects.select
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment3 : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    private var currentPosition: Int = -1
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var historyList: MutableList<HistoryItem> = mutableListOf()

    private lateinit var typeEditText2: EditText
    private lateinit var moneyEditText2: EditText
    private lateinit var bankEditText2: EditText
    private lateinit var dateEditText2: EditText

    private var selectedItem: String = "null"

    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_3, container, false)

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
                selectedItem = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(requireContext(), "Не выбран тип валюты", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bankEditText2 = view.findViewById(R.id.bankET2)
        dateEditText2 = view.findViewById(R.id.dateET2)
        typeEditText2 = view.findViewById(R.id.type_salary_ET2)
        moneyEditText2 = view.findViewById(R.id.money_ET2)

        navController = findNavController()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HistoryAdapter(historyList) { position ->

            currentPosition = position
            val selectedItem = historyList[position]

            typeEditText2.setText(selectedItem.type)
            moneyEditText2.setText(selectedItem.money.dropLast(1))
            bankEditText2.setText(selectedItem.bank)
            dateEditText2.setText(selectedItem.date)
        }

        recyclerView.adapter = adapter

        loadHistory()

        view.findViewById<Button>(R.id.editBut).setOnClickListener {
            if (currentPosition != -1) {
                if (isValidDate(dateEditText2.text.toString())) {
                    val newItem = HistoryItem(
                        typeEditText2.text.toString(),
                        moneyEditText2.text.toString() + selectedItem,
                        bankEditText2.text.toString(),
                        dateEditText2.text.toString()
                    )
                    updateItem(currentPosition, newItem)
                }
                else{
                    Toast.makeText(requireContext(),"Неверный формат даты!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<FloatingActionButton>(R.id.floatingBut).setOnClickListener {
            navController.navigate(R.id.fragment1)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadHistory() {
       sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("history_list", null)
        if (json != null) {
            val type = object : TypeToken<List<HistoryItem>>() {}.type
            val savedHis: List<HistoryItem> = Gson().fromJson(json, type)
            historyList.clear()
            historyList.addAll(savedHis)
            adapter.notifyDataSetChanged()
        }
    }

    fun updateItem(position: Int, newItem: HistoryItem) {
            historyList[position] = newItem
            adapter.notifyItemChanged(position)
            saveHistory()
    }

    private fun saveHistory() {
        val sharedPreferences = requireActivity().getSharedPreferences("history_list", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(historyList)
        editor.putString("history_list", json)
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

    companion object {
        fun addHis(context: Context, task: HistoryItem) {
            val sharedPreferences =
                context.getSharedPreferences("history_list", Context.MODE_PRIVATE)
            val json = sharedPreferences.getString("history_list", null)
            val taskList = if (json != null) {
                val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
                val savedTasks: MutableList<HistoryItem> = Gson().fromJson(json, type)
                savedTasks
            } else {
                mutableListOf()
            }
            taskList.add(task)
            sharedPreferences.edit().putString("history_list", Gson().toJson(taskList)).apply()
        }

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


