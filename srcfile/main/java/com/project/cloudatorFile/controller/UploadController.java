import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadController {

    private static final String STORAGE_FOLDER = "/path/to/storage/folder";

    @PostMapping("/upload-endpoint")
    public ResponseEntity<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Archivo vacío", HttpStatus.BAD_REQUEST);
        }

        try {
            // Lee el archivo
            byte[] bytes = file.getBytes();

            // Crea el path del archivo en el servidor de almacenamiento
            Path storagePath = Paths.get(STORAGE_FOLDER + file.getOriginalFilename());

            // Escribe el archivo en el servidor de almacenamiento
            Files.write(storagePath, bytes);

            return new ResponseEntity<>("Archivo recibido correctamente", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // Maneja la excepción de manera adecuada para tu aplicación
            return new ResponseEntity<>("Error al procesar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}