import { useState, useCallback } from "react";
import { Suggestion, Feature } from "@/types/Location";

export const useSuggestions = () => {
  const [suggestions, setSuggestions] = useState<Suggestion[]>([]);

  const fetchSuggestions = useCallback(async (query: string) => {
    if (query.length < 3) {
      setSuggestions([]);
      return;
    }

    try {
      const response = await fetch(
        `https://api-adresse.data.gouv.fr/search/?q=${encodeURIComponent(query)}&limit=5&autocomplete=1`,
      );
      const data = await response.json();

      setSuggestions(
        data.features.map((feature: Feature) => ({
          label: feature.properties.label,
          coordinates: feature.geometry.coordinates,
          context: feature.properties.context,
          postcode: feature.properties.postcode,
          city: feature.properties.city,
        })),
      );
    } catch (error) {
      console.error("Erreur lors de la recherche d'adresses", error);
      setSuggestions([]);
    }
  }, []);

  const clearSuggestions = useCallback(() => {
    setSuggestions([]);
  }, []);

  return { suggestions, fetchSuggestions, clearSuggestions };
};
