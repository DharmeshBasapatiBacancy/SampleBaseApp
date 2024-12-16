package com.bacancy.samplebaseapp.silentUpdates

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bacancy.samplebaseapp.forKoin.UserRepository
import com.bacancy.samplebaseapp.local.TbUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit

class ApiWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params),
    KoinComponent {

    private val userRepository: UserRepository by inject(UserRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            Log.d("ApiWorker", "doWork: Called")
            val response = userRepository.getUsers()
            Log.d("ApiWorker", "API Response: $response")
            withContext(Dispatchers.IO){
                val users = response.map {
                    TbUser(it.id, it.name, it.email)
                }
                userRepository.insertAllUsers(users)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("ApiWorker", "Exception: ${e.message}")
            Result.retry()
        } finally {
            val newWorkRequest = OneTimeWorkRequestBuilder<ApiWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(applicationContext).enqueue(newWorkRequest)
        }
    }
}