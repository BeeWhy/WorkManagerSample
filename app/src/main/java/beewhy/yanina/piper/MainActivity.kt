package beewhy.yanina.piper

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.work.*
import androidx.work.ktx.PeriodicWorkRequestBuilder
import beewhy.yanina.piper.workmanager.PeriodicLogWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val chargingConstraints = Constraints.Builder().setRequiresCharging(true).build()
    private val networkConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

    private val chargingTag = "CHARGING"
    private val networkTag = "NETWORK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val workerManager = WorkManager.getInstance()

        schedulePeriodicChargingRequest(workerManager)

        schedulePeriodicNetworkRequest(workerManager)

        workerManager.getStatusesByTag(chargingTag).observe(this, Observer<MutableList<WorkStatus>> { statuses ->
            if (statuses != null && !statuses.isEmpty()) {
                val workStatus = statuses[0]

                val finished = workStatus.state.isFinished
                val running = workStatus.state == State.RUNNING
                val succeeded = workStatus.state == State.SUCCEEDED

                val outputData = workStatus.outputData
                if (running){
                    Log.d(PeriodicLogWorker.TAG, "running status: ${outputData.getString(PeriodicLogWorker.KEY_OUTPUT_PROGRESS, "unavail")}")
                }
                if (succeeded){
                    Log.d(PeriodicLogWorker.TAG, "succeeded status: ${outputData.getString(PeriodicLogWorker.KEY_OUTPUT_DONE, "unavail")}")
                }
            }
        })

    }

    private fun schedulePeriodicNetworkRequest(workerManager: WorkManager) {
        val periodicNetworkWorkRequest = PeriodicWorkRequestBuilder<PeriodicLogWorker>(15, TimeUnit.MINUTES)
                .addTag(networkTag)
                .setInputData(generateInputData("This is network worker"))
                .setConstraints(networkConstraints)
                .build()
        val wc = workerManager.enqueue(periodicNetworkWorkRequest)


    }

    private fun schedulePeriodicChargingRequest(workerManager: WorkManager) {
        val periodicChargingWorkRequest = PeriodicWorkRequestBuilder<PeriodicLogWorker>(15, TimeUnit.MINUTES)
                .addTag(chargingTag)
                .setInputData(generateInputData("This is charging worker"))
                .setConstraints(chargingConstraints)
                .build()
        workerManager.enqueue(periodicChargingWorkRequest)
    }

    private fun generateInputData(message: String) = Data.Builder()
            .putString(PeriodicLogWorker.KEY_TIME, DateUtils.generateDateString())
            .putString(PeriodicLogWorker.KEY_META_MESSAGE, message)
            .build()

}
