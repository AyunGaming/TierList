export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
    "./src/pages/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        "figma-dark": "#121212",
        "figma-green": "#58a65c",
        "figma-yellow": "#e2b355",
      },
    },
  },
  plugins: [],
};
