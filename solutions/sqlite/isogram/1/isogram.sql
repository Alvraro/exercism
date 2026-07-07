-- Schema: CREATE TABLE "isogram" ("phrase" TEXT, "is_isogram" INT);
-- Task: Given a phrase, determine if it is an isogram.
WITH
    letters(phrase, i, letter) AS (
        SELECT phrase, iterator.value, lower(substr(phrase,iterator.value,1))
        FROM isogram
            JOIN generate_series(1,length(phrase),1) AS iterator
    ),

    letter_num_occurrences(phrase, letter, num_occurrences) AS (
        SELECT phrase, letter, count(*)
        FROM
            letters
        WHERE
            letter != ' ' AND letter != '-'
        GROUP BY
            phrase, letter
    ),

    non_isograms(phrase) AS (
        SELECT
            phrase
        FROM
            letter_num_occurrences
        GROUP BY
            phrase
        HAVING
            MAX(num_occurrences) > 1
    )

UPDATE
    isogram
SET
    is_isogram = NOT EXISTS 
        (SELECT 1 
         FROM non_isograms 
         WHERE non_isograms.phrase = isogram.phrase);