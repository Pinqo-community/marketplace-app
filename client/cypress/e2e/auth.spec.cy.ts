/// <reference types="cypress" />

describe("Authentication Tests", () => {
  beforeEach(() => {
    cy.clearLocalStorage();
    cy.clearCookies();

    cy.window().then((window) => {
      const recentLocations = [
        {
          name: "Bordeaux",
          address: "",
          coordinates: { latitude: 44.851895, longitude: -0.587877 },
        },
      ];
      window.localStorage.setItem(
        "recentLocations",
        JSON.stringify(recentLocations)
      );
    });

    // Intercepte l'appel API avec l'URL exacte pour le succès
    cy.intercept(
      "POST",
      `${Cypress.env("API_BASE_URL")}/api/v1/auth/login`,
      (req) => {
        // Si les credentials sont valides
        if (
          req.body.email === "test@example.com" &&
          req.body.password === "Pa$$w0rd!"
        ) {
          req.reply({
            statusCode: 200,
            body: {
              tokens: {
                access: {
                  token: "fake-access-token-12345",
                  expiresIn: "15m",
                },
                refresh: {
                  token: "fake-refresh-token-67890",
                  expiresIn: "7d",
                },
              },
              user: {
                id: 1,
                email: "test@example.com",
                firstName: "Test",
                lastName: "User",
              },
            },
          });
        } else {
          // Si les credentials sont invalides
          req.reply({
            statusCode: 401,
            body: {
              message: "Les identifiants sont invalides",
            },
          });
        }
      }
    ).as("loginRequest");

    // Intercepte l'appel API de signup
    cy.intercept(
      "POST",
      `${Cypress.env("API_BASE_URL")}/api/v1/auth/register`,
      (req) => {
        req.reply({
          statusCode: 201,
          body: {
            tokens: {
              access: {
                token: "fake-access-token-new-user",
                expiresIn: "15m",
              },
              refresh: {
                token: "fake-refresh-token-new-user",
                expiresIn: "7d",
              },
            },
            user: {
              id: 2,
              email: req.body.email,
              firstName: req.body.firstName,
              lastName: req.body.lastName,
            },
          },
        });
      }
    ).as("registerRequest");

    cy.visit("/");

    cy.get('[data-testid="location-popup"]').should("be.visible");

    cy.get('[data-testid="recent-location"]').first().click();
    cy.get('[data-testid="recent-location"]').first().click();

    cy.get('[data-testid="location-popup"]').should("not.exist");
  });

  describe("Login Page", () => {
    beforeEach(() => {
      cy.get('[data-testid="user-button"]').first().click({ force: true });
      cy.get("[data-testid=auth-layout]").should("be.visible");
    });

    it("should display login form with all elements", () => {
      cy.get('input[name="email"]').should("be.visible");
      cy.get('input[name="password"]').should("be.visible");
      cy.get("button").contains("Se connecter").should("be.visible");
      cy.get("[data-testid='checkbox']").should("be.exist");
      cy.contains("Pas encore de compte ?").should("be.visible");
      cy.contains("S'inscrire").should("be.visible");
    });

    it("should show error for empty fields", () => {
      cy.get("button").contains("Se connecter").click();
      cy.contains("Veuillez remplir tous les champs").should("be.visible");
    });

    it("should show error for invalid credentials", () => {
      cy.get('input[name="email"]').type("test@invalid.com");
      cy.get('input[name="password"]').type("wrongpassword");
      cy.get("button").contains("Se connecter").click();
      cy.contains("Les identifiants sont invalides").should("be.visible");
    });

    it("should successfully login with valid credentials", () => {
      cy.get('input[name="email"]').type("test@example.com");
      cy.get('input[name="password"]').type("Pa$$w0rd!");
      cy.get("button").contains("Se connecter").click();

      // Vérifie que la requête API a été interceptée
      cy.wait("@loginRequest").its("request.body").should("deep.equal", {
        email: "test@example.com",
        password: "Pa$$w0rd!",
      });

      // Vérifie la redirection et le stockage du token
      cy.url().should("eq", Cypress.config().baseUrl + "/");
      cy.window().its("localStorage.accessToken").should("exist");
      cy.window().its("localStorage.refreshToken").should("exist");
    });
  });

  /* -------------------------------------------------------------------------- */
  /*                                   SignUp                                   */
  /* -------------------------------------------------------------------------- */

  describe("Signup Page", () => {
    beforeEach(() => {
      cy.get('[data-testid="user-button"]').first().click({ force: true });
      cy.get("[data-testid=auth-layout]").should("be.visible");
      cy.contains("S'inscrire").click();
    });

    it("should display signup form with all elements", () => {
      cy.get('input[name="firstName"]').should("be.visible");
      cy.get('input[name="lastName"]').should("be.visible");
      cy.get('input[name="email"]').should("be.visible");
      cy.get('input[name="password"]').should("be.visible");
      cy.get('input[name="confirmPassword"]').should("be.visible");
      cy.get("[data-testid='checkbox']").should("be.exist");
      cy.get("button").contains("S'inscrire").should("be.visible");
    });

    it("should show validation errors for empty fields", () => {
      cy.get("button").contains("S'inscrire").click();
      cy.contains("Le prénom est requis").should("be.visible");
      cy.contains("Le nom est requis").should("be.visible");
      cy.contains("Vous devez accepter les conditions d'utilisation").should(
        "be.visible"
      );
    });

    it("should validate password requirements", () => {
      cy.get('input[name="password"]').type("weak");
      cy.get("button").contains("S'inscrire").click();
      cy.contains("Le mot de passe doit contenir au moins 8 caractères").should(
        "be.visible"
      );
    });

    it("should validate password confirmation", () => {
      cy.get('input[name="password"]').type("StrongPass123!");
      cy.get('input[name="confirmPassword"]').type("DifferentPass123!");
      cy.get("button").contains("S'inscrire").click();
      cy.contains("Les mots de passe ne correspondent pas").should(
        "be.visible"
      );
    });

    it("should validate email format", () => {
      cy.get('input[name="email"]').type("invalid@email");
      cy.get("button").contains("S'inscrire").click();
      cy.contains("Adresse email invalide").should("be.visible");
    });

    it("should successfully register a new user", () => {
      const testUser = {
        firstName: "Jean",
        lastName: "Test",
        email: `test${Date.now()}@example.com`,
        password: "TestPass123!",
      };

      cy.get('input[name="firstName"]').type(testUser.firstName);
      cy.get('input[name="lastName"]').type(testUser.lastName);
      cy.get('input[name="email"]').type(testUser.email);
      cy.get('input[name="password"]').type(testUser.password);
      cy.get('input[name="confirmPassword"]').type(testUser.password);
      cy.get('[data-testid="checkbox-label"]').click();
      cy.get("button").contains("S'inscrire").click();

      // Attendre la réponse de l'API
      cy.wait("@registerRequest");

      // Vérifie la redirection et le stockage du token
      cy.url().should("eq", Cypress.config().baseUrl + "/");
      cy.window().its("localStorage.accessToken").should("exist");
      cy.window().its("localStorage.refreshToken").should("exist");
    });
  });

  describe("Navigation between auth popups", () => {
    it("should open login popup and switch to signup", () => {
      cy.get('[data-testid="user-button"]').first().click({ force: true });
      cy.get("[data-testid=auth-layout]").should("be.visible");

      cy.contains("S'inscrire").click();
      cy.get("[data-testid=signup-form]").should("be.visible");
    });

    it("should open signup popup and switch to login", () => {
      cy.get('[data-testid="user-button"]').first().click({ force: true });
      cy.get("[data-testid=auth-layout]").should("be.visible");

      cy.contains("S'inscrire").click();
      cy.get("[data-testid=signup-form]").should("be.visible");

      cy.contains("Se connecter").click();
      cy.get("[data-testid=login-form]").should("be.visible");
    });
  });
});
