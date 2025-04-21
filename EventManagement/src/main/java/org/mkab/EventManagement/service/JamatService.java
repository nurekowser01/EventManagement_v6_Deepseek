package org.mkab.EventManagement.service;



import org.apache.coyote.BadRequestException;
import org.mkab.EventManagement.entity.*;
import org.mkab.EventManagement.repository.JamatRepository;
import org.mkab.EventManagement.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JamatService {

    @Autowired
    private JamatRepository jamatRepository;

    public List<Jamat> getAllJamat() {
        return jamatRepository.findAll();
    }

    public Jamat getJamatById(Long id) {
        return jamatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jamat not found with id: " + id));
    }

    public Jamat createJamat(Jamat jamat) throws BadRequestException {
        if (jamatRepository.existsByName(jamat.getName())) {
            throw new BadRequestException("Jamat with this name already exists");
        }
        return jamatRepository.save(jamat);
    }

    public Jamat updateJamat(Long id, Jamat jamatDetails) {
        Jamat jamat = getJamatById(id);
        jamat.setName(jamatDetails.getName());
        jamat.setNextVal(jamatDetails.getNextVal());
        return jamatRepository.save(jamat);
    }

    public void deleteJamat(Long id) {
        Jamat jamat = getJamatById(id);
        jamatRepository.delete(jamat);
    }
}