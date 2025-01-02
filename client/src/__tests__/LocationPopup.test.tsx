import {
  DEFAULT_LOCATION,
  MOCK_NEW_LOCATION,
  mockGeolocation,
  mockUseSuggestions,
} from "@/__mocks__/locationMocks";
import LocationPopup from "@/components/LocationPopup/LocationPopup";
import { useGeolocation } from "@/hooks/useGeolocation";
import locationReducer, {
  addRecentLocation,
  removeRecentLocation,
  setUserLocation,
} from "@/store/slices/locationSlice";
import { configureStore } from "@reduxjs/toolkit";
import { fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { Provider } from "react-redux";

// Configuration des mocks
jest.mock("@/hooks/useSuggestions", () => ({
  useSuggestions: jest.fn(() => mockUseSuggestions),
}));

jest.mock("@/hooks/useGeolocation", () => ({
  useGeolocation: jest.fn(() => mockGeolocation),
}));

describe("LocationPopup", () => {
  type Store = ReturnType<typeof configureStore>;

  let store: Store;
  let onCloseMock: jest.Mock;

  const setupStore = (recentLocations = [DEFAULT_LOCATION]) => {
    return configureStore({
      reducer: { location: locationReducer },
      preloadedState: {
        location: {
          name: null,
          address: null,
          coordinates: null,
          recentLocations,
          isLocationPopupOpen: true,
        },
      },
    });
  };

  const renderLocationPopup = (store: Store) => {
    return render(
      <Provider store={store}>
        <LocationPopup isOpen={true} onClose={onCloseMock} />
      </Provider>,
    );
  };

  beforeEach(() => {
    onCloseMock = jest.fn();
    store = setupStore();
    jest.spyOn(store, "dispatch");

    (useGeolocation as jest.Mock).mockReturnValue(mockGeolocation);
  });

  describe("Affichage", () => {
    it("affiche les localisations récentes", () => {
      renderLocationPopup(store);

      expect(screen.getByText("Localisations récentes")).toBeInTheDocument();
      expect(screen.getByText(DEFAULT_LOCATION.name)).toBeInTheDocument();
      expect(screen.getByText(DEFAULT_LOCATION.address)).toBeInTheDocument();
    });

    it("affiche un message quand aucune localisation récente n'est disponible", () => {
      const emptyStore = setupStore([]);
      renderLocationPopup(emptyStore);

      expect(
        screen.getByText("Aucune localisation récente disponible."),
      ).toBeInTheDocument();
    });

    it("ne montre pas les suggestions si aucune adresse n'est saisie", () => {
      renderLocationPopup(store);

      const input = screen.getByTestId("address-input");
      fireEvent.change(input, { target: { value: "" } });

      expect(screen.queryByText("Suggestions")).not.toBeInTheDocument();
    });
  });

  describe("Interactions utilisateur", () => {
    it("se ferme lors de l'appui sur la touche Échap", async () => {
      renderLocationPopup(store);
      await userEvent.keyboard("{Escape}");
      expect(onCloseMock).toHaveBeenCalled();
    });

    it("se ferme lors du clic sur le bouton de fermeture", () => {
      renderLocationPopup(store);
      fireEvent.click(screen.getByTestId("close-button"));
      expect(onCloseMock).toHaveBeenCalled();
    });

    it("active la géolocalisation via le bouton dédié", () => {
      renderLocationPopup(store);
      const { locate } = useGeolocation();

      fireEvent.click(screen.getByTestId("automatic-location-button"));
      expect(locate).toHaveBeenCalledTimes(1);
    });
  });

  describe("Gestion des localisations", () => {
    it("ajoute une nouvelle localisation via la sélection d'une suggestion", async () => {
      renderLocationPopup(store);

      // Simulation de la saisie et sélection d'une suggestion
      const input = screen.getByTestId("address-input");
      fireEvent.change(input, {
        target: { value: "5 Rue de Champagne 42400 Saint-Chamond" },
      });

      const suggestionList = await screen.findByTestId("suggestion-list");
      expect(suggestionList).toBeVisible();

      const [suggestionItem] = screen.getAllByTestId("suggestion-item");
      fireEvent.click(suggestionItem);

      // Vérification des actions Redux
      expect(store.dispatch).toHaveBeenCalledWith(
        setUserLocation(MOCK_NEW_LOCATION),
      );
      expect(store.dispatch).toHaveBeenCalledWith(
        addRecentLocation(MOCK_NEW_LOCATION),
      );
    });

    it("supprime une localisation récente", () => {
      renderLocationPopup(store);

      fireEvent.click(screen.getByTestId("remove-location"));

      expect(store.dispatch).toHaveBeenCalledWith(
        removeRecentLocation(DEFAULT_LOCATION.coordinates),
      );
    });
  });
});
