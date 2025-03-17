/*
  Warnings:

  - The values [good,bad] on the enum `tb_meditation_type_situation_meditation` will be removed. If these variants are still used in the database, this will fail.
  - You are about to alter the column `type_phone` on the `tb_phone` table. The data in that column could be lost. The data in that column will be cast from `Enum(EnumId(1))` to `Enum(EnumId(1))`.
  - The values [therapist] on the enum `tb_users_type_user` will be removed. If these variants are still used in the database, this will fail.

*/
-- AlterTable
ALTER TABLE `tb_meditation` MODIFY `type_situation_meditation` ENUM('boa', 'ruim') NOT NULL;

-- AlterTable
ALTER TABLE `tb_phone` MODIFY `type_phone` ENUM('casa', 'trabalho', 'celular', 'emergencia') NOT NULL DEFAULT 'celular';

-- AlterTable
ALTER TABLE `tb_users` MODIFY `type_user` ENUM('user', 'admin') NOT NULL DEFAULT 'user';
