delete from amp_team_member where amp_team_id not in (select amp_team_id from amp_team);