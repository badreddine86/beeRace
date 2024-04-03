package com.example.beerace
import retrofit2.*

class BeeRepository(private val apiBee: ApiBeeService) {

    fun getBees(onSuccess: (List<Bee>) -> Unit, onFailure: (String) -> Unit) {
        apiBee.getBeesStatus().enqueue(object : Callback<BeeResponse> {
            override fun onResponse(call: Call<BeeResponse>, response: Response<BeeResponse>) {
                if (response.isSuccessful) {
                    val beeResponse = response.body()
                    beeResponse?.let {
                        onSuccess.invoke(it.beeList)
                    }
                } else {
                    onFailure.invoke("Erreur de récupération de la liste: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BeeResponse>, t: Throwable) {
                onFailure.invoke("Erreur de récupération de la liste: ${t.message}")
            }
        })
    }

    fun getDuration(onSuccess: (RaceDurationReponse) -> Unit, onFailure: (String) -> Unit) {
        apiBee.getRaceDuration().enqueue(object : Callback<RaceDurationReponse> {
            override fun onResponse(call: Call<RaceDurationReponse>, response: Response<RaceDurationReponse>) {
                if (response.isSuccessful) {
                    val beeResponse = response.body()
                    beeResponse?.let {
                        onSuccess.invoke(it)
                    }
                } else {
                    onFailure.invoke("Erreur de récupération de timing: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RaceDurationReponse>, t: Throwable) {
                onFailure.invoke("Erreur de récupération du timing: ${t.message}")
            }
        })
    }
}

