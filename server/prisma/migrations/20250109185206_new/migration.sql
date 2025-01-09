-- CreateTable
CREATE TABLE `tb_users` (
    `id_user` INTEGER NOT NULL AUTO_INCREMENT,
    `name_user` VARCHAR(100) NOT NULL,
    `email_user` VARCHAR(50) NOT NULL,
    `cpf_user` VARCHAR(11) NOT NULL,
    `password_user` VARCHAR(255) NOT NULL,
    `lost_password_user` BOOLEAN NOT NULL DEFAULT false,
    `lost_pasword_token_user` VARCHAR(255) NULL,
    `active_user` BOOLEAN NOT NULL DEFAULT true,
    `type_user` ENUM('user', 'therapist') NOT NULL DEFAULT 'user',
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at` DATETIME(3) NOT NULL,

    UNIQUE INDEX `tb_users_email_user_key`(`email_user`),
    UNIQUE INDEX `tb_users_cpf_user_key`(`cpf_user`),
    PRIMARY KEY (`id_user`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `tb_phones` (
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

-- AddForeignKey
ALTER TABLE `tb_phones` ADD CONSTRAINT `tb_phones_users_id_fkey` FOREIGN KEY (`users_id`) REFERENCES `tb_users`(`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE;
