package com.swapmystuffbackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID


data class ProductData(
    val name: String,
    val description: String? = null
)

@RestController
@RequestMapping("/api")
class FileController {
    companion object {
        private val UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads").toString() + "/"
    }

    @PostMapping("/upload")
    fun uploadFile(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("data") data: ProductData
    ): ResponseEntity<String> {
        return try {
            if (file.isEmpty) {
                return ResponseEntity.badRequest().body("File is empty")
            }
            val originalFilename = file.originalFilename ?: return ResponseEntity.badRequest().body("File has no name")
            val fileName = "${UUID.randomUUID()}_$originalFilename"
            val uploadPath = Paths.get(UPLOAD_DIR)
            Files.createDirectories(uploadPath)
            val filePath = uploadPath.resolve(fileName)
            Files.write(filePath, file.bytes)
            val fileUrl = "/uploads/$fileName"
            println("Uploaded file: $fileName with data: $data") // For debugging
            ResponseEntity.ok(fileUrl)
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Upload failed: ${e.message}")
        }
    }
}