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
UPDATE "dnd-character"
SET modifier = floor(0.5 * (constitution - 10))
WHERE input='score';

WITH 
    dice1(n,throw) AS (SELECT 1, 1 + abs(random()) % 6 AS throw),
    dice2(n,throw) AS (SELECT 2, 1 + abs(random()) % 6 AS throw),
    dice3(n,throw) AS (SELECT 3, 1 + abs(random()) % 6 AS throw),
    dice4(n,throw) AS (SELECT 4, 1 + abs(random()) % 6 AS throw),
    discarded_throw(throw) AS (
        SELECT min(dice1.throw, dice2.throw, dice3.throw, dice4.throw)
        FROM dice1,dice2,dice3,dice4
    ),
    discarded_dice(n) AS (
        SELECT MIN(n) 
        FROM
            (SELECT n,throw FROM dice1
            UNION 
            SELECT n,throw FROM dice2
            UNION 
            SELECT n,throw FROM dice3
            UNION 
            SELECT n,throw FROM dice4)
        WHERE throw = (SELECT throw FROM discarded_throw)
    ),
    sum_dice(sum) AS (
        SELECT SUM(throw)
        FROM
            (SELECT n,throw FROM dice1
            UNION 
            SELECT n,throw FROM dice2
            UNION 
            SELECT n,throw FROM dice3
            UNION 
            SELECT n,throw FROM dice4)
        WHERE n != (SELECT n FROM discarded_dice)
    )
UPDATE 
    "dnd-character"
SET
    strength = sum_dice.sum,
    dexterity = sum_dice.sum,
    constitution = sum_dice.sum,
    intelligence = sum_dice.sum,
    wisdom = sum_dice.sum,
    charisma = sum_dice.sum,
    modifier = floor(0.5 * (sum_dice.sum - 10)),
    hitpoints = 10 + floor(0.5 * (sum_dice.sum - 10))
FROM
    sum_dice
WHERE
    property != 'modifier';

SELECT *
FROM "dnd-character";
    