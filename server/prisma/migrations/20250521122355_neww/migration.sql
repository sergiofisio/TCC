-- CreateTable
CREATE TABLE `tb_users` (
    `id_user` INTEGER NOT NULL AUTO_INCREMENT,
    `name_user` VARCHAR(100) NOT NULL,
    `email_user` VARCHAR(50) NOT NULL,
    `cpf_user` VARCHAR(11) NOT NULL,
    `password_user` VARCHAR(255) NOT NULL,
    `password_changed` BOOLEAN NOT NULL DEFAULT false,
    `lost_pasword_token_user` VARCHAR(255) NULL,
    `active_user` BOOLEAN NOT NULL DEFAULT true,
    `type_user` ENUM('user', 'admin') NOT NULL DEFAULT 'user',
    `last_login_date_user` DATETIME(3) NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,
    `exercise_habit_user` BOOLEAN NOT NULL DEFAULT false,
    `hydration_habit_user` BOOLEAN NOT NULL DEFAULT false,
    `sleep_habit_user` BOOLEAN NOT NULL DEFAULT false,
    `smoke_habit_user` BOOLEAN NOT NULL DEFAULT false,
    `weight_user` DOUBLE NULL,

    UNIQUE INDEX `tb_users_email_user_key`(`email_user`),
    UNIQUE INDEX `tb_users_cpf_user_key`(`cpf_user`),
    PRIMARY KEY (`id_user`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `tb_phone` (
    `id_phone` INTEGER NOT NULL AUTO_INCREMENT,
    `type_phone` ENUM('casa', 'trabalho', 'celular', 'emergencia') NOT NULL DEFAULT 'celular',
    `country_code_phone` INTEGER NOT NULL DEFAULT 55,
    `area_code_phone` INTEGER NOT NULL,
    `phone_number` INTEGER NOT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    INDEX `tb_phone_users_id_fkey`(`users_id`),
    PRIMARY KEY (`id_phone`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `tb_today` (
    `id_today` INTEGER NOT NULL AUTO_INCREMENT,
    `emotion_today` ENUM('Felicidade', 'Tristeza', 'Raiva', 'Desgosto', 'Medo') NOT NULL,
    `description_today` TEXT NULL,
    `morning_afternoon_evening` ENUM('manhã', 'tarde', 'noite') NOT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    INDEX `tb_today_users_id_fkey`(`users_id`),
    PRIMARY KEY (`id_today`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `tb_meditation` (
    `id_meditation` INTEGER NOT NULL AUTO_INCREMENT,
    `description_meditation` TEXT NOT NULL,
    `think_today_meditation` TEXT NOT NULL,
    `emotion_meditation` ENUM('Felicidade', 'Tristeza', 'Raiva', 'Desgosto', 'Medo') NOT NULL,
    `caracter_meditation` TEXT NOT NULL,
    `type_situation_meditation` ENUM('boa', 'ruim') NOT NULL,
    `users_id` INTEGER NOT NULL,
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    INDEX `tb_meditation_users_id_fkey`(`users_id`),
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

    INDEX `tb_breath_users_id_fkey`(`users_id`),
    PRIMARY KEY (`id_breath`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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

    INDEX `tb_habits_users_id_fkey`(`users_id`),
    PRIMARY KEY (`id_habit`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `tb_phone` ADD CONSTRAINT `tb_phone_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_today` ADD CONSTRAINT `tb_today_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_meditation` ADD CONSTRAINT `tb_meditation_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_breath` ADD CONSTRAINT `tb_breath_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `tb_habits` ADD CONSTRAINT `tb_habits_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;
