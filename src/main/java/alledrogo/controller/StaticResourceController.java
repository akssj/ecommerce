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
 * Api endpoint class, provides /static/{fileName} endpoint for frontend related files.
 */
@RestController
@RequestMapping("/static")
public class StaticResourceController {

    @GetMapping(value = "/{fileName}", produces = "text/javascript")
    public byte[] getScriptFile(@PathVariable String fileName) throws IOException {
        Resource resource = new ClassPathResource("static/" + fileName);
        return Files.readAllBytes(Path.of(resource.getURI()));
    }
    @GetMapping(value = "/component/{fileName}", produces = "text/html")
    public byte[] getNavbarHtml(@PathVariable String fileName) throws IOException {
        Resource resource = new ClassPathResource("templates/components/" + fileName);
        return Files.readAllBytes(Path.of(resource.getURI()));
    }

    @GetMapping(value = "/styles.css", produces = "text/css")
    public byte[] getStyleFile() throws IOException {
        Resource resource = new ClassPathResource("static/styles.css");
        return Files.readAllBytes(Path.of(resource.getURI()));
    }
}
