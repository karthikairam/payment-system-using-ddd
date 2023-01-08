DROP SCHEMA IF EXISTS "receipt" CASCADE;
CREATE SCHEMA "receipt";

DROP TYPE IF EXISTS receipt_status;
CREATE TYPE receipt_status AS ENUM ('PENDING', 'COMPLETED');

DROP TABLE IF EXISTS "receipt"."receipts" CASCADE;
CREATE TABLE "receipt"."receipts" (
    "id" UUID PRIMARY KEY NOT NULL,
    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    "transaction_ts" TIMESTAMP WITH TIME ZONE NOT NULL,
    "paid_by" CHARACTER VARYING NOT NULL,
    "reference_number" CHARACTER VARYING NOT NULL,
    "card_number" CHARACTER VARYING NOT NULL,
    "card_type" CHARACTER VARYING NOT NULL,
    "student_id" CHARACTER VARYING NOT NULL,
    "student_name" CHARACTER VARYING NOT NULL,
    "student_grade" CHARACTER VARYING NOT NULL,
    "status" receipt_status NOT NULL
);

DROP TABLE IF EXISTS "receipt"."receipt_purchase_items" CASCADE;
CREATE TABLE "receipt"."purchase_items" (
    "id" uuid PRIMARY KEY NOT NULL,
    "receipt_id" uuid NOT NULL,
    "fee_type" CHARACTER VARYING NOT NULL,
    "item_name" CHARACTER VARYING NOT NULL,
    "quantity" INTEGER NOT NULL,
    "item_price" NUMERIC(10,2) NOT NULL
);
ALTER TABLE "receipt"."receipt_purchase_items"
    ADD CONSTRAINT "fk_receipt_purchase_items_receipt_id" FOREIGN KEY ("receipt_id")
    REFERENCES "receipt"."receipts" (id);

