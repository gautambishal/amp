ALTER TABLE `dg_user` DROP COLUMN `LAST_VISIT`;
ALTER TABLE `dg_user` DROP COLUMN `SECOND_TO_LAST_VISIT`;
ALTER TABLE `dg_user` DROP COLUMN `NUMBER_OF_SESSIONS`;
ALTER TABLE `dg_user` DROP COLUMN `ORGANIZATION`;
DROP VIEW IF EXISTS v_funding_terms;