import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST("/api/v1/auth/authenticate")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/auth/register")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
}
data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

data class SignupRequest(val username: String, val email: String, val password: String)
data class SignupResponse(val message: String)
