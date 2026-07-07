-- Schema: CREATE TABLE "darts" ("x" REAL, "y" REAL, score INTEGER);
-- Task: update the darts table and set the score based on the x and y values.
UPDATE darts
SET score = (
    SELECT
        CASE
            WHEN distance <= 1 THEN 10
            WHEN distance <= 25 THEN 5
            WHEN distance <= 100 THEN 1
            ELSE 0
        END
    FROM (SELECT pow(x,2) + pow(y,2) distance)
);