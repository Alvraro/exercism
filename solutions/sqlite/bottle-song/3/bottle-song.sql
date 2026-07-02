-- Schema:
-- CREATE TABLE "bottle-song" (
--         start_bottles INTEGER NOT NULL,
--         take_down     INTEGER NOT NULL,
--         result        TEXT
-- );
-- Task: update bottle-song table and set the result based on the
-- start_bottles and take_down.
UPDATE "bottle-song" SET result = (   
    WITH 
        number_alias(number, number_str) AS (
            VALUES
                (0, 'No green bottles'),
                (1, 'One green bottle'),
                (2, 'Two green bottles'),
                (3, 'Three green bottles'),
                (4, 'Four green bottles'),
                (5, 'Five green bottles'),
                (6, 'Six green bottles'),
                (7, 'Seven green bottles'),
                (8, 'Eight green bottles'),
                (9, 'Nine green bottles'),
                (10, 'Ten green bottles')
        ),
    
        verses(num_bottles, verse) AS (
            SELECT
                start.number AS num_bottles,
                concat_ws(char(10),
                    start.number_str || ' hanging on the wall,',
                    start.number_str || ' hanging on the wall,',
                    'And if one green bottle should accidentally fall,',
                    'There''ll be ' || lower(endn.number_str) || ' hanging on the wall.')
            FROM
                number_alias AS start 
                JOIN number_alias AS endn 
                    ON start.number = endn.number+1
            ORDER BY
                start.number DESC
        )
    
    SELECT GROUP_CONCAT(verse, char(10) || char(10))
    FROM verses
    WHERE 
        num_bottles BETWEEN start_bottles - take_down + 1 AND start_bottles
    ORDER BY 
        num_bottles DESC
);