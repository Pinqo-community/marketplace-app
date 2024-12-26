import {
  Feature,
  LocationSearchState,
  LocationState,
  Suggestion,
} from "@/types/Location";
import { useState, useCallback } from "react";

export const useLocationSearch = () => {
  const [state, setState] = useState<LocationState>({
    address: "",
    coordinates: null,
    isValidAddress: false,
  });

  const [searchState, setSearchState] = useState<LocationSearchState>({
    suggestions: [],
    isLoading: false,
    hasError: false,
  });

  const fetchSuggestions = useCallback(async (query: string) => {
    if (query.length < 3) {
      setSearchState((prev) => ({ ...prev, suggestions: [] }));
      return;
    }

    try {
      const response = await fetch(
        `https://api-adresse.data.gouv.fr/search/?q=${encodeURIComponent(query)}&limit=5&autocomplete=1`,
      );
      const data = await response.json();

      setSearchState((prev) => ({
        ...prev,
        suggestions: data.features.map((feature: Feature) => ({
          label: feature.properties.label,
          coordinates: feature.geometry.coordinates,
          context: feature.properties.context,
          postcode: feature.properties.postcode,
          city: feature.properties.city,
        })),
      }));
    } catch (error) {
      console.error("Erreur lors de la recherche d'adresses", error);
      setSearchState((prev) => ({ ...prev, suggestions: [] }));
    }
  }, []);

  const handleAddressChange = (value: string) => {
    setState((prev) => ({
      ...prev,
      address: value,
      isValidAddress: false,
      coordinates: null,
    }));
    fetchSuggestions(value);
  };

  const handleSelectAddress = (suggestion: Suggestion) => {
    setState({
      address: suggestion.label,
      coordinates: {
        latitude: suggestion.coordinates[1],
        longitude: suggestion.coordinates[0],
      },
      isValidAddress: true,
    });
    setSearchState((prev) => ({ ...prev, suggestions: [] }));
  };

  return {
    state,
    searchState,
    handleAddressChange,
    handleSelectAddress,
    setSearchState,
  };
};
