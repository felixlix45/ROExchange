package com.first.roexchange.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class EtValViewModel : ViewModel() {

    private var listET: MutableLiveData<ArrayList<String>> = MutableLiveData()
    private var listVal: MutableLiveData<ArrayList<String>> = MutableLiveData()

    private var listItemET = ArrayList<String>()
    private var listItemVal = ArrayList<String>()

    private val db = FirebaseFirestore.getInstance()
    private val noteRef = db.collection("URL").document("ET")
    private val noteRefVal = db.collection("URL").document("VAL")

    private var urlET = ""
    private var urlETMini = ""

    private var urlGlobalET = ""
    private var urlGlobalETMini = ""
    private var urlLastUpdated = ""

    private var urlVal = ""
    private var urlGlobalVal = ""
    private var urlValLastUpdated = ""

    fun setET() {
        noteRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                urlET = doc.getString("MVP") ?: ""
                listItemET.add(urlET)
                urlETMini = doc.getString("Mini") ?: ""
                listItemET.add(urlETMini)
                urlGlobalET = doc.getString("GlobalMVP") ?: ""
                listItemET.add(urlGlobalET)
                urlGlobalETMini = doc.getString("GlobalMini") ?: ""
                listItemET.add(urlGlobalETMini)
                urlLastUpdated = doc.getString("LastUpdated") ?: ""
                listItemET.add(urlLastUpdated)

                listET.postValue(listItemET)

            }
        }
    }

    fun getET(): LiveData<ArrayList<String>> {
        return listET
    }

    fun setVal() {
        noteRefVal.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                urlVal = doc.getString("VALList") ?: ""
                listItemVal.add(urlVal)
                urlGlobalVal = doc.getString("ValGlobal") ?: ""
                listItemVal.add(urlGlobalVal)
                urlValLastUpdated = doc.getString("LastUpdated") ?: ""
                listItemVal.add(urlValLastUpdated)

                listVal.postValue(listItemVal)

            }
        }
    }

    fun getVal(): LiveData<ArrayList<String>> {
        return listVal
    }

}