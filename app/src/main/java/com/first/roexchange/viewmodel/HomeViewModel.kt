package com.first.roexchange.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val informationRef = db.collection("URL").document("Information")


    private val _information = MutableLiveData<String>()
    val information: LiveData<String> = this._information

    fun getInformation() = this.viewModelScope.launch(Dispatchers.IO) {
        informationRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                this@HomeViewModel._information.postValue(doc.getString("info"))
            }
        }
    }
}