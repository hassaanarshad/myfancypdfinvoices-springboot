package com.practice.myfancypdfinvoices.springboot.service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.practice.myfancypdfinvoices.springboot.model.Invoice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class InvoiceService {

    private List<Invoice> invoices = new CopyOnWriteArrayList<>();

    private final UserService userService;

    private final String cdnUrl;

    private final JdbcTemplate jdbcTemplate;

    public InvoiceService(UserService userService, @Value("${cdn.url}") String cdnUrl, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.cdnUrl = cdnUrl;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("Fetching PDF Template from S3...");
        // TODO download from s3 and save locally
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Deleting downloaded templates...");
        // TODO actual deletion of PDFs
    }


    @Transactional
    public List<Invoice> findAll() {
        // return invoices;

        System.out.println("Is a database transaction open? = " + TransactionSynchronizationManager.isActualTransactionActive());
        return jdbcTemplate.query("select id, user_id, pdf_url, amount from invoices", (resultSet, rowNum) -> {
            Invoice invoice = new Invoice();
            invoice.setId(resultSet.getObject("id").toString());
            invoice.setUserId(resultSet.getString("user_id"));
            invoice.setPdfUrl(resultSet.getString("pdf_url"));
            invoice.setAmount(resultSet.getInt("amount"));

            return invoice;
        });
    }

    @Transactional
    public Invoice create(String userId, Integer amount) {
        // User user = userService.findById(userId);
        // if (user == null) {
        //     throw new IllegalStateException();
        // }

        // Invoice invoice = new Invoice(userId, cdnUrl + "/images/default/sample.pdf", amount);
        // invoices.add(invoice);

        // return invoice;
        System.out.println("Is a database transaction open? = " + TransactionSynchronizationManager.isActualTransactionActive());
        String generatedPdfUrl = cdnUrl + "/images/default/sample.pdf";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into invoices (user_id, pdf_url, amount) values (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, generatedPdfUrl);
            preparedStatement.setInt(3, amount);

            return preparedStatement;
        }, keyHolder);

        String uuid = !keyHolder.getKeys().isEmpty() ? ((UUID) keyHolder.getKeys().values().iterator().next()).toString()
        : null;

        Invoice invoice = new Invoice();
        invoice.setId(uuid);
        invoice.setPdfUrl(generatedPdfUrl);
        invoice.setAmount(amount);
        invoice.setUserId(userId);
        
        return invoice;
    }
}
