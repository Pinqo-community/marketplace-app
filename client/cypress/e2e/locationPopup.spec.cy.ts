/// <reference types="cypress" />

describe("LocationPopup Component", () => {
  beforeEach(() => {
    cy.visit("http://localhost:5173/");
    cy.clearCookies();
    cy.clearLocalStorage();
  });

  it("Verifie que l'utilisateur ne peut pas fermer la popup sans avoir saisie une adresse", () => {
    cy.get("[data-testid='close-button']").click();
    cy.get("[data-testid='location-popup']").should("exist");
  });

  it("Vérifie que la popup de localisation est ouverte si aucune localisation dans le localstorage", () => {
    cy.get("[data-testid='location-popup']").should("exist");
    cy.contains("Choisissez votre localisation").should("be.visible");
  });

  // it("Vérifie que la popup de localisation est ouverte en moins de 1000ms", () => {
  //   const start = performance.now();

  //   cy.get("[data-testid='location-popup']").should("exist");

  //   const end = performance.now();
  //   cy.log(`Popup ouverte en ${end - start}ms`);

  //   expect(end - start).to.be.lessThan(1000);
  // });

  // it("Charge les suggestions en moins de 500ms après une saisie", () => {
  //   cy.get("[data-testid='address-input']").type("5 Rue de Champagne");

  //   const start = performance.now();

  //   cy.get("[data-testid='suggestion-list']", { timeout: 500 }).should(
  //     "be.visible"
  //   );

  //   const end = performance.now();
  //   cy.log(`Suggestions chargées en ${end - start}ms`);
  //   expect(end - start).to.be.lessThan(500);
  // });

  // it("Réagit au clic sur un bouton en moins de 100ms", () => {
  //   const start = performance.now();

  //   cy.get("[data-testid='automatic-location-button']").click();
  //   cy.get("[data-testid='location-loading']").should("be.visible");

  //   const end = performance.now();
  //   cy.log(`Réponse en ${end - start}ms`);
  //   expect(end - start).to.be.lessThan(100);
  // });

  it("Sauvegarde l'adresse dans le localStorage", () => {
    cy.get("[data-testid='address-input']").type(
      "5 Rue de Champagne 42400 Saint-Chamond"
    );

    cy.get("[data-testid='suggestion-list']", { timeout: 500 }).should(
      "be.visible"
    );

    cy.get("[data-testid='suggestion-item']").first().click();
    cy.get("[data-testid='address-input']").should(
      "have.value",
      "5 Rue de Champagne 42400 Saint-Chamond"
    );

    const expectedKey = "recentLocations";
    const expectedValue = JSON.stringify([
      {
        name: "Saint-Chamond",
        address: "5 Rue de Champagne 42400",
        coordinates: {
          latitude: 45.475614,
          longitude: 4.527323,
        },
      },
    ]);

    cy.window().then((win) => {
      const actualValue = win.localStorage.getItem(expectedKey);
      expect(actualValue).to.eq(expectedValue);
    });

    cy.reload();
    cy.get("[data-testid='open-popup-button']").click();

    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "true"
    );
    cy.get("[data-testid='recent-location']")
      .should("be.visible")
      .and("contain.text", "Saint-Chamond");
    cy.get("[data-testid='location-header-text']").should(
      "contain",
      "Saint-Chamond"
    );
  });

  it("Ajoute une adresse automatiquement via le bouton de géolocalisation", () => {
    cy.window().then((win) => {
      cy.stub(win.navigator.geolocation, "getCurrentPosition").callsFake(
        (success) => {
          success({
            coords: {
              latitude: 45.543408,
              longitude: 4.598067,
            },
          });
        }
      );
    });

    cy.get("[data-testid='automatic-location-button']").click();
    cy.get("[data-testid='location-loading']").should("be.visible");

    cy.get("[data-testid='location-loading']", { timeout: 10000 }).should(
      "not.exist"
    );

    cy.get("[data-testid='address-input']", { timeout: 10000 })
      .invoke("val")
      .should("not.be.empty");
    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "true"
    );
    cy.get("[data-testid='location-header-text']").should(
      "contain",
      "Saint-Martin-la-Plaine"
    );
  });

  it("Avertit l'utilisateur lorsque la localisation automatique est indisponible", () => {
    cy.window().then((win) => {
      cy.stub(win.navigator.geolocation, "getCurrentPosition").callsFake(
        (error) => {
          error({
            code: 1,
            message: "User denied Geolocation",
          });
        }
      );
    });

    cy.get("[data-testid='automatic-location-button']").click();
    cy.get("[data-testid='address-input']", { timeout: 10000 }).should(
      "be.empty"
    );
    cy.get("[data-testid='automatic-location-button']").should(
      "have.attr",
      "data-error",
      "true"
    );
  });

  it("Ajoute manuellement une adresse et la sélectionne dans la liste de suggestions", () => {
    cy.get("[data-testid='address-input']").type(
      "5 Rue de Champagne 42400 Saint-Chamond"
    );
    cy.get("[data-testid='suggestion-list']", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("[data-testid='suggestion-item']").first().click();
    cy.get("[data-testid='address-input']").should(
      "have.value",
      "5 Rue de Champagne 42400 Saint-Chamond"
    );
    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "true"
    );
    cy.get("[data-testid='recent-location']")
      .should("be.visible")
      .and("contain.text", "Saint-Chamond");
    cy.get("[data-testid='location-header-text']").should(
      "contain",
      "Saint-Chamond"
    );
  });

  it("N'ajoute pas d'adresse invalide lorsqu'aucune suggestion n'est sélectionnée", () => {
    cy.get("[data-testid='address-input']").type("Adresse inconnue");
    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "false"
    );
  });

  it("Empêche les doublons dans les adresses récentes", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();
    cy.get("[data-testid='recent-location']")
      .should("be.visible")
      .and("contain.text", "Saint-Chamond");

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-item']").first().click();
    cy.get("[data-testid='recent-location']").should("have.length", 1);
  });

  it("Supprime une adresse récente de la liste", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();
    cy.get("[data-testid='recent-location']")
      .should("be.visible")
      .and("contain.text", "Saint-Chamond");

    cy.get("[data-testid='recent-location']").trigger("mouseover");
    cy.get("[data-testid='remove-location']").invoke(
      "css",
      "visibility",
      "visible"
    );
    cy.get("[data-testid='remove-location']").click();
    cy.get("[data-testid='recent-location']").should("not.exist");
    cy.get("[data-testid='no-recent-locations']").should("exist");
  });

  it("Ferme la popup lorsque l'utilisateur appuie sur echap", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='location-popup']").trigger("keydown", {
      key: "Escape",
    });
    cy.get("[data-testid='location-popup']").should("not.exist");
  });

  it("Ferme la popup lorsqu'on clique à l'extérieur", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='overlay']").click({ force: true });

    cy.get("[data-testid='location-popup']").should("not.exist");
  });

  it("Ferme la popup lorsqu'on clique sur le bouton de fermeture", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='close-button']").click();

    cy.get("[data-testid='location-popup']").should("not.exist");
  });

  it("Cycle utilisateur complet dans la popup de localisation", () => {
    cy.get("[data-testid='location-popup']").should("exist");

    cy.window().then((win) => {
      cy.stub(win.navigator.geolocation, "getCurrentPosition").callsFake(
        (success) => {
          success({
            coords: {
              latitude: 45.543408,
              longitude: 4.598067,
            },
          });
        }
      );
    });

    cy.get("[data-testid='automatic-location-button']").click();
    cy.get("[data-testid='location-loading']").should("be.visible");

    cy.get("[data-testid='address-input']", { timeout: 10000 })
      .invoke("val")
      .should("not.be.empty");

    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "true"
    );

    cy.get("[data-testid='location-header-text']").should(
      "contain",
      "Saint-Martin-la-Plaine"
    );

    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-list']", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").should(
      "have.value",
      "5 Rue de Champagne 42400 Saint-Chamond"
    );

    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "true"
    );

    cy.get("[data-testid='recent-location']")
      .should("be.visible")
      .and("contain.text", "Saint-Chamond");

    cy.get("[data-testid='recent-location']").first().trigger("mouseover");
    cy.get("[data-testid='remove-location']")
      .first()
      .invoke("css", "visibility", "visible");
    cy.get("[data-testid='remove-location']").first().click();
    cy.get("[data-testid='recent-location']").should("have.length", 1);

    cy.get("[data-testid='overlay']").click({ force: true });
    cy.get("[data-testid='location-popup']").should("not.exist");
  });
});
