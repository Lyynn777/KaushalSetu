package com.kaushal.setu.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import com.kaushal.setu.BuildConfig
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class GeminiRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    // Replace with your Gemini API key from aistudio.google.com
    private val apiKey = BuildConfig.GEMINI_API_KEY
    private val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

    suspend fun generateDescription(serviceType: String, years: Int): Result<String> =
        withContext(Dispatchers.IO) {
            if (apiKey == "YOUR_GEMINI_API_KEY") {
                return@withContext Result.success(mockDescription(serviceType, years))
            }
            try {
                val prompt = """
                    Write a 2-3 sentence professional service description for a skilled worker profile in India.
                    Worker: $serviceType with $years years experience.
                    Be specific about tasks, convey reliability. Plain text only, no markdown.
                """.trimIndent()
                val body = gson.toJson(mapOf(
                    "contents" to listOf(mapOf("parts" to listOf(mapOf("text" to prompt))))
                )).toRequestBody("application/json".toMediaType())
                val req = Request.Builder().url(url).post(body).build()
                val resp = client.newCall(req).execute()
                val text = resp.body?.string() ?: throw Exception("Empty response")
                @Suppress("UNCHECKED_CAST")
                val parsed = gson.fromJson(text, Map::class.java) as Map<String, Any>
                val candidates = parsed["candidates"] as? List<Map<String, Any>>
                val content = candidates?.firstOrNull()?.get("content") as? Map<String, Any>
                val parts = content?.get("parts") as? List<Map<String, Any>>
                val result = parts?.firstOrNull()?.get("text") as? String
                    ?: throw Exception("Parse error")
                Result.success(result.trim())
            } catch (e: Exception) {
                Result.success(mockDescription(serviceType, years))
            }
        }

    private fun mockDescription(serviceType: String, years: Int): String {
        val exp = when {
            years <= 1 -> "over a year of"
            years <= 5 -> "$years years of"
            else -> "$years+ years of extensive"
        }
        return when (serviceType.lowercase()) {
            "electrician", "electrical" ->
                "Skilled electrician with $exp experience in household wiring, fan installations, switch repairs, and electrical troubleshooting. " +
                "Specialises in safe, efficient electrical solutions for homes and small businesses. " +
                "Available for both emergency repairs and planned installations across the area."
            "plumber", "plumbing" ->
                "Professional plumber with $exp experience in pipe fitting, tap repairs, drainage solutions, and bathroom installations. " +
                "Expert in diagnosing and fixing leaks, blockages, and water pressure issues quickly. " +
                "Committed to clean, reliable plumbing work at transparent prices."
            "carpenter", "carpentry" ->
                "Experienced carpenter with $exp experience in furniture making, door and window repairs, and custom woodwork. " +
                "Skilled in both traditional joinery and modern modular furniture assembly. " +
                "Delivers quality craftsmanship for residential and commercial spaces."
            "painter", "painting" ->
                "Professional painter with $exp experience in interior and exterior wall painting, texture finishes, and waterproofing. " +
                "Expert in surface preparation, primer application, and quality finishing. " +
                "Provides neat, timely, and affordable painting services for homes and offices."
            "mechanic" ->
                "Skilled mechanic with $exp experience in two-wheeler and four-wheeler servicing, engine repairs, and general maintenance. " +
                "Proficient in diagnosing mechanical issues, brake servicing, and electrical repairs. " +
                "Provides reliable and cost-effective vehicle care at your doorstep."
            else ->
                "Experienced $serviceType professional with $exp hands-on experience delivering quality services. " +
                "Dedicated to providing reliable, timely, and professional work at competitive prices. " +
                "Available for both residential and commercial projects in the local area."
        }
    }
}