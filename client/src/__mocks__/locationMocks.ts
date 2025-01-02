export const DEFAULT_LOCATION = {
  name: "Maison",
  address: "123 Rue Exemple",
  coordinates: { latitude: 48.8566, longitude: 2.3522 },
} as const;

export const MOCK_NEW_LOCATION = {
  name: "Saint-Chamond",
  address: "5 Rue de Champagne 42400",
  coordinates: { latitude: 45.543408, longitude: 4.598067 },
} as const;

export const MOCK_SUGGESTIONS = [
  {
    label: "5 Rue de Champagne 42400 Saint-Chamond",
    coordinates: [4.598067, 45.543408],
    postcode: "42400",
    city: "Saint-Chamond",
  },
];

export const mockUseSuggestions = {
  suggestions: MOCK_SUGGESTIONS,
  fetchSuggestions: jest.fn(),
  clearSuggestions: jest.fn(),
};

export const mockGeolocation = {
  coordinates: null,
  address: "",
  isLoading: false,
  hasError: false,
  locate: jest.fn(),
};
