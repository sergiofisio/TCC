-- DropForeignKey
ALTER TABLE `tb_breath` DROP FOREIGN KEY `tb_breath_users_id_fkey`;

-- DropForeignKey
ALTER TABLE `tb_habits` DROP FOREIGN KEY `tb_habits_users_id_fkey`;

-- DropForeignKey
ALTER TABLE `tb_meditation` DROP FOREIGN KEY `tb_meditation_users_id_fkey`;

-- DropForeignKey
ALTER TABLE `tb_phone` DROP FOREIGN KEY `tb_phone_users_id_fkey`;

-- DropForeignKey
ALTER TABLE `tb_today` DROP FOREIGN KEY `tb_today_users_id_fkey`;

-- AddForeignKey
ALTER TABLE `tb_phone` ADD CONSTRAINT `tb_phone_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_today` ADD CONSTRAINT `tb_today_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_meditation` ADD CONSTRAINT `tb_meditation_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_breath` ADD CONSTRAINT `tb_breath_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_habits` ADD CONSTRAINT `tb_habits_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;
