CREATE TABLE IF NOT EXISTS invoices (
    id uuid DEFAULT random_uuid () PRIMARY KEY,
    pdf_url varchar(255),
    user_id varchar(255),
    amount int
);
