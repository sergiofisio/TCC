/*
  Warnings:

  - Added the required column `morning_afternoon_evening` to the `tb_today` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `tb_today` ADD COLUMN `morning_afternoon_evening` ENUM('morning', 'afternoon', 'evening') NOT NULL;
