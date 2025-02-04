-- AlterTable
ALTER TABLE `tb_users` MODIFY `type_user` ENUM('user', 'therapist', 'admin') NOT NULL DEFAULT 'user';
