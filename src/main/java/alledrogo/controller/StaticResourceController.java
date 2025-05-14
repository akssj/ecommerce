package alledrogo.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * Api endpoint class, provides /static/{fileName} endpoint for frontend related files.
 */
@RestController
@RequestMapping("static")
public class StaticResourceController {
    @GetMapping(value = {"/{fileName}", "/scripts/{fileName}"}, produces = "text/javascript")
    public ResponseEntity<byte[]> getScriptFile(@PathVariable String fileName) throws IOException {
        String filePath = "static/" + (fileName.equals("module.js") ? "" : "scripts/") + fileName;
        Resource resource = new ClassPathResource(filePath);
        byte[] fileContent = Files.readAllBytes(Path.of(resource.getURI()));

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().getHeaderValue());
        headers.setExpires(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    @GetMapping(value = "/component/{fileName}", produces = "text/html")
    public ResponseEntity<byte[]> getNavbarHtml(@PathVariable String fileName) throws IOException {
        Resource resource = new ClassPathResource("templates/components/" + fileName);
        byte[] fileContent = Files.readAllBytes(Path.of(resource.getURI()));

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().getHeaderValue());
        headers.setExpires(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    @GetMapping(value = "/styles.css", produces = "text/css")
    public ResponseEntity<byte[]> getStyleFile() throws IOException {
        Resource resource = new ClassPathResource("static/styles.css");
        byte[] fileContent = Files.readAllBytes(Path.of(resource.getURI()));

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().getHeaderValue());
        headers.setExpires(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }
    @GetMapping(value = "/static/favicon.ico", produces = "image/vnd.microsoft.icon")
    public ResponseEntity<byte[]> getFavicon() throws IOException {
        String filePath = "static/favicon.ico";
        Resource resource = new ClassPathResource(filePath);
        byte[] fileContent = Files.readAllBytes(Path.of(resource.getURI()));

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic().getHeaderValue());
        headers.setExpires(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }
}
