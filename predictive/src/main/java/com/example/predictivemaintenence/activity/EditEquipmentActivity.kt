package com.example.predictivemaintenence.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64.decode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.predictivemaintenence.ApiClientt
import com.example.predictivemaintenence.R
import com.example.predictivemaintenence.adapters.CustomAdapterAutocomplete
import com.example.predictivemaintenence.modelClass.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.Byte.decode
import java.util.*
import kotlin.collections.ArrayList

class EditEquipmentActivity : AppCompatActivity() {
    private val workspace: String = "CMMS-DEMO-112020-001"
    private val token: String ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYzOTE0OTI3MywiaWF0IjoxNjM5MDQxMjczfQ.wrlKTVi1x6pAge_LMvf-rZfizJpK27Dd_9tSanyIF22ZYdQTiGAjGk_fZochtnscRt1ZreyQeAG2cxyEuZ42gw"
    lateinit var equipId: String

    var receivedParamList: ArrayList<ParameterModelClass> = ArrayList()
    var receivedMonitorList: ArrayList<MonitorModelClass> = ArrayList()
    val parList: ArrayList<Int> = ArrayList()

    lateinit var comments: String
    lateinit var description: String
    lateinit var fileString: String
    private var tId: Long = 0
    private var createdDate: String? = null

    private lateinit var img: ImageView
    private lateinit var descEt: TextInputEditText
    private lateinit var commentEt: TextInputEditText
    private lateinit var parentLinearLayoutParameter: LinearLayout
    private lateinit var parentLinearLayoutMonitor: LinearLayout

    var paramList: ArrayList<Int> = ArrayList()
    var monitorList: ArrayList<Int> = ArrayList()

    private var validParamterValidatuion: Boolean = false
    private var validMonitorValidatuion: Boolean = false

    val parameterListModel: ArrayList<ParameterModelClass> = ArrayList()
    val monitorListModel: ArrayList<MonitorModelClass> = ArrayList()
    var predectiveMonitorList: ArrayList<PredictiveMonitorClass> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_equipment)

        equipId = intent.getStringExtra("id").toString()
        getdataCall(equipId)


        descEt = findViewById(R.id.description)
        commentEt = findViewById(R.id.comment)
        val addParameterBtn = findViewById<MaterialButton>(R.id.btn_add_parameter)
        val addMonitorBtn = findViewById<MaterialButton>(R.id.btn_add_monitor)
        val addCrateEauip = findViewById<Button>(R.id.btn_create)
        val delParBtn = findViewById<MaterialButton>(R.id.btn_del_parameter)
        val delMonitor = findViewById<MaterialButton>(R.id.btn_del_monitor)

        val btn = findViewById<Button>(R.id.btn)
        img = findViewById(R.id.img1)

        getMonitorlistCall()

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                img.visibility = View.VISIBLE
                img.setImageURI(uri)

                val byteArrayOutputStream = ByteArrayOutputStream()
                val bitmap = (img.drawable as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
                val imageString: String = Base64.getEncoder().encodeToString(imageBytes)
                fileString = imageString

            })
        btn.setOnClickListener {

            getAction.launch("image/*")

        }

        addParameterBtn.setOnClickListener {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.add_parameter_fileds, null)
            parentLinearLayoutParameter.addView(rowView, parentLinearLayoutParameter.childCount)

            paramList.add(paramList.size + 1)
        }
        addMonitorBtn.setOnClickListener {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.add_monitoring_fields, null)
            parentLinearLayoutMonitor.addView(rowView, parentLinearLayoutMonitor.childCount)
            monitorList.add(monitorList.size + 1)

            val act: AutoCompleteTextView =
                parentLinearLayoutMonitor.getChildAt(monitorList.size - 1)
                    .findViewById(R.id.m_class)

            val adapter3 =
                CustomAdapterAutocomplete(
                    this,
                    R.layout.item_auto_complete_text_view,
                    predectiveMonitorList
                )
            act.setAdapter(adapter3)


        }
        addCrateEauip.setOnClickListener {
            parameterListModel.clear()
            monitorListModel.clear()


            if (paramList.size > 0) {
                for (i in 0 until paramList.size) {
                    val eetParameter = parentLinearLayoutParameter.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.parameter1)
                    val eetValue = parentLinearLayoutParameter.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.value1)
                    val eetUnit = parentLinearLayoutParameter.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.unit1)

                    val paramterString = eetParameter.text.toString()
                    val valueString = eetValue.text.toString()
                    val unitString = eetUnit.text.toString()
                    if (eetParameter.text.isNullOrEmpty() || eetValue.text.isNullOrEmpty()
                        || eetUnit.text.isNullOrEmpty()
                    ) {
                        Toast.makeText(
                            this@EditEquipmentActivity,
                            "Please fill all fields in Parameter View",
                            Toast.LENGTH_SHORT
                        ).show()
                        validParamterValidatuion = false
                    } else {
                        val parameterItem =
                            ParameterModelClass(0, paramterString, valueString, unitString)
                        parameterListModel.add(parameterItem)
                        validParamterValidatuion = true
                    }
                }
            }
            if (monitorList.size > 0) {
                for (i in 0 until monitorList.size) {
                    val mType = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_type)
                    val mClass = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<AutoCompleteTextView>(R.id.m_class)
                    val mReadingDate = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_reading)
                    val mUnit = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_unit)
                    val mLowLeve = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_allowe_low_lev_val)
                    val mHoghLevel = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_allow_high_lev_val)
                    val mActualValue = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_actual_val)
                    val mReadingDelta = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<TextInputEditText>(R.id.m_reading_delta)

                    if (mType.text.isNullOrEmpty() || mClass.text.isNullOrEmpty() || mReadingDate.text.isNullOrEmpty() ||
                        mUnit.text.isNullOrEmpty() || mLowLeve.text.isNullOrEmpty() || mHoghLevel.text.isNullOrEmpty() ||
                        mActualValue.text.isNullOrEmpty() || mReadingDelta.text.isNullOrEmpty()
                    ) {
                        Toast.makeText(
                            this@EditEquipmentActivity,
                            "Please fill all fields in Monitor",
                            Toast.LENGTH_SHORT
                        ).show()
                        validMonitorValidatuion = false
                    } else {
                        val typeString = mType.text.toString()
                        val classString = mClass.text.toString()
                        val readingdateString = mReadingDate.text.toString()
                        val unitString = mUnit.text.toString()
                        val lowLevelString = mLowLeve.text.toString()
                        val highLevelString = mHoghLevel.text.toString()
                        val actialvalueString = mActualValue.text.toString()
                        val readingDeltaString = mReadingDelta.text.toString()
                        var classId: Long = 0
                        for (i in 0 until predectiveMonitorList.size) {
                            if (predectiveMonitorList.get(i).className.equals(classString)) {
                                classId = predectiveMonitorList.get(i).id

                            }
                        }
                        val predectiveMonitorClass = PredictiveMonitorClass(classString, classId)
                        val monitorModelClass = MonitorModelClass(
                            null,
                            typeString,
                            unitString,
                            readingdateString,
                            lowLevelString.toDouble(),
                            highLevelString.toDouble(),
                            actialvalueString.toDouble(),
                            readingDeltaString.toDouble(),
                            predectiveMonitorClass
                        )
                        monitorListModel.add(monitorModelClass)
                        validMonitorValidatuion = true
                    }
                }

            }
            if (monitorListModel.size < 1 || parameterListModel.size < 1) {
                Toast.makeText(
                    this@EditEquipmentActivity,
                    "Monitor/Parameter cannot empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!validMonitorValidatuion || !validParamterValidatuion) {
                Toast.makeText(
                    this@EditEquipmentActivity,
                    "Empty fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                callUpdateEquipment()
            }
        }
        delMonitor.setOnClickListener {
            val monList: ArrayList<Int> = ArrayList()
            if (monitorList.size > 0) {
                for (i in 0 until monitorList.size) {
                    val checkBoxPar = parentLinearLayoutMonitor.getChildAt(i)
                        .findViewById<CheckBox>(R.id.check_box_monitor)
                    if (checkBoxPar.isChecked) {
                        if (monitorList.size <= receivedMonitorList.size && receivedMonitorList.get(
                                i
                            ).id != null
                        ) {
                            deletemonitor(receivedMonitorList.get(i).id!!)
                            receivedMonitorList.removeAt(i)
                            monList.add(i)
                        } else {
                            monList.add(i)
                        }
                    }
                }
                for (i in monList.size downTo 1) {
                    parentLinearLayoutMonitor.removeViewAt(monList.get(i - 1))
                    monitorList.removeAt(monList.get(i - 1))
                    monList.clear()
                }
            }
        }
        delParBtn.setOnClickListener {
            if (paramList.size > 0) {
                for (i in 0 until paramList.size) {
                    val checkBoxPar = parentLinearLayoutParameter.getChildAt(i)
                        .findViewById<CheckBox>(R.id.check_box)
                    if (checkBoxPar.isChecked) {
                        if (paramList.size <= receivedParamList.size && receivedParamList.get(i).id != null) {
                            deleteParam(receivedParamList.get(i).id!!)
                            receivedParamList.removeAt(i)
                            parList.add(i)
                        } else {
                            parList.add(i)
                        }
                    }
                }
                for (i in parList.size downTo 1) {
                    parentLinearLayoutParameter.removeViewAt(parList.get(i - 1))
                    paramList.removeAt(parList.get(i - 1))
                    parList.clear()


                }
            }
        }

    }

    private fun callUpdateEquipment() {
        val commentEt = findViewById<TextInputEditText>(R.id.comment)
        val commentString = commentEt.text.toString()
        val descriptionEt = findViewById<TextInputEditText>(R.id.description)
        val descriptionString = descriptionEt.text.toString()
        val user = User(1)
        val equipmentObj = EquipmentClass(equipId.toLong())
        val thrushold = ThrushHoldValueClass(
            tId, createdDate,
            equipmentObj, commentString, descriptionString,
            fileString, user, parameterListModel, monitorListModel
        )
        val apiCall = ApiClientt.instance.getupdate(workspace, token, thrushold)
        apiCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    Toast.makeText(
                        this@EditEquipmentActivity,
                        "Updated Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    this@EditEquipmentActivity.finish()

                } else {
                    Toast.makeText(
                        this@EditEquipmentActivity,
                        "Error while Updating",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@EditEquipmentActivity,
                    "Failed due to : " + t.cause,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun getdataCall(equipId: String) {
        val apiCall = ApiClientt.instance.checkEquipmentCreated(
            workspace,
            token, equipId.toLong()
        )
        apiCall.enqueue(object : Callback<ThrushHoldValueClass> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<ThrushHoldValueClass>,
                response: Response<ThrushHoldValueClass>
            ) {

                if (response.code() == 200) {
                    val recievedThrushHoldValueClass = response.body()!!
                    tId = recievedThrushHoldValueClass.id!!
                    createdDate = recievedThrushHoldValueClass.createdDate

                    receivedParamList = recievedThrushHoldValueClass.parameterList
                    receivedMonitorList = recievedThrushHoldValueClass.monitorPointList

                    descEt.setText(recievedThrushHoldValueClass.taskDescription)
                    commentEt.setText(recievedThrushHoldValueClass.comments)
                    comments = recievedThrushHoldValueClass.comments
                    description = recievedThrushHoldValueClass.taskDescription
                    fileString = recievedThrushHoldValueClass.fileName.toString()

                    updateUi()
                }
            }

            override fun onFailure(call: Call<ThrushHoldValueClass>, t: Throwable) {
                Toast.makeText(
                    this@EditEquipmentActivity,
                    "Failed due to :" + t.cause,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateUi() {
        parentLinearLayoutParameter = findViewById(R.id.parent_layout)
        parentLinearLayoutMonitor = findViewById(R.id.parent_layout_monitor)


        for (i in 0 until receivedParamList.size) {

            paramList.add(paramList.size + 1)
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.add_parameter_fileds, null)
            parentLinearLayoutParameter.addView(rowView, parentLinearLayoutParameter.childCount)

            val eetParameter = parentLinearLayoutParameter.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.parameter1)
            val eetValue = parentLinearLayoutParameter.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.value1)
            val eetUnit = parentLinearLayoutParameter.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.unit1)

            eetParameter.isEnabled = false
            eetUnit.isEnabled = false
            eetValue.isEnabled = false

            eetParameter.setText(receivedParamList[i].parameter)
            eetValue.setText(receivedParamList[i].fieldValue)
            eetUnit.setText(receivedParamList[i].fieldUnit)
        }

        for (i in 0 until receivedMonitorList.size) {
            monitorList.add(monitorList.size + 1)
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.add_monitoring_fields, null)
            parentLinearLayoutMonitor.addView(rowView, parentLinearLayoutMonitor.childCount)

            val mType = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_type)

            val mClass = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<AutoCompleteTextView>(R.id.m_class)

            //conver date here
            val dateInString = receivedMonitorList[i].date
            dateInString.substring(0, 10)

            val mReadingDate = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_reading)

            val mUnit = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_unit)

            val mLowLeve = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_allowe_low_lev_val)

            val mHoghLevel = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_allow_high_lev_val)

            val mActualValue = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_actual_val)

            val mReadingDelta = parentLinearLayoutMonitor.getChildAt(i)
                .findViewById<TextInputEditText>(R.id.m_reading_delta)


            mType.setText(receivedMonitorList[i].monitorType)
            mClass.setText(receivedMonitorList[i].predictiveMonClass.className)
            mReadingDate.setText(dateInString.substring(0, 10))
            mUnit.setText(receivedMonitorList[i].unit)
            mLowLeve.setText(receivedMonitorList[i].lowValue.toString())
            mHoghLevel.setText(receivedMonitorList[i].highValue.toString())
            mActualValue.setText(receivedMonitorList[i].actualValue.toString())
            mReadingDelta.setText(receivedMonitorList[i].readingDelta.toString())

            mType.isEnabled = false
            mClass.isEnabled = false
            mReadingDate.isEnabled = false
            mUnit.isEnabled = false
            mLowLeve.isEnabled =false
            mHoghLevel.isEnabled = false
            mActualValue.isEnabled =false
            mReadingDelta.isEnabled = false

        }
    }

    private fun getMonitorlistCall() {
        val apiCall = ApiClientt.instance.getMonitorList(workspace, token)
        apiCall.enqueue(object : Callback<List<PredictiveMonitorClass>> {
            override fun onResponse(
                call: Call<List<PredictiveMonitorClass>>,
                response: Response<List<PredictiveMonitorClass>>
            ) {
                if (response.code() == 200) {
                    predectiveMonitorList = response.body() as ArrayList<PredictiveMonitorClass>
                } else {
                    Toast.makeText(this@EditEquipmentActivity, "error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PredictiveMonitorClass>>, t: Throwable) {
                Toast.makeText(
                    this@EditEquipmentActivity,
                    "failed : " + t.cause,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        })


    }

    fun dateMethod(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dtv = view.findViewById<TextInputEditText>(R.id.m_reading)

        val datePickerDialog = DatePickerDialog(
            this@EditEquipmentActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                dtv.setText("$year-$monthOfYear-$dayOfMonth")


            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun deleteParam(paramId: Long) {
        val apiCall = ApiClientt.instance.getDeleteParam(
            workspace,
            token, paramId, tId
        )
        apiCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    Toast.makeText(
                        this@EditEquipmentActivity,
                        "Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
//                    parentLinearLayoutParameter.removeViewAt(i)
//                    paramList.removeAt(parList.get(i))
                } else {
                    Toast.makeText(
                        this@EditEquipmentActivity,
                        "error while deleting",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun deletemonitor(monitorId: Long) {
        val apiCall = ApiClientt.instance.getDeleteMonitor(
            workspace,
            token, monitorId, tId
        )
        apiCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    Toast.makeText(
                        this@EditEquipmentActivity,
                        "Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@EditEquipmentActivity,
                        "error while deleting",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}