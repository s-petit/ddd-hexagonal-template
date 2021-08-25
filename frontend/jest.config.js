const {defaults} = require('jest-config');

module.exports = {
  verbose: true,
  collectCoverage: true,
  collectCoverageFrom: [
    "src/app/**/*.ts",
    "!src/app/app.module.ts",
    "!src/app/app-routing.module.ts",
  ],
  coverageDirectory: "./build/reports/coverage",
  reporters: [
    "jest-standard-reporter",
    ["jest-junit", {outputDirectory: "./build/reports/jest", outputName: "jest-junit.xml"}]
  ]
};
