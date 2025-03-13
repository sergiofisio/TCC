-- CreateTable
CREATE TABLE `tb_today` (
    `id_today` INTEGER NOT NULL AUTO_INCREMENT,
    `emotion_today` ENUM('Felicidade', 'Tristeza', 'Raiva', 'Desgosto', 'Medo') NOT NULL,
    `description_today` TEXT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    PRIMARY KEY (`id_today`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `tb_today` ADD CONSTRAINT `tb_today_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;
