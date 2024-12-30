package org.but.feec.javafx.services;

import org.but.feec.javafx.api.FineBasicView;
import org.but.feec.javafx.api.FineCreateView;
import org.but.feec.javafx.api.FineEditView;
import org.but.feec.javafx.data.FineRepository;

import java.util.List;

/**
 * Class representing business logic on top of the Fines
 */
public class FineService {

    private FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public List<FineBasicView> getFineBasicView() {
        return fineRepository.getFinesBasicView();
    }

    public void createFine(FineCreateView fineCreateView) {

        fineRepository.createFine(fineCreateView);
    }

    public void editFine(FineEditView fineEditView) {
        fineRepository.editFine(fineEditView);
    }

    public void deleteFineById(Long id) {
        fineRepository.deleteFineById(id);
    }
}
