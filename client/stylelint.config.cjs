module.exports = {
  extends: ["stylelint-config-standard-scss"],
  plugins: ["stylelint-order"],
  rules: {
    "block-no-empty": true,
    "color-no-invalid-hex": true,
    "value-keyword-case": "lower",
    "unit-no-unknown": true,
    "at-rule-no-unknown": null,
    "scss/at-rule-no-unknown": true,
    "scss/selector-no-redundant-nesting-selector": true,
    "selector-max-id": 0,
    "comment-empty-line-before": "never",
    "scss/no-global-function-names": null,
    "order/properties-order": [
      {
        properties: [
          "display",
          "flex",
          "align-items",
          "justify-content",
          "position",
          "top",
          "right",
          "bottom",
          "left",
        ],
      },
      {
        properties: [
          "width",
          "height",
          "margin",
          "padding",
          "border",
          "background",
        ],
      },
      {
        properties: ["color", "font-size", "text-align"],
      },
      {
        // Police & Texte
        properties: [
          "font-family",
          "font-weight",
          "font-size",
          "font-style",
          "line-height",
          "letter-spacing",
          "text-transform",
          "text-align",
          "text-decoration",
          "white-space",
        ],
      },
      {
        // Animation & Transition
        properties: [
          "animation",
          "animation-duration",
          "animation-timing-function",
          "animation-delay",
          "transition",
          "transition-duration",
          "transition-timing-function",
          "transition-delay",
        ],
      },
      {
        // Typographie & Espacement
        properties: [
          "word-wrap",
          "word-break",
          "overflow-wrap",
          "text-indent",
          "letter-spacing",
          "text-shadow",
        ],
      },
      {
        // Effets visuels
        properties: [
          "box-shadow",
          "text-shadow",
          "filter",
          "opacity",
          "transform",
        ],
      },
    ],
  },
};
