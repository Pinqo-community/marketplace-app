export interface Suggestion {
  label: string;
  postcode: string;
  city: string;
  coordinates: number[];
}

export interface SuggestionListProps {
  suggestions: Suggestion[];
  onSelect: (suggestion: Suggestion) => void;
}

export interface Coordinates {
  latitude: number;
  longitude: number;
}

export interface Feature {
  properties: {
    label: string;
    context: string;
    postcode: string;
    city: string;
  };
  geometry: {
    coordinates: number[];
  };
}

export interface LocationState {
  address: string;
  isValidAddress: boolean;
  coordinates: Coordinates | null;
  suggestions?: Suggestion[];
}

export interface LocationStatus {
  isLoading: boolean;
  hasError: boolean;
}

export interface LocationSearchState {
  suggestions: Suggestion[];
  isLoading: boolean;
  hasError: boolean;
}
