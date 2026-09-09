package br.com.fiap.reservas.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class SwaggerAutoOpen {

    private final Environment environment;

    public SwaggerAutoOpen(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {
        if (!Boolean.parseBoolean(environment.getProperty("app.swagger.auto-open", "true"))) {
            return;
        }

        String port = environment.getProperty("local.server.port");
        if (port == null) {
            port = environment.getProperty("server.port", "8080");
        }

        String url = "http://localhost:" + port + "/swagger-ui/index.html";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
            }
        } catch (Exception ignored) {
            // A aplicação continua normalmente mesmo quando o ambiente não possui navegador gráfico.
        }
    }
}
