package org.excelreader.controllers;

import lombok.RequiredArgsConstructor;
import org.excelreader.services.KthMinFindService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KthMinFindController {

    private final KthMinFindService kthMinFindService;

    @GetMapping("/KthMin")
    public double findKthMin(@RequestParam(name = "N") int n, @RequestParam(name = "path") String path) throws IOException {
        return kthMinFindService.kthMin(n,path);
    }
}
