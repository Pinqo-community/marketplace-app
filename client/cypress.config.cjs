module.exports = {
  e2e: {
    baseUrl: "http://localhost:5173",
    defaultCommandTimeout: 15000,
    pageLoadTimeout: 30000,
    requestTimeout: 15000,
    responseTimeout: 15000,
    viewportWidth: 1280,
    viewportHeight: 720,
    video: false,
    screenshotOnRunFailure: true,
    retries: {
      runMode: 2,
      openMode: 0,
    },
    chromeWebSecurity: false,
    testIsolation: true,
    experimentalMemoryManagement: false,
    experimentalRunAllSpecs: false,
    env: {
      API_BASE_URL: process.env.VITE_API_BASE_URL || "http://localhost:8080",
      MOCK_GEOLOCATION: true,
      DEFAULT_LOCATION: {
        latitude: 45.475614,
        longitude: 4.527323,
      },
    },
    setupNodeEvents(on, config) {
      return config;
    },
  },
};
