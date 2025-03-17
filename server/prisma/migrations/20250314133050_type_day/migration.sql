/*
  Warnings:

  - The values [morning,afternoon,evening] on the enum `tb_today_morning_afternoon_evening` will be removed. If these variants are still used in the database, this will fail.

*/
-- AlterTable
ALTER TABLE `tb_today` MODIFY `morning_afternoon_evening` ENUM('manhã', 'tarde', 'noite') NOT NULL;
