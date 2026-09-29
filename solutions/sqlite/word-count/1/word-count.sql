-- Schema:
-- CREATE TABLE "word-count" (
--     sentence TEXT NOT NULL,
--     result   TEXT               -- json object
-- );
--
-- Task: update the word-count table and set the result based on sentence.
--       * the result column should contain JSON-encoded object of objects,
--         each word as key and count as integer.
WITH
    words(sentence, rest, buffer, word) AS (
        SELECT sentence, lower(sentence) || ' ', '', ''
        FROM "word-count"
        UNION ALL
        SELECT
            sentence, 
            substr(rest,2),
            IF(substr(rest,1,1) GLOB '[a-z0-9'']*', buffer || substr(rest,1,1), ''), 
            IF(substr(rest,1,1) GLOB '[a-z0-9'']*', '', trim(buffer,''''))
        FROM words
        WHERE rest != ''
    ),
    
    word_counts(sentence,word,c) AS (
        SELECT sentence,word,count(*)
        FROM words
        WHERE word != ''
        GROUP BY sentence,word
    ),
    
    word_count_json(sentence,json) AS (
        SELECT sentence,json_group_object(word,c)
        FROM word_counts
        GROUP by sentence
    )

UPDATE "word-count" AS wc
SET result=json
FROM word_count_json AS wcj
WHERE wc.sentence = wcj.sentence;