/*
  Warnings:

  - The values [Angry] on the enum `tb_meditation_emotion_meditation` will be removed. If these variants are still used in the database, this will fail.

*/
-- AlterTable
ALTER TABLE `tb_meditation` MODIFY `emotion_meditation` ENUM('Felicidade', 'Tristeza', 'Raiva', 'Desgosto', 'Medo') NOT NULL;
