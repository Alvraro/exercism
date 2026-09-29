-- Schema:
-- CREATE TABLE "dnd-character" (
--   property     TEXT    NOT NULL,
--   input        TEXT    NOT NULL,
--   strength     INTEGER         ,
--   dexterity    INTEGER         ,
--   constitution INTEGER         ,
--   intelligence INTEGER         ,
--   wisdom       INTEGER         ,
--   charisma     INTEGER         ,
--   modifier     INTEGER         ,
--   hitpoints    INTEGER
-- );
--
-- Task: update the dnd-character table and set the appropriate columns based on the property and the input.

-- Modifiers
UPDATE "dnd-character"
SET modifier = floor(0.5 * (constitution - 10))
WHERE 
    property='modifier' and input='score';

-- Abilities
WITH 
    strength_roll(sum) AS (
        SELECT SUM(roll) FROM (
            SELECT 1 + abs(random()) % 6 AS roll
            FROM generate_series(1,4)
            ORDER BY roll DESC
            LIMIT 3
        ) AS rolls
    ),
    dexterity_roll(sum) AS (
        SELECT SUM(roll) FROM (
            SELECT 1 + abs(random()) % 6 AS roll
            FROM generate_series(1,4)
            ORDER BY roll DESC
            LIMIT 3
        ) AS rolls
    ),
    constitution_roll(sum) AS (
        SELECT SUM(roll) FROM (
            SELECT 1 + abs(random()) % 6 AS roll
            FROM generate_series(1,4)
            ORDER BY roll DESC
            LIMIT 3
        ) AS rolls
    ),
    intelligence_roll(sum) AS (
        SELECT SUM(roll) FROM (
            SELECT 1 + abs(random()) % 6 AS roll
            FROM generate_series(1,4)
            ORDER BY roll DESC
            LIMIT 3
        ) AS rolls
    ),
    wisdom_roll(sum) AS (
        SELECT SUM(roll) FROM (
            SELECT 1 + abs(random()) % 6 AS roll
            FROM generate_series(1,4)
            ORDER BY roll DESC
            LIMIT 3
        ) AS rolls
    ),
    charisma_roll(sum) AS (
        SELECT SUM(roll) FROM (
            SELECT 1 + abs(random()) % 6 AS roll
            FROM generate_series(1,4)
            ORDER BY roll DESC
            LIMIT 3
        ) AS rolls
    )
UPDATE 
    "dnd-character"
SET
    strength = strength_roll.sum,
    dexterity = dexterity_roll.sum,
    constitution = constitution_roll.sum,
    intelligence = intelligence_roll.sum,
    wisdom = wisdom_roll.sum,
    charisma = charisma_roll.sum
FROM strength_roll, dexterity_roll, constitution_roll, intelligence_roll, wisdom_roll, charisma_roll
WHERE
    input != 'score';

-- Hitpoints
WITH
    constitution_modifier(value,modifier) AS (
        SELECT constitution,modifier
        FROM "dnd-character"
        WHERE property='modifier' and input='score'
    )
UPDATE "dnd-character"
SET
    modifier = constitution_modifier.modifier,
    hitpoints = 10 + constitution_modifier.modifier
FROM constitution_modifier
WHERE
    property = 'character' AND input='random'
    AND constitution_modifier.value = constitution;