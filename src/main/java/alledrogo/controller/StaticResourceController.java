package alledrogo.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Api endpoint class, provides /static/{fileName} endpoint for "text/javascript" and "text/css" files.
 */
@RestController
@RequestMapping("/static")
public class StaticResourceController {

    @GetMapping(value = "/{fileName}", produces = "text/javascript")
    public byte[] getScriptFile(@PathVariable String fileName) throws IOException {
        Resource resource = new ClassPathResource("static/" + fileName);
        return Files.readAllBytes(Path.of(resource.getURI()));
    }
    @GetMapping(value = "/navbar.html", produces = "text/html")
    public byte[] getNavbarHtml() throws IOException {
        Resource resource = new ClassPathResource("templates/components/navbar.html");
        return Files.readAllBytes(Path.of(resource.getURI()));
    }
    @GetMapping(value = "/styles.css", produces = "text/css")
    public byte[] getStyleFile() throws IOException {
        Resource resource = new ClassPathResource("static/styles.css");
        return Files.readAllBytes(Path.of(resource.getURI()));
    }
}
