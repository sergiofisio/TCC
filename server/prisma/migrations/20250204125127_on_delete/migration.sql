-- DropForeignKey
ALTER TABLE `tb_breath` DROP FOREIGN KEY `tb_breath_users_id_fkey`;

-- DropForeignKey
ALTER TABLE `tb_meditation` DROP FOREIGN KEY `tb_meditation_users_id_fkey`;

-- DropForeignKey
ALTER TABLE `tb_phone` DROP FOREIGN KEY `tb_phone_users_id_fkey`;

-- DropIndex
DROP INDEX `tb_breath_users_id_fkey` ON `tb_breath`;

-- DropIndex
DROP INDEX `tb_meditation_users_id_fkey` ON `tb_meditation`;

-- DropIndex
DROP INDEX `tb_phone_users_id_fkey` ON `tb_phone`;

-- AddForeignKey
ALTER TABLE `tb_phone` ADD CONSTRAINT `tb_phone_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_meditation` ADD CONSTRAINT `tb_meditation_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_breath` ADD CONSTRAINT `tb_breath_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;
