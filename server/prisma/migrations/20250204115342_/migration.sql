/*
  Warnings:

  - You are about to drop the column `lost_password_user` on the `tb_users` table. All the data in the column will be lost.
  - You are about to drop the `tb_phones` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE `tb_phones` DROP FOREIGN KEY `tb_phones_users_id_fkey`;

-- AlterTable
ALTER TABLE `tb_users` DROP COLUMN `lost_password_user`;

-- DropTable
DROP TABLE `tb_phones`;

-- CreateTable
CREATE TABLE `tb_phone` (
    `id_phone` INTEGER NOT NULL AUTO_INCREMENT,
    `type_phone` ENUM('home', 'work', 'mobile', 'office', 'clinic', 'emergency') NOT NULL DEFAULT 'mobile',
    `country_code_phone` INTEGER NOT NULL DEFAULT 55,
    `area_code_phone` INTEGER NOT NULL,
    `phone_number` INTEGER NOT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    PRIMARY KEY (`id_phone`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `tb_meditation` (
    `id_meditation` INTEGER NOT NULL AUTO_INCREMENT,
    `description_meditation` TEXT NOT NULL,
    `think_today_meditation` TEXT NOT NULL,
    `emotion_meditation` ENUM('happy', 'sad', 'angry', 'disgoust', 'fear') NOT NULL,
    `caracter_meditation` TEXT NOT NULL,
    `type_situation_meditation` ENUM('good', 'bad') NOT NULL,
    `felt_after_meditation` TEXT NOT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    PRIMARY KEY (`id_meditation`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `tb_breath` (
    `id_breath` INTEGER NOT NULL AUTO_INCREMENT,
    `finished_breath` BOOLEAN NOT NULL DEFAULT true,
    `felt_betther_breath` BOOLEAN NOT NULL DEFAULT true,
    `description_breath` TEXT NOT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    PRIMARY KEY (`id_breath`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `tb_phone` ADD CONSTRAINT `tb_phone_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_meditation` ADD CONSTRAINT `tb_meditation_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_breath` ADD CONSTRAINT `tb_breath_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;
