package it.project.services;
import it.project.entity.Exhibition;
import it.project.repositories.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionService {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Transactional
    public Exhibition addExhibition(Exhibition exhibition) {
        return exhibitionRepository.save(exhibition);
    }

    @Transactional(readOnly = true)
    public List<Exhibition> getAllExhibitions() {
        return exhibitionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Exhibition getExhibitionById(int id) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(id);
        return exhibition.orElse(null); // Restituisce null se non trova la mostra
    }

    @Transactional(readOnly = true)
    public List<Exhibition> searchExhibitionsByName(String name) {
        return exhibitionRepository.findByNameLike(name);
    }


    @Transactional
    public Exhibition updateExhibition(int id, Exhibition updatedExhibition) throws Exception {
        Optional<Exhibition> existingExhibition = exhibitionRepository.findById(id);
        if (existingExhibition.isPresent()) {
            Exhibition exhibition = existingExhibition.get();
            exhibition.setName(updatedExhibition.getName());
            exhibition.setDescription(updatedExhibition.getDescription());
            exhibition.setTickets(updatedExhibition.getTickets());
            return exhibitionRepository.save(exhibition);
        } else {
            throw new Exception("Exhibition not found");
        }
    }

    @Transactional
    public void deleteExhibition(int id) throws Exception {
        if (exhibitionRepository.existsById(id)) {
            exhibitionRepository.deleteById(id);
        } else {
            throw new Exception("Exhibition not found");
        }
    }
}