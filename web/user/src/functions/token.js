//função para salvar no sessionStorage
export function saveItem(name, info) {
  sessionStorage.setItem(name, info);
}

//função para adiquirir informação no sessionStorage
export function getItem(name) {
  return sessionStorage.getItem(name);
}

//função para limpar o sessionStorage
export function clearSesstioon() {
  sessionStorage.clear();
}

//função para remover item do sessionStorage
export function removeItem(name) {
  sessionStorage.removeItem(name);
}
