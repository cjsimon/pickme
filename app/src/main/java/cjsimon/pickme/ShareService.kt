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
        
        println(StringBuilder()
            .append("Intent:\n")
            .append(intentToString(intent))
        )
        
        handleIntent(intent)
        
        return super.onStartCommand(intent, flags, startId)
    }
    
    private fun handleIntent(intent: Intent?) {
        val extraSubject = intent?.getStringExtra(Intent.EXTRA_SUBJECT)
        val extraText    = intent?.getStringExtra(Intent.EXTRA_TEXT)
        
        extraText.let {
            println("Incoming Data: $it")
            
            val url = "https://endpoint.local/youtube"
            val jsonPayload = """
                {
                    "title": ""
                    "link": "$it"
                    
                }
            """
            
            postJson(url, jsonPayload)
        }
    }
    
    private fun postJson(url: String, jsonPayload: String) {
        val jsonMediaType: MediaType = MediaType.get("application/json; charset=utf-8")
        
        val requestBody = RequestBody.create(jsonMediaType, jsonPayload)
        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()
        
        val client = OkHttpClient()
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
            .append("action: ").append(intent?.action)
            .append("\n")
            
            .append("data: ").append(intent?.dataString)
            .append("\n")
            
            .append("extras:")
            .append("\n\t")
                for(key in intent?.extras!!.keySet())
                    output
                        .append("${key}=${intent?.extras!!.get(key)}")
                        .append("\n\t")
        
        return output.toString()
    }
}
