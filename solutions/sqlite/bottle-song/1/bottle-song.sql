-- Schema:
-- CREATE TABLE "bottle-song" (
--         start_bottles INTEGER NOT NULL,
--         take_down     INTEGER NOT NULL,
--         result        TEXT
-- );
-- Task: update bottle-song table and set the result based on the
-- start_bottles and take_down.
CREATE TABLE numbers (
    number INTEGER NOT NULL
);

INSERT INTO numbers (number) VALUES (0), (1), (2), (3), (4), (5), (6), (7), (8), (9), (10);

CREATE VIEW number_alias AS
SELECT number, 
    CASE number
        WHEN 0 THEN 'No green bottles'
        WHEN 1 THEN 'One green bottle'
        WHEN 2 THEN 'Two green bottles'
        WHEN 3 THEN 'Three green bottles'
        WHEN 4 THEN 'Four green bottles'
        WHEN 5 THEN 'Five green bottles'
        WHEN 6 THEN 'Six green bottles'
        WHEN 7 THEN 'Seven green bottles'
        WHEN 8 THEN 'Eight green bottles'
        WHEN 9 THEN 'Nine green bottles'
        WHEN 10 THEN 'Ten green bottles'
    END AS number_str
FROM numbers;

CREATE VIEW verses AS
SELECT
    start.number AS num_bottles,
    concat_ws(char(10),
        start.number_str || ' hanging on the wall,',
        start.number_str || ' hanging on the wall,',
        'And if one green bottle should accidentally fall,',
        'There''ll be ' || lower(endn.number_str) || ' hanging on the wall.') AS verse
FROM
    number_alias AS start 
    JOIN number_alias AS endn 
        ON start.number = endn.number+1
ORDER BY num_bottles DESC;

SELECT * FROM "bottle-song"
ORDER BY 
    start_bottles DESC, 
    take_down;

SELECT * FROM verses ORDER BY num_bottles DESC;

CREATE VIEW compound_verses AS
SELECT start_bottles,take_down,GROUP_CONCAT(verse, char(10) || char(10)) compound_verse
FROM "bottle-song" AS bs
    CROSS JOIN verses
WHERE 
    num_bottles BETWEEN bs.start_bottles - bs.take_down + 1 AND bs.start_bottles
GROUP BY start_bottles,take_down
ORDER BY 
    bs.start_bottles DESC, 
    bs.take_down;

UPDATE "bottle-song" AS bs
SET result = compound_verse
FROM 
    compound_verses
WHERE
    compound_verses.start_bottles = bs.start_bottles AND compound_verses.take_down = bs.take_down;

-- cleanup
DROP VIEW compound_verses;
DROP VIEW verses;
DROP VIEW number_alias;
DROP TABLE numbers;