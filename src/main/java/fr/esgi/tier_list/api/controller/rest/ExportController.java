package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.application.export.ExportSyntheseTierListsService;
import fr.esgi.tier_list.application.storage.StockageService;
import fr.esgi.tier_list.application.view.ExportSyntheseTierListsPdfView;
import fr.esgi.tier_list.domain.port.ExportSyntheseTierListsUseCase.CompanySynthesis;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class ExportController {
    private final StockageService stockageService;
    private final ExportSyntheseTierListsService exportSyntheseTierListsService;

    public ExportController(StockageService stockageService, ExportSyntheseTierListsService exportSyntheseTierListsService) {
        this.stockageService = stockageService;
        this.exportSyntheseTierListsService = exportSyntheseTierListsService;
    }

    @GetMapping("/exportSyntheseTierListsPdf")
    public ModelAndView exportSyntheseTierListsPdf() {
        ModelAndView mav = new ModelAndView(new ExportSyntheseTierListsPdfView(stockageService));

        Map<String, CompanySynthesis> companySynthesis = exportSyntheseTierListsService.generateSynthesis();

        mav.addObject("companySynthesis", companySynthesis);

        return mav;
    }
}
