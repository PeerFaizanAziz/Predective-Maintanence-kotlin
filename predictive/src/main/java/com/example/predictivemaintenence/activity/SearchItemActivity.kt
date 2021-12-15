package com.example.predictivemaintenence.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.predictivemaintenence.ApiClientt
import com.example.predictivemaintenence.adapters.ItemAdapter
import com.example.predictivemaintenence.modelClass.SearchEquipmentClass
import com.example.predictivemaintenence.modelClass.ThrushHoldValueClass
import com.example.predictivemaintenence.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchItemActivity : AppCompatActivity(), ItemAdapter.onItemClickListener {

    var list = ArrayList<SearchEquipmentClass>()
    lateinit var recyclerview: RecyclerView
    lateinit var adapter: ItemAdapter
    private val token: String ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYzOTE0OTI3MywiaWF0IjoxNjM5MDQxMjczfQ.wrlKTVi1x6pAge_LMvf-rZfizJpK27Dd_9tSanyIF22ZYdQTiGAjGk_fZochtnscRt1ZreyQeAG2cxyEuZ42gw"

    private val workspace: String = "CMMS-DEMO-112020-001"
    private var selectedEquipId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_item_activity_main)







        val searchView = findViewById<SearchView>(R.id.search_view)
        recyclerview =
            findViewById(R.id.recycler_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                callMethodSearch(newText)
                return false
            }
        })
    }

    private fun callMethodSearch(newText: String?) {
        val apiCall = ApiClientt.instance.getEquipment(workspace, token, newText)

        apiCall.enqueue(object : Callback<List<SearchEquipmentClass>> {
            override fun onResponse(
                call: Call<List<SearchEquipmentClass>>,
                response: Response<List<SearchEquipmentClass>>
            ) {
                if (response.code() == 200) {
                    list.clear()
                    list =
                        response.body() as ArrayList<SearchEquipmentClass>

                    adapter = ItemAdapter(list, this@SearchItemActivity)
                    recyclerview.adapter = adapter
                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                    recyclerview.setHasFixedSize(true)
                    Toast.makeText(applicationContext, "success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error : " + response.code(),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(call: Call<List<SearchEquipmentClass>>, t: Throwable) {
                Log.d("TAG", "onFailure:  cause " + t.cause)
                Log.d("TAG", "onFailure:  message " + t.message)

                Toast.makeText(applicationContext, "failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(position: Int) {

        selectedEquipId = list.get(position).id.toLong()
        checkEquipmentCreatedCall()

    }

    private fun checkEquipmentCreatedCall() {
        val apiCall = ApiClientt.instance.checkEquipmentCreated(
            workspace,
            token,
            selectedEquipId
        )
        apiCall.enqueue(object : Callback<ThrushHoldValueClass> {
            override fun onResponse(
                call: Call<ThrushHoldValueClass>,
                response: Response<ThrushHoldValueClass>
            ) {
                if (response.code() == 404) {

                    val intent =
                        Intent(this@SearchItemActivity, CreateEquipmentActivity::class.java)
                    intent.putExtra("id",selectedEquipId.toString())
                    startActivity(intent)
                } else
                    if (response.code() == 200) {
                        val intent =
                            Intent(this@SearchItemActivity, EditEquipmentActivity::class.java)
                        intent.putExtra("id", selectedEquipId.toString())
                        startActivity(intent)
                    }
            }

            override fun onFailure(call: Call<ThrushHoldValueClass>, t: Throwable) {
                Toast.makeText(
                    this@SearchItemActivity,
                    "Failed due to :" + t.cause,
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("TAG", "onFailure: failed" + t.cause)
            }

        })
    }

}