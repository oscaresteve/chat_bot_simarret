package edu.alumno.oscaresteve.chat_bot_simarret;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
  private String url;
  private String endpoint;
  private int statusCode;
  private String message;

  public ExternalApiException(String url, String endpoint, int statusCode, String message) {
    super(message);
    this.url = url;
    this.endpoint = endpoint;
    this.statusCode = statusCode;
    this.message = message;
  }

  public static ExternalApiException handleException(Throwable ex, String endpoint) {
    // Lógica de manejo de errores (similar a la original)
    String errorMsg = "Error llamando al endpoint " + endpoint + ": " + ex.getMessage();
    return new ExternalApiException(
        "external.api.url",
        endpoint,
        500, // Código genérico (ajustar según excepción)
        errorMsg);
  }
}
