-- DropIndex
DROP INDEX `tb_breath_users_id_fkey` ON `tb_breath`;

-- DropIndex
DROP INDEX `tb_meditation_users_id_fkey` ON `tb_meditation`;

-- DropIndex
DROP INDEX `tb_phone_users_id_fkey` ON `tb_phone`;

-- DropIndex
DROP INDEX `tb_today_users_id_fkey` ON `tb_today`;

-- AlterTable
ALTER TABLE `tb_users` ADD COLUMN `last_login_date_user` DATETIME(3) NULL;

-- AddForeignKey
ALTER TABLE `tb_phone` ADD CONSTRAINT `tb_phone_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_today` ADD CONSTRAINT `tb_today_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_meditation` ADD CONSTRAINT `tb_meditation_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_breath` ADD CONSTRAINT `tb_breath_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;
