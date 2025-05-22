// 📦 Importações
const { prisma } = require("../../src/config/prisma"); // Prisma Client para acesso ao banco
const users = require("../data"); // Lista de usuários a serem inseridos

// 🌱 Função de seed para inserir usuários no banco de dados
module.exports = async function seedUsers() {
  try {
    // 🔹 Busca todos os usuários existentes no banco (para evitar duplicatas)
    const usersDb = await prisma.tb_users.findMany();

    // 🔁 Itera sobre os usuários do arquivo de dados
    for (const user of users) {
      // 🔍 Verifica se o e-mail já está cadastrado no banco
      const userExists = usersDb.some(
        (userDb) => userDb.email_user === user.email_user
      );

      // ⚠ Caso o usuário já exista, apenas loga uma mensagem
      if (userExists) {
        console.log(`⚠ Usuário ${user.name_user} já existe no banco.`);
      } else {
        // ✅ Se não existir, cria o usuário com todos os dados relacionados
        await prisma.tb_users.create({
          data: {
            name_user: user.name_user,
            email_user: user.email_user,
            cpf_user: user.cpf_user,
            password_user: user.password_user,
            type_user: user.type_user,
            active_user: user.active_user,
            weight_user: user.weight_user || null,

            // 📞 Telefones vinculados ao usuário
            phone_user: {
              create:
                user.phones_user?.map(
                  ({
                    type_phone,
                    country_code_phone,
                    area_code_phone,
                    phone_number,
                  }) => ({
                    type_phone,
                    country_code_phone,
                    area_code_phone,
                    phone_number,
                  })
                ) || [],
            },

            // 🧘 Meditações vinculadas ao usuário
            meditation_user: {
              create:
                user.meditations_user?.map(
                  ({
                    description_meditation,
                    think_today_meditation,
                    emotion_meditation,
                    caracter_meditation,
                    type_situation_meditation,
                    created_at,
                  }) => ({
                    description_meditation,
                    think_today_meditation,
                    emotion_meditation,
                    caracter_meditation,
                    type_situation_meditation,
                    created_at,
                  })
                ) || [],
            },

            // 🌬️ Respirações vinculadas ao usuário
            breath_user: {
              create:
                user.breaths_user?.map(
                  ({
                    finished_breath,
                    felt_betther_breath,
                    description_breath,
                    created_at,
                  }) => ({
                    finished_breath,
                    felt_betther_breath,
                    description_breath,
                    created_at,
                  })
                ) || [],
            },

            // 😌 Emoções diárias vinculadas ao usuário
            today_user: {
              create:
                user.todays_user?.map(
                  ({
                    emotion_today,
                    description_today,
                    morning_afternoon_evening,
                    created_at,
                  }) => ({
                    emotion_today,
                    description_today,
                    morning_afternoon_evening,
                    created_at,
                  })
                ) || [],
            },
          },
        });

        // ✅ Loga sucesso após criar usuário
        console.log(`✅ Usuário ${user.name_user} criado com sucesso.`);
      }
    }
  } catch (error) {
    // ❌ Em caso de erro, exibe no console
    console.error({ error });

    // Alternativa com cores para mensagens de erro:
    console.error(`❌ Erro ao inserir usuários:`, error);
  }
};
