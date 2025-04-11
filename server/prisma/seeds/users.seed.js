const users = require("../../../../backup/DBTCC/users");
const { colors } = require("../../src/functions/colors");
const { prisma } = require("../../src/prismaFunctions/prisma");

module.exports = async function seedUsers() {
  try {
    const usersDb = await prisma.tb_users.findMany();

    for (const user of users) {
      const userExists = usersDb.some(
        (userDb) => userDb.email_user === user.email_user
      );

      if (userExists) {
        console.log(
          `${colors.yellow}⚠ Usuário ${user.name_user} já existe no banco.${colors.reset}`
        );
      } else {
        await prisma.tb_users.create({
          data: {
            name_user: user.name_user,
            email_user: user.email_user,
            cpf_user: user.cpf_user,
            password_user: user.password_user,
            type_user: user.type_user,
            active_user: user.active_user,
            phones_user: {
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
            meditations_user: {
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
            breaths_user: {
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
            todays_user: {
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

        console.log(
          `${colors.green}✅ Usuário ${user.name_user} criado com sucesso.${colors.reset}`
        );
      }
    }
  } catch (error) {
    console.error({ error });
    // console.error(
    //   `${colors.red}❌ Erro ao inserir usuários:${colors.reset}`,
    //   error
    // );
  }
};
