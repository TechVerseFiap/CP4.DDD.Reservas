package br.com.fiap.reservas.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BrowserStartupConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {

        openUrl("http://localhost:8080/swagger-ui.html");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        openUrl("http://localhost:8080/h2-console");
    }

    private void openUrl(String url) {
        try {
            new ProcessBuilder("cmd", "/c", "start", "", url)
                    .start();

            System.out.println("Abrindo navegador: " + url);

        } catch (Exception e) {
            System.err.println("Não foi possível abrir: " + url);
            e.printStackTrace();
        }
    }
}