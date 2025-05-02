//função para formatar o telefone
export const formatPhone = (phone) => {
  if (!phone) return "-";
  const number = String(phone.phone_number).padStart(9, "0");
  return `+${phone.country_code_phone} (${
    phone.area_code_phone
  }) ${number.slice(0, 5)}-${number.slice(5)}`;
};
