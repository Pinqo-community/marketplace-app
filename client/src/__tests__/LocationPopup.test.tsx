import LocationPopup from "@/components/LocationPopup/LocationPopup";
import { RootState } from "@/store";
import locationReducer, {
  closeLocationPopup,
  setUserLocation,
} from "@/store/slices/locationSlice";
import { configureStore } from "@reduxjs/toolkit";
import "@testing-library/jest-dom";
import { fireEvent, render, screen } from "@testing-library/react";
import { Provider } from "react-redux";

describe("LocationPopup Component", () => {
  const initialState = {
    location: {
      name: "Paris",
      address: "1 Rue de la Paix, Paris",
      coordinates: { latitude: 48.8566, longitude: 2.3522 },
      recentLocations: [
        {
          name: "Lyon",
          address: "Place Bellecour, Lyon",
          coordinates: { latitude: 45.764, longitude: 4.8357 },
        },
      ],
      isLocationPopupOpen: true,
    },
  };

  // Déclarations des variables nécessaires pour le store et le spy sur dispatch
  let store: ReturnType<typeof configureStore>;
  let dispatchSpy: jest.SpyInstance;

  // Initialisation avant chaque test
  beforeEach(() => {
    // Configuration du store
    store = configureStore({
      reducer: { location: locationReducer },
      preloadedState: initialState,
    });

    // spy pour observer les appels de dispatch
    dispatchSpy = jest.spyOn(store, "dispatch");
  });

  // Test : Vérifie que le popup s'affiche avec le contenu par défaut
  test("renders the popup with default content", () => {
    render(
      <Provider store={store}>
        <LocationPopup
          isOpen={initialState.location.isLocationPopupOpen}
          onClose={() => store.dispatch(closeLocationPopup())}
        />
      </Provider>,
    );

    expect(
      screen.getByText(/Choisissez votre localisation/i),
    ).toBeInTheDocument();
  });

  // Test : Vérifie que la localisation est mise à jour lorsque l'action setUserLocation est dispatchée
  test("updates the location when setUserLocation is dispatched", () => {
    render(
      <Provider store={store}>
        <LocationPopup
          isOpen={initialState.location.isLocationPopupOpen}
          onClose={() => store.dispatch(closeLocationPopup())}
        />
      </Provider>,
    );

    // Dispatch une nouvelle localisation pour simuler un changement d'état
    store.dispatch(
      setUserLocation({
        name: "Lyon",
        address: "Place Bellecour, Lyon",
        coordinates: { latitude: 45.764, longitude: 4.8357 },
      }),
    );

    // Récupère l'état actuel et le typage explicite
    const state = store.getState() as RootState;

    // Vérifie que l'état Redux a été mis à jour correctement
    expect(state.location.name).toBe("Lyon");
    expect(state.location.address).toBe("Place Bellecour, Lyon");
    expect(state.location.coordinates).toEqual({
      latitude: 45.764,
      longitude: 4.8357,
    });
  });

  // Test : Vérifie que l'action closeLocationPopup est dispatchée lors de la fermeture du popup
  test("dispatches closeLocationPopup action when onClose is triggered", () => {
    render(
      <Provider store={store}>
        <LocationPopup
          isOpen={initialState.location.isLocationPopupOpen}
          onClose={() => store.dispatch(closeLocationPopup())}
        />
      </Provider>,
    );

    // Vérifie que l'état initial du popup est ouvert
    const stateBefore = store.getState() as RootState;
    expect(stateBefore.location.isLocationPopupOpen).toBe(true);

    // Clic sur le bouton de fermeture
    const closeButton = screen.getByRole("button", { name: /close/i });
    fireEvent.click(closeButton); // Déclenche l'événement de clic

    // Vérifie que l'action closeLocationPopup a bien été dispatchée
    expect(dispatchSpy).toHaveBeenCalledWith(closeLocationPopup());

    // Vérifie que l'état du popup a été mis à jour à false fermeture
    const stateAfter = store.getState() as RootState;
    expect(stateAfter.location.isLocationPopupOpen).toBe(false);
  });
});
