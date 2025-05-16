-- DropIndex
DROP INDEX `tb_breath_users_id_fkey` ON `tb_breath`;

-- DropIndex
DROP INDEX `tb_meditation_users_id_fkey` ON `tb_meditation`;

-- DropIndex
DROP INDEX `tb_phone_users_id_fkey` ON `tb_phone`;

-- DropIndex
DROP INDEX `tb_today_users_id_fkey` ON `tb_today`;

-- AlterTable
ALTER TABLE `tb_users` ADD COLUMN `exercise_habit_user` BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN `hydration_habit_user` BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN `sleep_habit_user` BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN `smoke_habit_user` BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN `weight_user` DOUBLE NULL;

-- CreateTable
CREATE TABLE `tb_habits` (
    `id_habit` INTEGER NOT NULL AUTO_INCREMENT,
    `name_habit` ENUM('hidratacao', 'fumo', 'exercicio', 'sono') NOT NULL,
    `value_habit` INTEGER NULL,
    `sleep_time_start` DATETIME(3) NULL,
    `sleep_time_end` DATETIME(3) NULL,
    `sleep_time_duration` INTEGER NULL,
    `sleep_feeling` VARCHAR(191) NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    PRIMARY KEY (`id_habit`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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
