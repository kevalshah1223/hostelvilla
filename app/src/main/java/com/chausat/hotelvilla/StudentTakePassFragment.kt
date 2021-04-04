package com.chausat.hotelvilla

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import com.google.android.material.textfield.TextInputLayout
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentTakePassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentTakePassFragment : Fragment(),View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val dayOfWeekArray = arrayOf("THU", "THURSDAY", "SUN", "SUNDAY")

    lateinit var radioGroupPassType : RadioGroup
    lateinit var buttonClearSelection : Button
    lateinit var groupHomePass :Group
    lateinit var groupLatePass :Group
    lateinit var cardViewSelectLatePassDate : CardView
    lateinit var textViewLatePassDate : TextView
    lateinit var cardViewSelectLatePassTime : CardView
    lateinit var textViewLatePassTime : TextView
    lateinit var cardViewSelectHomePassDate : CardView
    lateinit var cardViewSelectHomePassEndDate : CardView
    lateinit var textViewHomePassDate : TextView
    lateinit var textViewHomePassEndDate : TextView
    lateinit var buttonApplyForLatePass : AppCompatButton
    lateinit var buttonApplyForHomePass : AppCompatButton
    lateinit var textInputLayoutLatePassDestination : TextInputLayout
    lateinit var textInputLayoutLatePassPurpose : TextInputLayout
    lateinit var textInputLayoutHomePassDestination : TextInputLayout
    lateinit var textInputLayoutHomePassPurpose : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_student_take_pass, container, false)

        textInputLayoutHomePassDestination = view.findViewById(R.id.textInputLayoutHomePassDestination)
        textInputLayoutHomePassPurpose = view.findViewById(R.id.textInputLayoutHomePassPurpose)
        textInputLayoutLatePassDestination = view.findViewById(R.id.textInputLayoutLatePassDestination)
        textInputLayoutLatePassPurpose = view.findViewById(R.id.textInputLayoutLatePassPurpose)
        radioGroupPassType = view.findViewById<RadioGroup>(R.id.radioGroupPassType)
        cardViewSelectLatePassDate = view.findViewById<CardView>(R.id.cardViewSelectLatePassDate)
        textViewLatePassDate = view.findViewById<TextView>(R.id.textViewLatePassDate)
        cardViewSelectLatePassTime = view.findViewById<CardView>(R.id.cardViewSelectLatePassTime)
        textViewLatePassTime = view.findViewById<TextView>(R.id.textViewLatePassTime)
        groupHomePass = view.findViewById<Group>(R.id.groupHomePass)
        groupLatePass = view.findViewById<Group>(R.id.groupLatePass)
        buttonClearSelection = view.findViewById<Button>(R.id.buttonClearSelection)
        cardViewSelectHomePassDate = view.findViewById(R.id.cardViewSelectHomePassDate)
        textViewHomePassDate = view.findViewById(R.id.textViewHomePassDate)
        cardViewSelectHomePassEndDate = view.findViewById(R.id.cardViewSelectHomePassEndDate)
        textViewHomePassEndDate = view.findViewById(R.id.textViewHomePassEndDate)
        buttonApplyForHomePass = view.findViewById(R.id.buttonApplyForHomePass)
        buttonApplyForLatePass = view.findViewById(R.id.buttonApplyForLatePass)
        radioGroupPassType.setOnCheckedChangeListener(this)
        buttonClearSelection.setOnClickListener(this)
        cardViewSelectLatePassDate.setOnClickListener(this)
        cardViewSelectLatePassTime.setOnClickListener(this)
        cardViewSelectHomePassDate.setOnClickListener(this)
        cardViewSelectHomePassEndDate.setOnClickListener(this)

        buttonApplyForHomePass.setOnClickListener(this)
        buttonApplyForLatePass.setOnClickListener(this)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentTakePassFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                StudentTakePassFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(v: View?) {
        val calendarEndDate: Calendar = Calendar.getInstance()
        when(v!!.id){
            R.id.buttonClearSelection -> {
                radioGroupPassType.clearCheck()
                buttonClearSelection.visibility = View.INVISIBLE
                groupHomePass.visibility = View.GONE
                groupLatePass.visibility = View.GONE
                clearData()
            }

            R.id.cardViewSelectLatePassDate -> {
                val year = Calendar.getInstance().get(Calendar.YEAR)
                val month = Calendar.getInstance().get(Calendar.MONTH)
                val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(v.context, { _, year, month, dayOfMonth ->
                    val date = Date("${month + 1}/$dayOfMonth/$year")
                    val formatter = SimpleDateFormat("EEE")
                    val strDate: String = formatter.format(date)

                    if (strDate.toUpperCase() in dayOfWeekArray) {
                        val dateFormatter = SimpleDateFormat("dd MMM, yyyy")
                        textViewLatePassDate.text = dateFormatter.format(date)
                    } else {
                        val toast = Toast.makeText(
                                v.context,
                                "Select the Date for Thursday and Sunday Only",
                                Toast.LENGTH_LONG
                        )
                        toast.show()
                        textViewLatePassDate.text = getString(R.string._00_00_0000)
                    }
                }, year, month, dayOfMonth)

                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            R.id.cardViewSelectLatePassTime -> {
                val picker = TimePickerDialog(v.context, { _, hourOfDay, minute ->
                    val calender = Calendar.getInstance()
                    calender.set(0, 0, 0, hourOfDay, minute)
                    textViewLatePassTime.text = android.text.format.DateFormat.format(
                            "hh:mm aa",
                            calender
                    )
                }, 12, 0, false)
                picker.show()
            }

            R.id.cardViewSelectHomePassDate -> {
                val year = Calendar.getInstance().get(Calendar.YEAR)
                val month = Calendar.getInstance().get(Calendar.MONTH)
                val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(v.context, { _, year, month, dayOfMonth ->
                    val date = Date("${month + 1}/$dayOfMonth/$year")
                    val dateFormatter = SimpleDateFormat("dd MMM, yyyy")

                    calendarEndDate.set(Calendar.YEAR,year)
                    calendarEndDate.set(Calendar.DAY_OF_MONTH,dayOfMonth-1)
                    calendarEndDate.set(Calendar.MONTH,month)

                    textViewHomePassDate.text = dateFormatter.format(date)
                }, year, month, dayOfMonth)
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }

            R.id.cardViewSelectHomePassEndDate -> {
                val year = Calendar.getInstance().get(Calendar.YEAR)
                val month = Calendar.getInstance().get(Calendar.MONTH)
                val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(v.context, { _, year, month, dayOfMonth ->
                    val date = Date("${month + 1}/$dayOfMonth/$year")
                    val dateFormatter = SimpleDateFormat("dd MMM, yyyy")
                    textViewHomePassEndDate.text = dateFormatter.format(date)
                }, year, month, dayOfMonth)
                datePickerDialog.datePicker.minDate = calendarEndDate.timeInMillis
                datePickerDialog.show()
            }

            R.id.buttonApplyForLatePass -> {
                val pref = activity!!.getSharedPreferences(LoginActivity.LOGIN, AppCompatActivity.MODE_PRIVATE)
                if (textViewLatePassDate.text.toString() == getString(R.string._00_00_0000)) {
                    Toast.makeText(activity!!, "Select Late Pass Date", Toast.LENGTH_SHORT).show()
                    return
                }
                if (textViewLatePassTime.text.toString() == getString(R.string._00_00_am_pm)) {
                    Toast.makeText(activity!!, "Select Late Pass Time", Toast.LENGTH_SHORT).show()
                    return
                }

                if (textInputLayoutLatePassDestination.editText!!.text.isEmpty()) {
                    Toast.makeText(activity!!, "Enter Destination", Toast.LENGTH_SHORT).show()
                    return
                }

                if (textInputLayoutLatePassPurpose.editText!!.text.isEmpty()) {
                    Toast.makeText(activity!!, "Enter Purpose", Toast.LENGTH_SHORT).show()
                    return
                }
                val leaveType = if (view!!.findViewById<AppCompatRadioButton>(R.id.radioHomePass).isChecked) 0 else 1
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url("${MainClass().getURL()}insertPass.php?studID=${pref.getString(LoginActivity.USER_ID, null)}&studPass=${pref.getString(LoginActivity.USER_PASS, null)}&leaveType=${leaveType}&data1=${textViewLatePassDate.text.toString()}&data2=${textViewLatePassTime.text.toString()}&destination=${textInputLayoutLatePassDestination.editText!!.text.toString()}&purpose=${textInputLayoutLatePassPurpose.editText!!.text.toString()}")
                        .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        activity!!.runOnUiThread {
                            val dialog = Dialog(activity!!)
                            dialog.setContentView(R.layout.custom_loading)
                            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.show()
                            val data = response.body?.string() as String
                            Log.d("Data", data)
                            val JsonObject = JSONObject(data)
                            val success = JsonObject.getInt("success")
                            if (success == 1) {
                                Log.d("TAG", "onResponse: Applied")
                                Toast.makeText(activity!!, "Your pass is Applied", Toast.LENGTH_SHORT).show()
                                clearData()
                            } else {
                                Log.d("TAG", "onResponse: Faild")
                                Toast.makeText(activity!!, "Error in Registering", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        }
                    }
                })
            }

            R.id.buttonApplyForHomePass -> {

                val pref = activity!!.getSharedPreferences(LoginActivity.LOGIN, AppCompatActivity.MODE_PRIVATE)
                if (textViewHomePassDate.text.toString() == getString(R.string._00_00_0000)) {
                    Toast.makeText(activity!!, "Select Home Pass Start Date", Toast.LENGTH_SHORT).show()
                    return
                }
                if (textViewHomePassEndDate.text.toString() == getString(R.string._00_00_0000)) {
                    Toast.makeText(activity!!, "Select Home Pass End Date", Toast.LENGTH_SHORT).show()
                    return
                }

                if (textInputLayoutHomePassDestination.editText!!.text.isEmpty()) {
                    Toast.makeText(activity!!, "Enter Destination", Toast.LENGTH_SHORT).show()
                    return
                }

                if (textInputLayoutHomePassPurpose.editText!!.text.isEmpty()) {
                    Toast.makeText(activity!!, "Enter Purpose", Toast.LENGTH_SHORT).show()
                    return
                }
                val leaveType = if (view!!.findViewById<AppCompatRadioButton>(R.id.radioHomePass).isChecked) 0 else 1
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url("${MainClass().getURL()}insertPass.php?studID=${pref.getString(LoginActivity.USER_ID, null)}&studPass=${pref.getString(LoginActivity.USER_PASS, null)}&leaveType=${leaveType}&data1=${textViewHomePassDate.text.toString()}&data2=${textViewHomePassEndDate.text.toString()}&destination=${textInputLayoutHomePassDestination.editText!!.text.toString()}&purpose=${textInputLayoutHomePassPurpose.editText!!.text.toString()}")
                        .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        activity!!.runOnUiThread {
                            val dialog = Dialog(activity!!)
                            dialog.setContentView(R.layout.custom_loading)
                            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.show()
                            val data = response.body?.string() as String
                            Log.d("Data", data)
                            val JsonObject = JSONObject(data)
                            val success = JsonObject.getInt("success")
                            if (success == 1) {
                                Toast.makeText(activity!!, "Your pass is Applied", Toast.LENGTH_SHORT).show()
                                clearData()
                            } else {
                                Toast.makeText(activity!!, "Error in Registering", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        }
                    }
                })
            }
        }
    }

    private fun clearData() {
        textViewHomePassEndDate.text = getString(R.string._00_00_0000)
        textViewHomePassDate.text = getString(R.string._00_00_0000)
        textInputLayoutHomePassDestination.editText!!.text.clear()
        textInputLayoutHomePassPurpose.editText!!.text.clear()


        textViewLatePassDate.text = getString(R.string._00_00_0000)
        textViewLatePassTime.text = getString(R.string._00_00_am_pm)
        textInputLayoutLatePassDestination.editText!!.text.clear()
        textInputLayoutLatePassPurpose.editText!!.text.clear()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.radioHomePass -> {
                groupHomePass.visibility = View.VISIBLE
                groupLatePass.visibility = View.GONE
            }
            R.id.radioLatePass -> {
                groupLatePass.visibility = View.VISIBLE
                groupHomePass.visibility = View.GONE
            }
        }
        buttonClearSelection.visibility = View.VISIBLE
    }
}