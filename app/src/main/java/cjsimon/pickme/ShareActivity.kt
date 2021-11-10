package cjsimon.pickme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ShareActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        
        handleIntent(this.intent)
        
        finish() // Activity closes after this
        
        /*
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            Toast.makeText(this@ShareActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
        }
        */
    }
    
    private fun handleIntent(intent: Intent) {
        
        /*
        when {
            intent?.action == Intent.ACTION_SEND -> {
                when(true) {
                    intent?.type == "text/plain"       -> handleTextPlain(intent)
                    intent?.type == "application/json" -> handleApplicationJson(intent)
                    intent?.type?.startsWith("image/") -> handleImage(intent)
                }
            }
            intent?.action == Intent.ACTION_SEND_MULTIPLE -> {
                when(true) {
                    intent?.type?.startsWith("image/") -> handleImages(intent)
                }
            }
            else -> {}
        }
        */
        
        val serviceClass = ShareService::class.java
        val serviceIntent = Intent(baseContext, serviceClass)
        
        serviceIntent.putExtras(intent.extras) // Transfer activity intent-filter bundled data to serviceIntent
        startService(serviceIntent)
    }
}
