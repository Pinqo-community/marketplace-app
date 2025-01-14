module.exports = {
  e2e: {
    baseUrl: "http://localhost:5173",
    defaultCommandTimeout: 10000,
    requestTimeout: 10000,
    responseTimeout: 10000,
    env: {
      CI: true,
    },
  },
};
