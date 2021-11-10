package cjsimon.pickme

import android.app.Service
import android.content.Intent
import android.os.IBinder
import okhttp3.*
import java.io.IOException

class ShareService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("Service Started!")
        println("Intent: ${intentToString(intent)}")
        handleIntent(intent)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.getStringExtra(Intent.EXTRA_TEXT)?.let {
            println("Incoming Data: $it")
            val url = "http://10.0.0.87:3000/youtube"
            val jsonPayload = """
                {
                    "link": "$it"
                }
            """
            postJson(url, jsonPayload)
        }
    }

    private fun postJson(url: String, jsonPayload: String) {
        val jsonMediaType: MediaType = MediaType.get("application/json; charset=utf-8")
        val client = OkHttpClient()

        val requestBody = RequestBody.create(jsonMediaType, jsonPayload)
        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) = println(e.toString())
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }

    /* Helpers */
    private fun intentToString(intent: Intent?): String {
        if(intent == null)
            return ""

        val output = StringBuilder()
            .append("action: ")
            .append(intent?.action)
            .append("\ndata: ")
            .append(intent?.dataString)
            .append("\nextras:\n\t")

        for(key in intent?.extras!!.keySet())
            output
                .append(key)
                .append("=")
                .append(intent?.extras!!.get(key))
                .append("\n\t")

        return output.toString()
    }
}