import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DownloadController {

    private static final String STORAGE_FOLDER = "/path/to/storage/folder";

    @GetMapping("/download-endpoint/{fileName}")
    public ResponseEntity<byte[]> handleFileDownload(@PathVariable("fileName") String fileName) {
        // Construir la ruta completa del archivo en el servidor de almacenamiento
        Path filePath = Paths.get(STORAGE_FOLDER + fileName);

        try {
            // Leer el contenido del archivo en bytes
            byte[] fileContent = Files.readAllBytes(filePath);

            // Configurar las cabeceras de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(fileContent.length);

            // Devolver una respuesta con el contenido del archivo
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Manejar la excepción de manera adecuada para tu aplicación
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
