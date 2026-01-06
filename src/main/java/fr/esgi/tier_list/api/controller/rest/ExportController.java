package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.application.storage.StockageService;
import fr.esgi.tier_list.application.view.ExportSyntheseTierListsPdfView;
import fr.esgi.tier_list.domain.Company;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExportController {
    private final StockageService stockageService;

    public ExportController(StockageService stockageService) {
        this.stockageService = stockageService;
    }

    @GetMapping("/exportSyntheseTierListsPdf")
    public ModelAndView exportSyntheseTierListsPdf() {
        ModelAndView mav = new ModelAndView(new ExportSyntheseTierListsPdfView(stockageService));

        Map<String, List<Company>> tierList = new LinkedHashMap<>();
        tierList.put("S", List.of(new Company("Discord", "https://discord.com")));
        tierList.put("A", List.of());
        tierList.put("B", List.of());
        tierList.put("C", List.of());
        tierList.put("D", List.of());

        mav.addObject("tierList", tierList);

        return mav;
    }
}
