-- Best practice, in Postgres, is to not use limit char in VARCHAR type if really not sure of data.
-- Because space occupancy & performance is not going to differ with limit or without for VARCHAR

ALTER TABLE "student"."students" ALTER COLUMN "student_id" CHARACTER VARYING PRIMARY KEY NOT NULL;
ALTER TABLE "student"."students" ALTER COLUMN "student_name" CHARACTER VARYING NOT NULL;
ALTER TABLE "student"."students" ALTER COLUMN "grade" CHARACTER VARYING NOT NULL;
ALTER TABLE "student"."students" ALTER COLUMN "mobile_number" CHARACTER VARYING;
ALTER TABLE "student"."students" ALTER COLUMN "school_name" CHARACTER VARYING NOT NULL;