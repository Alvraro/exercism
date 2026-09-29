WITH
    lines(id,num_game,line,rest) AS (
        SELECT rowid,0,'',input
        FROM tournament
        UNION ALL
        SELECT id,num_game+1,
                IIF(instr(rest,CHAR(10))>0, substr(rest,1,instr(rest,CHAR(10))-1), rest),
                IIF(instr(rest,CHAR(10))>0, substr(rest,instr(rest,CHAR(10))+1), '')            
        FROM lines
        WHERE rest!=''
    ),

    aux1(id,num_game,team1,rest) AS (
        SELECT id,num_game,substr(line,1,delim-1),substr(line,delim+1)
        FROM (SELECT id,num_game,line,instr(line,';') delim FROM lines WHERE line!='')
    ),

    aux2(id,num_game,team2,result) AS (
        SELECT id,num_game,substr(rest,1,delim-1),substr(rest,delim+1)
        FROM (SELECT id,num_game,rest,instr(rest,';') delim FROM aux1)
    ),

    games_multiteam(id,num_game,team1,team2,result) AS (
        SELECT id,num_game,team1,team2,result
        FROM aux1 JOIN aux2 USING(id,num_game)
    ),

    games_singleteam(id,num_game,team,result) AS (
        SELECT id,num_game,team1,result
        FROM games_multiteam
        UNION ALL
        SELECT id,num_game,team2,
            CASE result
                WHEN 'win' THEN 'loss'
                WHEN 'loss' THEN 'win'
                ELSE 'draw'
            END
        FROM games_multiteam
    ),

    wins(id,team,num_wins) AS (
        SELECT id,team,count(*)
        FROM games_singleteam
        WHERE result = 'win'
        GROUP BY id,team
    ),

    draws(id,team,num_draws) AS (
        SELECT id,team,count(*)
        FROM games_singleteam
        WHERE result = 'draw'
        GROUP BY id,team
    ),

    losses(id,team,num_losses) AS (
        SELECT id,team,count(*)
        FROM games_singleteam
        WHERE result = 'loss'
        GROUP BY id,team
    ),

    tournament_table(id,team,MP,W,D,L,P) AS (
        SELECT id,team,
            ifnull(num_wins,0)+ifnull(num_draws,0)+ifnull(num_losses,0) AS MP,
            ifnull(num_wins,0) AS W,
            ifnull(num_draws,0) AS D,
            ifnull(num_losses,0) AS L,
            3*ifnull(num_wins,0)+ifnull(num_draws,0) AS P
        FROM
            wins 
            FULL JOIN losses USING(id,team)
            FULL JOIN draws USING(id,team)
        ORDER BY id asc,P desc,team asc
    )

UPDATE tournament
SET result=t.result
FROM (
    SELECT id,
        'Team                           | MP |  W |  D |  L |  P' || CHAR(10) ||
        GROUP_CONCAT(printf('%-30s | %2d | %2d | %2d | %2d | %2d', team, MP, W, D, L, P), CHAR(10)) AS result
    FROM tournament_table
    GROUP BY id
) AS t
WHERE rowid=t.id;

UPDATE tournament
SET result='Team                           | MP |  W |  D |  L |  P'
WHERE input='';