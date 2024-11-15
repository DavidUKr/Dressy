import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val message: String)

interface AuthService {
    @POST("/api/auth/login") // pui tu cum e
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/api/auth/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
}

data class SignupRequest(val username: String, val email: String, val password: String)
data class SignupResponse(val message: String) // modifici daca e nevoie