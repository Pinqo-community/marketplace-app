// Fonction utilitaire pour charger les données de localStorage avec validation
export const loadFromLocalStorage = <T>(
  key: string,
  validator: (data: any) => data is T,
): T | null => {
  try {
    const savedData = localStorage.getItem(key);
    if (!savedData) return null;

    const parsedData = JSON.parse(savedData);
    return validator(parsedData) ? parsedData : null;
  } catch {
    console.error(
      `Erreur lors de la récupération de ${key} depuis localStorage.`,
    );
    return null;
  }
};

export const saveToLocalStorage = (key: string, data: any) => {
  try {
    localStorage.setItem(key, JSON.stringify(data));
  } catch {
    console.error(`Erreur lors de la sauvegarde de ${key} dans localStorage.`);
  }
};

export const removeFromLocalStorage = (key: string) => {
  try {
    localStorage.removeItem(key);
  } catch {
    console.error(`Erreur lors de la suppression de ${key} dans localStorage.`);
  }
};
