CREATE OR REPLACE VIEW `v_description` AS
  select
    distinct `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    trim(`dg_editor`.`BODY`) AS ebody
  from
    (`amp_activity` join `dg_editor`)
  where
    (`amp_activity`.`description` = `dg_editor`.`EDITOR_KEY`)
  order by
    `amp_activity`.`amp_activity_id`;


CREATE OR REPLACE  VIEW `v_objectives` AS
  select
    `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    trim(`dg_editor`.`BODY`) AS ebody
  from
    (`amp_activity` join `dg_editor`)
  where
    (`amp_activity`.`objectives` = `dg_editor`.`EDITOR_KEY`)
  order by
    `amp_activity`.`amp_activity_id`;


CREATE OR REPLACE  VIEW `v_purposes` AS
  select
    `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    trim(`dg_editor`.`BODY`) AS ebody
  from
    (`amp_activity` join `dg_editor`)
  where
    (`amp_activity`.`purpose` = `dg_editor`.`EDITOR_KEY`)
  order by
    `amp_activity`.`amp_activity_id`;



CREATE OR REPLACE  VIEW `v_results` AS
  select
    `amp_activity`.`amp_activity_id` AS `amp_activity_id`,
    trim(`dg_editor`.`BODY`) AS ebody
  from
    (`amp_activity` join `dg_editor`)
  where
    (`amp_activity`.`results` = `dg_editor`.`EDITOR_KEY`)
  order by
    `amp_activity`.`amp_activity_id`;