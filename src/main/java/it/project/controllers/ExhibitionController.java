package it.project.controllers;

import it.project.entity.Exhibition;
import it.project.services.ExhibitionService;
import it.project.utils.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exhibitions")
@CrossOrigin(origins = "http://localhost:4200")
public class ExhibitionController {

    @Autowired
    private ExhibitionService exhibitionService;

    @PostMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createExhibition(@RequestBody Exhibition exhibition) {
        try {
            Exhibition createdExhibition = exhibitionService.addExhibition(exhibition);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExhibition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResultMessage("Error creating exhibition: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Exhibition>> getAllExhibitions() {
        List<Exhibition> exhibitions = exhibitionService.getAllExhibitions();
        return ResponseEntity.ok(exhibitions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExhibitionById(@PathVariable int id) {
        Exhibition exhibition = exhibitionService.getExhibitionById(id);
        if (exhibition != null) {
            return ResponseEntity.ok(exhibition);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultMessage("Exhibition not found"));
        }
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateExhibition(@PathVariable int id, @RequestBody Exhibition updatedExhibition) {
        try {
            Exhibition exhibition = exhibitionService.updateExhibition(id, updatedExhibition);
            return ResponseEntity.ok(exhibition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResultMessage("Error updating exhibition: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResultMessage> deleteExhibition(@PathVariable int id) {
        try {
            exhibitionService.deleteExhibition(id);
            return ResponseEntity.ok(new ResultMessage("Exhibition deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultMessage("Error deleting exhibition: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> findExhibitionsByName(@RequestParam String name) {
        List<Exhibition> exhibitions = exhibitionService.searchExhibitionsByName(name);
        if (exhibitions.isEmpty()) {
            return ResponseEntity.ok(new ResultMessage("No exhibitions found with the name: " + name));
        }
        return ResponseEntity.ok(exhibitions);
    }
}
