DROP SCHEMA IF EXISTS "payment" CASCADE;
CREATE SCHEMA "payment";

DROP TYPE IF EXISTS payment_transaction_status;
CREATE TYPE payment_transaction_status AS ENUM ('INITIATED', 'COMPLETED', 'FAILED');

DROP TABLE IF EXISTS "payment"."payment_transactions" CASCADE;

CREATE TABLE "payment"."payment_transactions" (
    "id" UUID PRIMARY KEY NOT NULL,
    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    "student_id" CHARACTER VARYING NOT NULL,
    "paid_by" CHARACTER VARYING NOT NULL,
    "reference_number" CHARACTER VARYING NOT NULL,
    "card_number" CHARACTER VARYING NOT NULL,
    "card_type" CHARACTER VARYING NOT NULL,
    "total_fee_paid" NUMERIC(10,2) NOT NULL,
    "status" payment_transaction_status NOT NULL
);

DROP TABLE IF EXISTS "payment"."idempotency_keys" CASCADE;
CREATE TABLE "payment"."idempotency_keys" (
    "idempotency_key" CHARACTER VARYING PRIMARY KEY NOT NULL,
    "payment_transaction_id" uuid NOT NULL
);

ALTER TABLE "payment"."idempotency_keys"
    ADD CONSTRAINT "fk_idempotency_keys_payment_transaction_id" FOREIGN KEY ("payment_transaction_id")
    REFERENCES "payment"."payment_transactions" (id);

DROP TABLE IF EXISTS "payment"."purchase_items" CASCADE;
CREATE TABLE "payment"."purchase_items" (
    "id" uuid PRIMARY KEY NOT NULL,
    "payment_transaction_id" uuid NOT NULL,
    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
    "fee_type" CHARACTER VARYING NOT NULL,
    "item_name" CHARACTER VARYING NOT NULL,
    "quantity" INTEGER NOT NULL,
    "item_price" NUMERIC(10,2) NOT NULL
);
ALTER TABLE "payment"."purchase_items"
    ADD CONSTRAINT "fk_purchase_items_payment_transaction_id" FOREIGN KEY ("payment_transaction_id")
    REFERENCES "payment"."payment_transactions" (id);

