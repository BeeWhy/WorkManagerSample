package beewhy.yanina.piper.workmanager

import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import beewhy.yanina.piper.DateUtils

class PeriodicLogWorker : Worker() {

    companion object {
        const val TAG = "PiperWorkerManager"

        const val KEY_TIME = "time"
        const val KEY_META_MESSAGE = "meta_message"
        const val KEY_OUTPUT_PROGRESS = "progress"
        const val KEY_OUTPUT_DONE = "done"
    }


    override fun doWork(): WorkerResult {
        val timeStarted = inputData.getString(KEY_TIME, "unknown time")
        val metaMessage = inputData.getString(KEY_META_MESSAGE, "unknown message")
        Log.d(TAG, "$metaMessage id $id current time: $timeStarted")
        outputData = Data.Builder().putString(
                KEY_OUTPUT_PROGRESS, "in progress").build()

        Thread.sleep(4000)
        val timeFinished = DateUtils.generateDateString()
        Log.d(TAG, "$metaMessage id $id Finished execution at: $timeFinished")
        val timeConsumed = "four seconds" //DateUtils.convertDate(timeFinished) - DateUtils.convertDate(timeStarted)
        outputData = Data.Builder().putString(
                KEY_OUTPUT_DONE, "done, time consumed: $timeConsumed").build()
        return WorkerResult.SUCCESS
    }
}