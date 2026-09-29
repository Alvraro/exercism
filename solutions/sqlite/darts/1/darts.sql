-- Schema: CREATE TABLE "darts" ("x" REAL, "y" REAL, score INTEGER);
-- Task: update the darts table and set the score based on the x and y values.

UPDATE darts
SET score = 
CASE
    WHEN pow(x,2) + pow(y,2) <= 1 THEN 10
    WHEN pow(x,2) + pow(y,2) <= 25 THEN 5
    WHEN pow(x,2) + pow(y,2) <= 100 THEN 1
    ELSE 0
END;