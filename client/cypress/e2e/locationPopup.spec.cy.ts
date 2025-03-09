/// <reference types="cypress" />

describe("LocationPopup Component", () => {
  beforeEach(() => {
    cy.visit("http://localhost:5173/");
    cy.clearCookies();
    cy.clearLocalStorage();
  });

  it("should not close the popup when the user has not entered an address", () => {
    cy.get("[data-testid='close-button']").click();
    cy.get("[data-testid='location-popup']").should("exist");
  });

  it("should open the location popup if no location in the localstorage", () => {
    cy.get("[data-testid='location-popup']").should("exist");
    cy.contains("Choisissez votre localisation").should("be.visible");
  });

  // it("should open the location popup in less than 1000ms", () => {
  //   const start = performance.now();

  //   cy.get("[data-testid='location-popup']").should("exist");

  //   const end = performance.now();
  //   cy.log(`Popup opened in ${end - start}ms`);

  //   expect(end - start).to.be.lessThan(1000);
  // });

  // it("should load suggestions in less than 500ms after input", () => {
  //   cy.get("[data-testid='address-input']").type("5 Rue de Champagne");

  //   const start = performance.now();

  //   cy.get("[data-testid='suggestion-list']", { timeout: 500 }).should(
  //     "be.visible"
  //   );

  //   const end = performance.now();
  //   cy.log(`Suggestions loaded in ${end - start}ms`);
  //   expect(end - start).to.be.lessThan(500);
  // });

  // it("should react to button click in less than 100ms", () => {
  //   const start = performance.now();

  //   cy.get("[data-testid='automatic-location-button']").click();
  //   cy.get("[data-testid='location-loading']").should("be.visible");

  //   const end = performance.now();
  //   cy.log(`Response in ${end - start}ms`);
  //   expect(end - start).to.be.lessThan(100);
  // });

  it("should save the address in the localStorage", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";

    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-list']", { timeout: 10000 }).should(
      "be.visible"
    );

    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "true"
    );

    cy.wait(1000);
    cy.window().then((win) => {
      const storedLocations = JSON.parse(
        win.localStorage.getItem("recentLocations") || "[]"
      );
      // Vérifier uniquement la structure
      expect(storedLocations[0]).to.have.keys([
        "name",
        "address",
        "coordinates",
      ]);
      expect(storedLocations[0].coordinates).to.have.keys([
        "latitude",
        "longitude",
      ]);
    });
  });

  it("should add an address automatically via the geolocation button", () => {
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

  it("should warn the user when the geolocation is unavailable", () => {
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

  it("should add an address manually and select it in the suggestions list", () => {
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

  it("should not add invalid address when no suggestion is selected", () => {
    cy.get("[data-testid='address-input']").type("Adresse inconnue");
    cy.get("[data-testid='address-input']").should(
      "have.attr",
      "data-valid",
      "false"
    );
  });

  it("should prevent duplicates in recent addresses", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";

    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-list']", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.wait(1000);

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-list']", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.wait(1000);
    cy.window().then((win) => {
      const storedLocations = JSON.parse(
        win.localStorage.getItem("recentLocations") || "[]"
      );
      expect(storedLocations).to.have.lengthOf(1);
    });
  });

  it("should remove an address from the recent list", () => {
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

  it("should close the popup when the user presses escape", () => {
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

  it("should close the popup when clicking outside", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='overlay']").click({ force: true });

    cy.get("[data-testid='location-popup']").should("not.exist");
  });

  it("should close the popup when clicking the close button", () => {
    const address = "5 Rue de Champagne 42400 Saint-Chamond";
    cy.get("[data-testid='address-input']").type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='address-input']").clear().type(address);
    cy.get("[data-testid='suggestion-item']").first().click();

    cy.get("[data-testid='close-button']").click();

    cy.get("[data-testid='location-popup']").should("not.exist");
  });

  it("Complete user cycle in the location popup", () => {
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
