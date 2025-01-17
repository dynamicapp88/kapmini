/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}"],
  safelist: [
    'underline',
    'bg-gray-100'
  ],
  theme: {
    extend: {},
    container: {
      center: true,
    }
  },
  plugins: [
    require('@tailwindcss/forms')
  ]
}
