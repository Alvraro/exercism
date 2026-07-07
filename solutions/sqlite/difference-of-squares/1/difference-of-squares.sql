-- Schema: CREATE TABLE "difference-of-squares" ("number" INT, "property" TEXT, "result" INT);
-- Task: update the difference-of-squares table and set the result based on the number and property fields.
WITH
    RECURSIVE aux(number,sum,squares_sum) AS (
        VALUES(0,0,0) UNION ALL SELECT number+1, number+1+sum, pow(number+1,2)+squares_sum FROM aux
    ),
    results(number,squared_sum, squares_sum) AS (
        SELECT number, pow(sum,2) AS squared_sum, squares_sum
        FROM aux
    )
UPDATE "difference-of-squares" AS ds SET result = (
    SELECT
        CASE property
            WHEN 'squareOfSum' THEN squared_sum
            WHEN 'sumOfSquares' THEN squares_sum
            WHEN 'differenceOfSquares' THEN abs(squared_sum - squares_sum)
        END
    FROM results
    WHERE ds.number = results.number
);