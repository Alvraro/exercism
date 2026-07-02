-- Schema: CREATE TABLE "gigasecond" ("moment" TEXT, "result" TEXT);
-- Task: update the gigasecond table and set the result based on the moment.
UPDATE gigasecond
SET result=strftime('%Y-%m-%dT%H:%M:%S', moment, '+1e9 second');