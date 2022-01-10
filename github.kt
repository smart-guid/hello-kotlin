import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(){
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://rbc.com"))
        .build();
        
    val response = client.send(request, HttpResponse.BodyHandlers.ofString());
    println(response.body())
}