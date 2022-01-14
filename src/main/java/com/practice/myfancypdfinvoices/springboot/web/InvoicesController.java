package com.practice.myfancypdfinvoices.springboot.web;

import java.util.List;

import javax.validation.Valid;

import com.practice.myfancypdfinvoices.springboot.dto.InvoiceDto;
import com.practice.myfancypdfinvoices.springboot.model.Invoice;
import com.practice.myfancypdfinvoices.springboot.service.InvoiceService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoicesController {
    
    private final InvoiceService invoiceService;

    public InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public List<Invoice> findAll() {
        return invoiceService.findAll();
    }

    @PostMapping("/invoices")
    // public Invoice createInvoice(@RequestParam("user_id") String userId, @RequestParam Integer amount) {
    public Invoice createInvoice(@Valid @RequestBody InvoiceDto invoiceDto) {
        return invoiceService.create(invoiceDto.getUserId(), invoiceDto.getAmount());
    }
}
