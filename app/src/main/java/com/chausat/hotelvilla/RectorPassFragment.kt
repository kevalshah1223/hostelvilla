package com.chausat.hotelvilla

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RectorPassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RectorPassFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rector_pass, container, false)
        val recyclerViewRectorPass = view.findViewById<RecyclerView>(R.id.recyclerViewRectorPass1)

        val arrayListRectorPassHistory = ArrayList<RectorPassHistory>()
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.custom_loading)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("${MainClass().getURL()}rectorPassHistory.php")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                activity!!.run {
                    val data = response.body?.string() as String
                    Log.d("Data", data)
                    val JsonObject = JSONObject(data)
                    val success = JsonObject.getInt("success")
                    if (success == 1) {
                        val jsonData = JsonObject.getJSONArray("passHistory")
                        for(i in 0 until jsonData.length()){
                            arrayListRectorPassHistory.add(
                                    RectorPassHistory(
                                            jsonData.getJSONObject(i).getString("p_id").toInt(),jsonData.getJSONObject(i).getString("stus_id").toInt(),
                                            jsonData.getJSONObject(i).getString("stus_name"),jsonData.getJSONObject(i).getString("hostel_no").toInt(),
                                            jsonData.getJSONObject(i).getString("room_no").toInt(),jsonData.getJSONObject(i).getString("leave_type").toInt(),
                                            jsonData.getJSONObject(i).getString("data_1"), jsonData.getJSONObject(i).getString("data_2"),
                                            jsonData.getJSONObject(i).getString("destination"), jsonData.getJSONObject(i).getString("purpose"),
                                            jsonData.getJSONObject(i).getString("isRectorApproved").toInt(), jsonData.getJSONObject(i).getString("isFacultyApproved").toInt(),
                                            jsonData.getJSONObject(i).getString("isCanceled").toInt()
                                    )
                            )
                        }
                        recyclerViewRectorPass.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                        recyclerViewRectorPass.adapter = RecyclerViewRectorPassHistory(arrayListRectorPassHistory, activity!!)
                    }
                    dialog.dismiss()
                }
            }
        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RectorPassFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RectorPassFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}